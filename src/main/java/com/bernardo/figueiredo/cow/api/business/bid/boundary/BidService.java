/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.bid.boundary;

import static com.bernardo.figueiredo.cow.api.apiconfiguration.utils.HederaExecutor.*;

import com.bernardo.figueiredo.cow.api.apiconfiguration.boundary.BaseByteCode;
import com.bernardo.figueiredo.cow.api.apiconfiguration.boundary.BaseService;
import com.bernardo.figueiredo.cow.api.apiconfiguration.exceptions.ErrorCode;
import com.bernardo.figueiredo.cow.api.apiconfiguration.exceptions.ErrorCodeException;
import com.bernardo.figueiredo.cow.api.apiconfiguration.utils.HederaReceipt;
import com.bernardo.figueiredo.cow.api.business.auction.boundary.AuctionService;
import com.bernardo.figueiredo.cow.api.business.auction.dto.Auction;
import com.bernardo.figueiredo.cow.api.business.bid.dto.Bid;
import com.bernardo.figueiredo.cow.api.business.bid.dto.BidCreateDTO;
import com.bernardo.figueiredo.cow.api.business.user.boundary.UserService;
import com.bernardo.figueiredo.cow.api.business.user.dto.User;
import com.bernardo.figueiredo.cow.api.utils.EnvUtils;
import com.hedera.hashgraph.sdk.*;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BidService extends BaseService {

    @Autowired
    BidRepository bidRepository;

    @Autowired
    BaseByteCode baseByteCode;

    @Autowired
    UserService userService;

    @Autowired
    AuctionService auctionService;

    public List<Bid> getBids() {
        List<Bid> bids = bidRepository.getBids();

        if (bids.isEmpty()) {
            throw new ErrorCodeException(ErrorCode.BID_NOT_FOUND);
        }

        return bids;
    }

    public Bid getBidById(long id) {
        Bid bid = bidRepository.getBidByBidId(id);

        if (bid == null) {
            throw new ErrorCodeException(ErrorCode.BID_NOT_FOUND);
        }

        return bid;
    }

    public Bid createBid(BidCreateDTO bidCreateDTO) {
        Bid bid;

        HederaReceipt receipt;
        FileId fileId;

        User user = userService.getUserById(bidCreateDTO.getId());
        Auction auction = auctionService.getAuctionById(bidCreateDTO.getIdAuction());

        if (bidCreateDTO.getBidDate().after(new Date())) {
            throw new ErrorCodeException(ErrorCode.BID_DATE_INVALID);
        }

        try (Client client = getHederaClient()) {

            validateClientInstance(client);

            receipt = getHederaContractFile(client);

            validateReceiptStatus(receipt);

            fileId = receipt.getFileId();

            receipt = getHederaContractFileAppend(client, fileId, baseByteCode.getBidByteCode(), 10, 2);

            validateReceiptStatus(receipt);

            Bid newBid = new Bid();
            newBid.setAuction(auction);
            newBid.setBidder(user);
            newBid.setBidValue(bidCreateDTO.getBidValue());
            newBid.setBidDate(bidCreateDTO.getBidDate());

            receipt = getBidDeployReceipt(client, fileId, newBid);

            validateReceiptStatus(receipt);

            if (receipt.getContractId() == null) {
                throw new ErrorCodeException(ErrorCode.HEDERA_CONTRACT_ID_NOT_FOUND);
            }

            newBid.setIdContract(receipt.getContractId().toString());

            bid = bidRepository.save(newBid);

        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.BID_DEPLOY_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.BID_DEPLOY_FAILED));
        } catch (TimeoutException e) {
            throw new ErrorCodeException(ErrorCode.HEDERA_NETWORK_TIMEOUT);
        }
        return bid;
    }

    private HederaReceipt getBidDeployReceipt(Client client, FileId fileId, Bid bid) throws TimeoutException {
        try {
            return buildBidDeployReceipt(client, fileId, bid);
        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.BID_DEPLOY_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.BID_DEPLOY_FAILED));
        }
    }

    private HederaReceipt buildBidDeployReceipt(Client client, FileId byteCodeFileId, Bid newBid)
            throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        ContractCreateTransaction contractCreateTransaction = new ContractCreateTransaction()
                .setBytecodeFileId(byteCodeFileId)
                .setGas(300_000)
                .setAdminKey(EnvUtils.getOperatorKey())
                .setConstructorParameters(new ContractFunctionParameters()
                        .addUint256(BigInteger.valueOf((newBid.getAuction().getId())))
                        .addString(newBid.getBidder().getIdContract())
                        .addUint256(BigInteger.valueOf(newBid.getBidDate().getTime())));

        return execute(client, contractCreateTransaction);
    }
}
