/*
 *
 * @Copyright 2023 POLITÉCNICO DE LEIRIA, @bernardo-rf.
 *
 */

package cow.starter.bid;

import com.hedera.hashgraph.sdk.*;
import cow.starter.auction.AuctionRepository;
import cow.starter.auction.models.Auction;
import cow.starter.bid.models.Bid;
import cow.starter.bid.models.BidCreateDTO;
import cow.starter.bid.models.BidDTO;
import cow.starter.user.models.User;
import cow.starter.user.models.UserRepository;
import cow.starter.utilities.EnvUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

@Service
public class BidService {

    @Autowired
    BidRepository bidRepository;

    @Autowired
    AuctionRepository auctionRepository;

    @Autowired
    UserRepository userRepository;

    private Client client = Client.forTestnet();

    public boolean checkBidValues(long idAuction, String idOwner) {
        Auction auction = auctionRepository.getAuctionByIDAuction(idAuction);
        if (auction != null) {
            User user = userRepository.getUserByIDOwner(idOwner);
            if (user != null) {
                return user.getIdUser() != 0;
            }
        }
        return false;
    }

    public BidDTO convertToDTO(Bid bid) {
        return new BidDTO(bid.getIdBid(), bid.getIdContract(), bid.getAuction().getIdAuction(),
                bid.getUser().getIdWallet(), bid.getBidValue(), bid.getBidDate());
    }

    public List<BidDTO> getAllBids() {
        List<BidDTO> bidDTOList = new ArrayList<>();
        List<Bid> bids = bidRepository.getAllBids();
        if (bids == null) {
            return bidDTOList;
        }

        for (Bid bid : bids) {
            BidDTO bidDTO = convertToDTO(bid);
            bidDTOList.add(bidDTO);
        }
        return bidDTOList;
    }

    public BidDTO getBid(long idBid) {
        Bid bid = bidRepository.getBidByIDBid(idBid);
        if (bid == null) {
            return new BidDTO();
        }
        return convertToDTO(bid);
    }

    public BidDTO createBid(BidCreateDTO bidCreateDTO) {
        BidDTO emptyDTO = new BidDTO();
        try {
            boolean isCheck = checkBidValues(bidCreateDTO.getIdAuction(), bidCreateDTO.getIdBidder());
            if (!isCheck) {
                emptyDTO.setIdBid(999999);
                return emptyDTO;
            }

            File myObj = new File(EnvUtils.getProjectPath() + "COW.API\\src\\main\\java\\cow\\starter\\Bid\\Bid.bin");
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                PrivateKey operatorKey = EnvUtils.getOperatorKey();
                client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

                String objectByteCode = myReader.nextLine();
                byte[] bytecode = objectByteCode.getBytes(StandardCharsets.UTF_8);

                TransactionResponse fileCreateTx = new FileCreateTransaction()
                        .setKeys(operatorKey)
                        .freezeWith(client)
                        .execute(client);

                TransactionReceipt fileReceipt1 = fileCreateTx.getReceipt(client);
                FileId bytecodeFileId = fileReceipt1.fileId;

                if (bytecodeFileId != null) {
                    TransactionResponse fileAppendTransaction = new FileAppendTransaction()
                            .setFileId(bytecodeFileId)
                            .setContents(bytecode)
                            .setMaxChunks(10)
                            .setMaxTransactionFee(new Hbar(2))
                            .execute(client);
                    TransactionReceipt fileReceipt2 = fileAppendTransaction.getReceipt(client);
                    Logger.getLogger("Contract Created =" + fileReceipt2);

                    TransactionResponse contractCreateTransaction = new ContractCreateTransaction()
                            .setBytecodeFileId(bytecodeFileId)
                            .setGas(300000)
                            .setAdminKey(operatorKey)
                            .setConstructorParameters(new ContractFunctionParameters()
                                    .addUint256(BigInteger.valueOf((bidCreateDTO.getIdAuction())))
                                    .addString(bidCreateDTO.getIdBidder())
                                    .addUint256(BigInteger.valueOf(bidCreateDTO.getBidDate().getTime())))
                            .execute(client);

                    TransactionReceipt fileReceipt3 = contractCreateTransaction.getReceipt(client);
                    Logger.getLogger("Contract Filled " + fileReceipt3.contractId);

                    ContractId contractId = fileReceipt3.contractId;

                    if (contractId != null) {
                        Auction auction = auctionRepository.getAuctionByIDAuction(bidCreateDTO.getIdAuction());
                        if (auction != null) {
                            Bid bid = new Bid(contractId.toString(), auction,
                                    userRepository.getUserByIDOwner(bidCreateDTO.getIdBidder()),
                                    bidCreateDTO.getValue(), bidCreateDTO.getBidDate());
                            bidRepository.save(bid);

                            BidDTO bidDTO = convertToDTO(bid);
                            if (bidDTO.getIdBid() != 0) {
                                return bidDTO;
                            }
                        }
                    }
                }
            }
        } catch (PrecheckStatusException | TimeoutException | FileNotFoundException |
                ReceiptStatusException ex) {
            ex.printStackTrace();
        }
        return emptyDTO;
    }
}
