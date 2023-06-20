/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.business.auction.boundary;

import static com.bernardo.figueiredo.cow.api.apiconfiguration.utils.HederaExecutor.*;

import com.bernardo.figueiredo.cow.api.apiconfiguration.boundary.BaseByteCode;
import com.bernardo.figueiredo.cow.api.apiconfiguration.boundary.BaseService;
import com.bernardo.figueiredo.cow.api.apiconfiguration.exceptions.ErrorCode;
import com.bernardo.figueiredo.cow.api.apiconfiguration.exceptions.ErrorCodeException;
import com.bernardo.figueiredo.cow.api.apiconfiguration.utils.HederaReceipt;
import com.bernardo.figueiredo.cow.api.business.auction.dto.Auction;
import com.bernardo.figueiredo.cow.api.business.auction.dto.AuctionCreateDTO;
import com.bernardo.figueiredo.cow.api.business.auction.dto.AuctionDTO;
import com.bernardo.figueiredo.cow.api.business.bovine.boundary.BovineService;
import com.bernardo.figueiredo.cow.api.business.bovine.dto.Bovine;
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
public class AuctionService extends BaseService {

    @Autowired
    AuctionRepository auctionRepository;

    @Autowired
    BaseByteCode baseByteCode;

    @Autowired
    UserService userService;

    @Autowired
    BovineService bovineService;

    public Auction getAuctionById(long id) {

        Auction auction = auctionRepository.getAuctionById(id);

        if (auction == null) {
            throw new ErrorCodeException(ErrorCode.AUCTION_NOT_FOUND);
        }

        return auction;
    }

    public List<Auction> getAuctions() {

        List<Auction> auctions = auctionRepository.getAuctions();

        if (auctions.isEmpty()) {
            throw new ErrorCodeException(ErrorCode.AUCTION_NOT_FOUND);
        }

        return auctions;
    }

    public Auction createAuction(AuctionCreateDTO auctionCreateDTO) {
        Auction auction;

        HederaReceipt receipt;
        FileId fileId;

        User user = userService.getUserById(auctionCreateDTO.getIdAuctioneer());
        Bovine bovine = bovineService.getBovineById(auctionCreateDTO.getIdBovine());

        if (auctionCreateDTO.getStartDate().before(new Date())) {
            throw new ErrorCodeException(ErrorCode.AUCTION_START_DATE_INVALID);
        }

        if (auctionCreateDTO.getEndDate().after(new Date())) {
            throw new ErrorCodeException(ErrorCode.AUCTION_END_DATE_INVALID);
        }

        try (Client client = getHederaClient()) {

            validateClientInstance(client);

            receipt = getHederaContractFile(client);

            validateReceiptStatus(receipt);

            fileId = receipt.getFileId();

            receipt = getHederaContractFileAppend(client, fileId, baseByteCode.getAuctionByteCode(), 10, 2);

            validateReceiptStatus(receipt);

            Auction newAuction = new Auction();
            newAuction.setBovine(bovine);
            newAuction.setAuctioneer(user);
            newAuction.setAuctionDescription(auctionCreateDTO.getAuctionDescription());
            newAuction.setStartDate(auctionCreateDTO.getStartDate());
            newAuction.setEndDate(auctionCreateDTO.getEndDate());
            newAuction.setAuctionStatus(auctionCreateDTO.getAuctionStatus());

            receipt = getAuctionDeployReceipt(client, fileId, newAuction);

            validateReceiptStatus(receipt);

            newAuction.setIdContract(receipt.getContractId().toString());

            if (receipt.getContractId() == null) {
                throw new ErrorCodeException(ErrorCode.HEDERA_CONTRACT_ID_NOT_FOUND);
            }

            auction = auctionRepository.save(newAuction);

        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.APPOINTMENT_DEPLOY_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.APPOINTMENT_DEPLOY_FAILED));
        } catch (TimeoutException e) {
            throw new ErrorCodeException(ErrorCode.HEDERA_NETWORK_TIMEOUT);
        }

        return auction;
    }

    private HederaReceipt getAuctionDeployReceipt(Client client, FileId fileId, Auction auction)
            throws TimeoutException {
        try {
            return buildAuctionDeployReceipt(client, fileId, auction);
        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.AUCTION_DEPLOY_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.AUCTION_DEPLOY_FAILED));
        }
    }

    private HederaReceipt buildAuctionDeployReceipt(Client client, FileId byteCodeFileId, Auction newAuction)
            throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        ContractCreateTransaction contractCreateTransaction = new ContractCreateTransaction()
                .setBytecodeFileId(byteCodeFileId)
                .setGas(300_000)
                .setAdminKey(EnvUtils.getOperatorKey())
                .setConstructorParameters(new ContractFunctionParameters()
                        .addUint256(BigInteger.valueOf((newAuction.getBovine().getIdBovine())))
                        .addString(newAuction.getAuctioneer().getIdContract())
                        .addString(newAuction.getAuctionDescription())
                        .addUint256(BigInteger.valueOf(newAuction.getStartDate().getTime()))
                        .addUint256(BigInteger.valueOf(newAuction.getEndDate().getTime()))
                        .addUint32(newAuction.getAuctionStatus()));

        return execute(client, contractCreateTransaction);
    }

    public Auction updateAuction(long id, AuctionDTO auctionDTO) {

        Auction updateAuction = auctionRepository.getAuctionById(id);
        if (updateAuction == null) {
            throw new ErrorCodeException(ErrorCode.AUCTION_NOT_FOUND);
        }

        User auctioneer = userService.getUserById(auctionDTO.getIdAuctioneer());
        if (auctioneer == null) {
            throw new ErrorCodeException(ErrorCode.USER_NOT_FOUND);
        }

        User highestBidder = userService.getUserById(auctionDTO.getIdHighestBidder());
        if (highestBidder == null) {
            throw new ErrorCodeException(ErrorCode.USER_NOT_FOUND);
        }

        Bovine bovine = bovineService.getBovineById(auctionDTO.getIdBovine());
        if (bovine == null) {
            throw new ErrorCodeException(ErrorCode.BOVINE_NOT_FOUND);
        }

        if (auctionDTO.getStartDate().before(new Date())) {
            throw new ErrorCodeException(ErrorCode.AUCTION_START_DATE_INVALID);
        }

        if (auctionDTO.getEndDate().after(new Date())) {
            throw new ErrorCodeException(ErrorCode.AUCTION_END_DATE_INVALID);
        }

        HederaReceipt receipt;

        try (Client client = getHederaClient()) {

            validateClientInstance(client);

            receipt = getAuctionUpdateReceipt(client, auctionDTO);

            validateReceiptStatus(receipt);

        } catch (TimeoutException e) {
            throw new ErrorCodeException(ErrorCode.HEDERA_NETWORK_TIMEOUT);
        }

        updateAuction.setAuctioneer(auctioneer);
        updateAuction.setHighestBidder(highestBidder);
        updateAuction.setBovine(bovine);
        updateAuction.setAuctionDescription(auctionDTO.getAuctionDescription());
        updateAuction.setStartDate(auctionDTO.getStartDate());
        updateAuction.setEndDate(auctionDTO.getEndDate());
        updateAuction.setAuctionStatus(auctionDTO.getAuctionStatus());

        return auctionRepository.save(updateAuction);
    }

    private HederaReceipt getAuctionUpdateReceipt(Client client, AuctionDTO auctionDTO) throws TimeoutException {
        try {
            return buildAuctionUpdateReceipt(client, auctionDTO);
        } catch (ReceiptStatusException e) {
            validateGas(e);
            throw new ErrorCodeException(ErrorCode.AUCTION_UPDATE_FAILED);
        } catch (PrecheckStatusException e) {
            throw new ErrorCodeException(validateErrorCode(e, ErrorCode.AUCTION_UPDATE_FAILED));
        }
    }

    private HederaReceipt buildAuctionUpdateReceipt(Client client, AuctionDTO auctionDTO)
            throws ReceiptStatusException, PrecheckStatusException, TimeoutException {

        ContractExecuteTransaction contractCreateTransaction = new ContractExecuteTransaction()
                .setContractId(ContractId.fromString(auctionDTO.getIdContract()))
                .setGas(400_000)
                .setFunction(
                        "setUpdate",
                        new ContractFunctionParameters()
                                .addUint256(BigInteger.valueOf(auctionDTO.getIdBovine()))
                                .addUint256(BigInteger.valueOf(
                                        auctionDTO.getStartDate().getTime()))
                                .addUint256(BigInteger.valueOf(
                                        auctionDTO.getEndDate().getTime())));

        return execute(client, contractCreateTransaction);
    }

    public Auction updateAuctionStatus(long id, int status) {
        Auction auction = auctionRepository.getAuctionById(id);

        if (auction == null) {
            throw new ErrorCodeException(ErrorCode.AUCTION_NOT_FOUND);
        }

        auction.setAuctionStatus(status);
        return auctionRepository.save(auction);
    }
}
