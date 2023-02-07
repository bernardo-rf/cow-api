package cow.starter.Auction;

import com.hedera.hashgraph.sdk.*;
import cow.starter.Auction.models.Auction;
import cow.starter.Auction.models.AuctionCreateDTO;
import cow.starter.Auction.models.AuctionDTO;
import cow.starter.Bovine.models.Bovine;
import cow.starter.Bovine.models.BovineRepository;
import cow.starter.User.models.User;
import cow.starter.User.models.UserRepository;
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
        return new AuctionDTO(auction.getIdAuction(), auction.getIdBovine(), auction.getIdContract(),
                auction.getIdOwner(), auction.getAuctionDescription(), auction.getStartDate(), auction.getEndDate(),
                auction.getStatus(), auction.getStartingPrice());
    }

    public List<AuctionDTO> getAllAuctions() {
        List<AuctionDTO> auctionDTOList = new ArrayList<>();
        List<Auction> auctions = auctionRepository.getAllAuction();
        if (auctions == null) {
            return auctionDTOList;
        }

        for (Auction auction : auctions) {
            AuctionDTO auctionDTO = convertToDTO(auction);
            auctionDTOList.add(auctionDTO);
        }
        return auctionDTOList;
    }

    public AuctionDTO getAuction(long idAuction){
        Auction auction = auctionRepository.getAuctionByIDAuction(idAuction);
        if (auction == null ) {
            return new AuctionDTO();
        }
        return convertToDTO(auction);
    }

    public AuctionDTO createAuction(AuctionCreateDTO auctionCreateDTO) {
        AuctionDTO emptyDTO = new AuctionDTO();
        try {
            boolean isCheck = checkAuctionValues(auctionCreateDTO.getIdBovine(), auctionCreateDTO.getIdOwner());
            if (!isCheck) {
                emptyDTO.setIdAuction(999999);
                return emptyDTO;
            }

            File myObj = new File(EnvUtils.getProjectPath() + "COW.API\\src\\main\\java\\cow\\starter\\Auction\\Auction.bin");
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
                    System.out.println("Contract Created =" + fileReceipt2);

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
                    System.out.println("Contract Filled " + fileReceipt3.contractId);

                    ContractId contractId = fileReceipt3.contractId;

                    if (contractId != null) {
                        Auction auction = new Auction(contractId.toString(), auctionCreateDTO.getIdBovine(),
                                auctionCreateDTO.getIdOwner(), auctionCreateDTO.getAuctionDescription(),
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
            } else if (auctionToUpdate.getStatus() != 0) {
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
            System.out.println("Status " + fileReceipt.status);

            auctionToUpdate.setIdBovine(auctionDTO.getIdBovine());
            auctionToUpdate.setAuctionDescription(auctionDTO.getAuctionDescription());
            auctionToUpdate.setStartDate(auctionDTO.getStartDate());
            auctionToUpdate.setEndDate(auctionDTO.getEndDate());
            auctionToUpdate.setStatus(auctionDTO.getStatus());
            auctionRepository.save(auctionToUpdate);

            return convertToDTO(auctionToUpdate);

        } catch (TimeoutException | PrecheckStatusException | ReceiptStatusException e) {
            e.printStackTrace();
        }
        return emptyDTO;
    }
}
