/*
 *
 * @Copyright 2023 POLITÉCNICO DE LEIRIA, @bernardo-rf.
 *
 */

package cow.starter.auction;

import com.hedera.hashgraph.sdk.*;
import cow.starter.auction.models.Auction;
import cow.starter.auction.models.AuctionCreateDTO;
import cow.starter.auction.models.AuctionDTO;
import cow.starter.auction.models.AuctionFullInfoDTO;
import cow.starter.bovine.models.Bovine;
import cow.starter.bovine.models.BovineRepository;
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
public class AuctionService {

    @Autowired
    AuctionRepository auctionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BovineRepository bovineRepository;

    private Client client = Client.forTestnet();

    public boolean checkAuctionValues(long idBovine, String idOwner) {
        Bovine bovine = bovineRepository.getBovine(idBovine);
        if (bovine != null) {
            User user = userRepository.getUserByIDOwner(idOwner);
            if (user != null) {
                return user.getIdUser() != 0;
            }
        }
        return false;
    }

    public AuctionDTO convertToDTO(Auction auction) {
        return new AuctionDTO(auction.getIdAuction(), auction.getBovine().getIdBovine(), auction.getIdContract(),
                auction.getUser().getIdWallet(), auction.getAuctionDescription(), auction.getStartDate(),
                auction.getEndDate(), auction.getAuctionStatus(), auction.getStartingPrice());
    }

    public AuctionFullInfoDTO convertToFullDTO(Auction auction) {
        return new AuctionFullInfoDTO(auction.getIdAuction(), auction.getIdContract(), auction.getBovine(),
                auction.getUser(), auction.getAuctionDescription(), auction.getStartDate(),
                auction.getEndDate(), auction.getAuctionStatus(), auction.getStartingPrice(), auction.getBidSet());
    }

    public List<AuctionFullInfoDTO> getAllAuctions() {
        List<AuctionFullInfoDTO> auctionDTOList = new ArrayList<>();
        List<Auction> auctions = auctionRepository.getAllAuction();
        if (auctions == null) {
            return auctionDTOList;
        }

        for (Auction auction : auctions) {
            AuctionFullInfoDTO auctionDTO = convertToFullDTO(auction);
            auctionDTOList.add(auctionDTO);
        }
        return auctionDTOList;
    }

    public AuctionFullInfoDTO getAuction(long idAuction) {
        Auction auction = auctionRepository.getAuctionByIDAuction(idAuction);
        if (auction == null) {
            return new AuctionFullInfoDTO();
        }
        return convertToFullDTO(auction);
    }

    public AuctionDTO createAuction(AuctionCreateDTO auctionCreateDTO) {
        AuctionDTO emptyDTO = new AuctionDTO();
        try {
            boolean isCheck = checkAuctionValues(auctionCreateDTO.getIdBovine(), auctionCreateDTO.getIdOwner());
            if (!isCheck) {
                emptyDTO.setIdAuction(999999);
                return emptyDTO;
            }

            File myObj = new File(EnvUtils.getProjectPath() + "auction/Auction.bin");
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
                                    .addUint256(BigInteger.valueOf((auctionCreateDTO.getIdBovine())))
                                    .addString(auctionCreateDTO.getIdOwner())
                                    .addString(auctionCreateDTO.getAuctionDescription())
                                    .addUint256(BigInteger.valueOf(auctionCreateDTO.getStartDate().getTime()))
                                    .addUint256(BigInteger.valueOf(auctionCreateDTO.getEndDate().getTime()))
                                    .addUint32(auctionCreateDTO.getStatus()))
                            .execute(client);

                    TransactionReceipt fileReceipt3 = contractCreateTransaction.getReceipt(client);
                    Logger.getLogger("Contract Filled " + fileReceipt3.contractId);

                    ContractId contractId = fileReceipt3.contractId;

                    if (contractId != null) {
                        Auction auction = new Auction(contractId.toString(),
                                bovineRepository.getBovine(auctionCreateDTO.getIdBovine()),
                                userRepository.getUserByIDOwner(auctionCreateDTO.getIdOwner()),
                                auctionCreateDTO.getAuctionDescription(),
                                auctionCreateDTO.getStartDate(), auctionCreateDTO.getEndDate(),
                                auctionCreateDTO.getStatus(), auctionCreateDTO.getStartingPrice());
                        auctionRepository.save(auction);

                        AuctionDTO auctionDTO = convertToDTO(auction);
                        if (auctionDTO.getIdAuction() != 0) {
                            return auctionDTO;
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

    public AuctionDTO updateAuction(AuctionDTO auctionDTO) {
        AuctionDTO emptyDTO = new AuctionDTO();
        try {
            Auction auctionToUpdate = auctionRepository.getAuctionByIDAuction(auctionDTO.getIdAuction());
            if (auctionToUpdate == null) {
                emptyDTO.setIdAuction(999999);
                return emptyDTO;
            } else if (auctionToUpdate.getAuctionStatus() != 0) {
                return emptyDTO;
            }

            boolean isCheck = checkAuctionValues(auctionDTO.getIdBovine(), auctionDTO.getIdOwner());
            if (!isCheck) {
                return emptyDTO;
            }
            client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

            TransactionResponse contractCreateTransaction = new ContractExecuteTransaction()
                    .setContractId(ContractId.fromString(auctionToUpdate.getIdContract()))
                    .setGas(400000)
                    .setFunction("setUpdate", new ContractFunctionParameters()
                            .addUint256(BigInteger.valueOf(auctionDTO.getIdBovine()))
                            .addUint256(BigInteger.valueOf(auctionDTO.getStartDate().getTime()))
                            .addUint256(BigInteger.valueOf(auctionDTO.getEndDate().getTime())))
                    .execute(client);

            TransactionReceipt fileReceipt = contractCreateTransaction.getReceipt(client);
            Logger.getLogger("Status " + fileReceipt.status);

            auctionToUpdate.setBovine(bovineRepository.getBovine(auctionDTO.getIdBovine()));
            auctionToUpdate.setAuctionDescription(auctionDTO.getAuctionDescription());
            auctionToUpdate.setStartDate(auctionDTO.getStartDate());
            auctionToUpdate.setEndDate(auctionDTO.getEndDate());
            auctionToUpdate.setAuctionStatus(auctionDTO.getStatus());
            auctionRepository.save(auctionToUpdate);

            return convertToDTO(auctionToUpdate);

        } catch (TimeoutException | PrecheckStatusException | ReceiptStatusException e) {
            e.printStackTrace();
        }
        return emptyDTO;
    }

    public AuctionDTO updateAuctionStatus(long idAuction, int status) {
        AuctionDTO emptyDTO = new AuctionDTO();
        Auction auctionToUpdate = auctionRepository.getAuctionByIDAuction(idAuction);
        if (auctionToUpdate == null) {
            emptyDTO.setIdAuction(999999);
            return emptyDTO;
        }

        auctionToUpdate.setAuctionStatus(status);
        auctionRepository.save(auctionToUpdate);

        return convertToDTO(auctionToUpdate);
    }
}
