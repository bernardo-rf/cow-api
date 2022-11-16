package cow.starter.Bovine;

import com.hedera.hashgraph.sdk.*;
import cow.starter.Appointment.models.Appointment;
import cow.starter.Auction.models.Auction;
import cow.starter.Auction.models.AuctionFullInfoDTO;
import cow.starter.Bovine.models.*;
import cow.starter.Field.models.Field;
import cow.starter.Field.models.FieldRepository;
import cow.starter.FieldHistory.FieldHistoryService;
import cow.starter.FieldHistory.models.FieldHistory;
import cow.starter.FieldHistory.models.FieldHistoryCreatedDTO;
import cow.starter.FieldHistory.models.FieldHistoryDTO;
import cow.starter.User.models.User;
import cow.starter.User.models.UserRepository;
import cow.starter.utilities.EnvUtils;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeoutException;

@Service
public class BovineService {

    @Autowired
    BovineRepository bovineRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FieldRepository fieldRepository;

    @Autowired
    FieldHistoryService fieldHistoryService;

    private Client client = Client.forTestnet();

    public BovineDTO convertToDTO(Bovine bovine){
        return new BovineDTO(bovine.getIdBovine(), bovine.getIdContract(), bovine.getUser().getIdWallet(),
                bovine.getField().getIdField(), bovine.getSerialNumber(), bovine.getBirthDate().toString(), bovine.getWeight(),
                bovine.getHeight(), bovine.getBreed(), bovine.getColor(), bovine.getActive(),
                bovine.getObservation(), bovine.getIdBovineParent1(), bovine.getIdBovineParent2(),
                bovine.isGender(), bovine.getImageCID());
    }

    public BovineFullInfoDTO convertToFullDTO(Bovine bovine) {
        return new BovineFullInfoDTO(bovine.getIdBovine(), bovine.getIdContract(), bovine.getUser(),
                bovine.getField(), bovine.getSerialNumber(), bovine.getBirthDate().toString(), bovine.getWeight(),
                bovine.getHeight(), bovine.getBreed(), bovine.getColor(), bovine.getActive(),
                bovine.getObservation(), bovine.getIdBovineParent1(), bovine.getIdBovineParent2(),
                bovine.isGender(), bovine.getImageCID());
    }

    public Bovine convertToEntity(BovineCreateDTO bovineDTO, String idContract){
        return new Bovine(idContract, userRepository.getUserByIDOwner(bovineDTO.getIdOwner()),
                fieldRepository.getField(bovineDTO.getIdField()), bovineDTO.getSerialNumber(), bovineDTO.getBirthDate(),
                bovineDTO.getWeight(), bovineDTO.getHeight(), bovineDTO.getBreed(), bovineDTO.getColor(),
                bovineDTO.getActive(), bovineDTO.getObservation(), bovineDTO.getIdBovineParent1(),
                bovineDTO.getIdBovineParent2(), bovineDTO.isGender(), bovineDTO.getImageCID());
    }

    public boolean checkBovineValues(long idField, String idOwner){
        Field field = fieldRepository.getField(idField);
        if (field != null) {
            User user = userRepository.getUserByIDOwner(idOwner);
            if (user != null) {
                return user.getIdUser() != 0;
            }
        }
        return false;
    }

    public BovineFullInfoDTO getBovine(long idBovine){
        BovineFullInfoDTO bovineFullInfoDTO = new BovineFullInfoDTO();
        Bovine bovine = bovineRepository.getBovine(idBovine);
        if(bovine == null){
            return bovineFullInfoDTO;
        }
        return convertToFullDTO(bovine);
    }

    public List<BovineDTO> getAllBovine(){
        List<BovineDTO> bovineDTOList  = new ArrayList<>();
        List<Bovine> bovines = bovineRepository.getAllBovine();
        if (bovines.isEmpty()){
            return bovineDTOList;
        }

        for (Bovine bovine: bovines) {
            bovineDTOList.add(convertToDTO(bovine));
        }
        return bovineDTOList;
    }

    public List<BovineFullInfoDTO> getAllBovineOwned(String ownerId){
        List<BovineFullInfoDTO> bovineDTOList  = new ArrayList<>();
        List<Bovine> bovines = bovineRepository.getAllBovineIdOwner(ownerId);
        if (bovines.isEmpty()){
            return bovineDTOList;
        }

        for (Bovine bovine: bovines) {
            bovineDTOList.add(convertToFullDTO(bovine));
        }
        return bovineDTOList;
    }

    public List<BovineFullInfoDTO> getAllBovineOwnedToAuction(String ownerId){
        List<BovineFullInfoDTO> bovineDTOList  = new ArrayList<>();
        List<Bovine> bovines = bovineRepository.getAllBovineIdOwnerToAuction(ownerId);
        if (bovines.isEmpty()){
            return bovineDTOList;
        }

        for (Bovine bovine: bovines) {
            bovineDTOList.add(convertToFullDTO(bovine));
        }
        return bovineDTOList;
    }

    public List<BovineFullInfoDTO> getAllBovineNotInField(long idField, String idWallet){
        List<BovineFullInfoDTO> bovineDTOList  = new ArrayList<>();
        List<Bovine> bovines = bovineRepository.getAllBovinesNotIn(idField, idWallet);
        if (bovines.isEmpty()){
            return bovineDTOList;
        }

        for (Bovine bovine: bovines) {
            bovineDTOList.add(convertToFullDTO(bovine));
        }
        return bovineDTOList;
    }

    public List<BovineDTO> getGenealogy(long idBovine){
        List<BovineDTO> bovineDTOList =  new ArrayList<>();
        List<Bovine> bovines = bovineRepository.getGenealogy(idBovine);
        if (bovines.isEmpty()){
            return bovineDTOList;
        }
        for (Bovine bovine:bovines) {
            BovineDTO bovineDTO = convertToDTO(bovine);
            bovineDTOList.add(bovineDTO);
        }
        return bovineDTOList;
    }

    public BovineDTO createBovine(BovineCreateDTO bovineCreateDTO) {
        BovineDTO emptyBovineDTO = new BovineDTO();
        try {
            Bovine bovineAux = bovineRepository.checkBovineSerialNumber(bovineCreateDTO.getSerialNumber(),
                    bovineCreateDTO.getIdOwner());
            if (bovineAux != null){
                emptyBovineDTO.setIdBovine(999999);
                return emptyBovineDTO;
            }

            File myObj = new File(EnvUtils.getProjectPath() + "COW.API\\src\\main\\java\\cow\\starter\\Bovine\\Bovine.bin");
            Scanner myReader = new Scanner(myObj);

            if (checkBovineValues(bovineCreateDTO.getIdField(), bovineCreateDTO.getIdOwner())) {
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
                                .setGas(500000)
                                .setAdminKey(operatorKey)
                                .setConstructorParameters(new ContractFunctionParameters()
                                        .addString(bovineCreateDTO.getIdOwner())
                                        .addUint256(BigInteger.valueOf(bovineCreateDTO.getIdField()))
                                        .addUint256(BigInteger.valueOf(bovineCreateDTO.getSerialNumber()))
                                        .addUint256(BigInteger.valueOf(bovineCreateDTO.getBirthDate().getTime()))
                                        .addBool(bovineCreateDTO.getActive())
                                        .addString(bovineCreateDTO.getObservation())
                                        .addUint256(BigInteger.valueOf(bovineCreateDTO.getIdBovineParent1()))
                                        .addUint256(BigInteger.valueOf(bovineCreateDTO.getIdBovineParent2()))
                                        .addBool(bovineCreateDTO.isGender()))
                                .execute(client);

                        TransactionReceipt fileReceipt3 = contractCreateTransaction.getReceipt(client);
                        System.out.println("Contract Filled " + fileReceipt3.contractId);

                        ContractId contractId = fileReceipt3.contractId;

                        if (contractId != null){
                            Bovine bovine = convertToEntity(bovineCreateDTO, contractId.toString());
                            bovineRepository.save(bovine);
                            BovineDTO newBovineDTO = convertToDTO(bovine);
                            if (newBovineDTO.getIdBovine() != 0) {
                                FieldHistoryCreatedDTO historyCreatedDTO = new FieldHistoryCreatedDTO(
                                        bovineCreateDTO.getIdField(), newBovineDTO.getIdBovine(), new Date());
                                FieldHistoryDTO fieldHistoryDTO = fieldHistoryService.createFieldHistory(historyCreatedDTO);
                                if (fieldHistoryDTO.getIdFieldHistory() != 0){
                                    return newBovineDTO;
                                }
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException | TimeoutException | PrecheckStatusException | ReceiptStatusException  e) {
            e.printStackTrace();
        }

        return emptyBovineDTO;
    }

    public BovineDTO updateBovine(BovineDTO bovineDTO){
        BovineDTO emptyBovineDTO = new BovineDTO();
        try {
            if (checkBovineValues(bovineDTO.getIdField(), bovineDTO.getIdOwner())) {
                client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
                Date birthDate = formatter.parse(bovineDTO.getBirthDate());

                TransactionResponse contractCreateTransaction = new ContractExecuteTransaction()
                        .setContractId(ContractId.fromString(bovineDTO.getIdContract()))
                        .setGas(1000000)
                        .setFunction("setUpdate", new ContractFunctionParameters()
                                .addUint256(BigInteger.valueOf(bovineDTO.getIdField()))
                                .addUint256(BigInteger.valueOf(bovineDTO.getSerialNumber()))
                                .addUint256(BigInteger.valueOf(birthDate.getTime()))
                                .addBool(bovineDTO.getActive())
                                .addUint256(BigInteger.valueOf(bovineDTO.getIdBovineParent1()))
                                .addUint256(BigInteger.valueOf(bovineDTO.getIdBovineParent2()))
                                .addBool(bovineDTO.isGender()))
                        .execute(client);

                TransactionReceipt fileReceipt = contractCreateTransaction.getReceipt(client);
                System.out.println("Status " + fileReceipt.status);

                Bovine bovine = bovineRepository.getBovine(bovineDTO.getIdBovine());
                if (bovine != null) {
                    if(bovine.getField().getIdField() != bovineDTO.getIdField()){
                        FieldHistoryCreatedDTO historyCreatedDTO = new FieldHistoryCreatedDTO(bovineDTO.getIdField(),
                                bovineDTO.getIdBovine(), new Date());
                        FieldHistoryDTO fieldHistoryDTO = fieldHistoryService.createFieldHistory(historyCreatedDTO);
                    }

                    bovine.setUser(userRepository.getUserByIDOwner(bovineDTO.getIdOwner()));
                    bovine.setField(fieldRepository.getField(bovineDTO.getIdField()));
                    bovine.setSerialNumber(bovineDTO.getSerialNumber());
                    bovine.setBirthDate(birthDate);
                    bovine.setWeight(bovineDTO.getWeight());
                    bovine.setHeight(bovineDTO.getHeight());
                    bovine.setBreed(bovineDTO.getBreed());
                    bovine.setColor(bovineDTO.getColor());
                    bovine.setActive(bovineDTO.getActive());
                    bovine.setObservation(bovineDTO.getObservation());
                    bovine.setIdBovineParent1(bovineDTO.getIdBovineParent1());
                    bovine.setIdBovineParent2(bovineDTO.getIdBovineParent2());
                    bovine.setGender(bovineDTO.isGender());
                    bovine.setImageCID(bovineDTO.getImageCID());
                    bovineRepository.save(bovine);

                    return convertToDTO(bovine);
                }
            }
        } catch (TimeoutException | PrecheckStatusException | ReceiptStatusException | ParseException e) {
            e.printStackTrace();
        }
        return emptyBovineDTO;
    }

    public Set<BovineDTO> updateBovineLocation(Set<BovineDTO> bovineDTOS, long idField){
        for (BovineDTO bovineDTO: bovineDTOS) {
            Bovine bovine = bovineRepository.getBovine(bovineDTO.getIdBovine());
            if (bovine != null){
                bovine.setField(fieldRepository.getField(idField));
                bovineRepository.save(bovine);

                FieldHistoryCreatedDTO historyCreatedDTO = new FieldHistoryCreatedDTO(idField, bovineDTO.getIdBovine(),
                        new Date());
                FieldHistoryDTO fieldHistoryDTO = fieldHistoryService.createFieldHistory(historyCreatedDTO);
            }
        }
        return bovineDTOS;
    }

    public boolean deleteBovine(long idBovine){
        try {
            PrivateKey operatorKey = EnvUtils.getOperatorKey();
            client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

            if (client.getOperatorAccountId() != null){
                Bovine bovineToDelete = bovineRepository.getBovine(idBovine);

                if (bovineToDelete !=  null) {
                    ContractDeleteTransaction transaction = new ContractDeleteTransaction()
                            .setTransferAccountId(client.getOperatorAccountId())
                            .setContractId(ContractId.fromString(bovineToDelete.getIdContract()));

                    TransactionResponse txResponse = transaction.freezeWith(client).sign(operatorKey).execute(client);
                    TransactionReceipt receipt = txResponse.getReceipt(client);
                    System.out.println("STATUS:" + receipt.status);

                    bovineRepository.delete(bovineToDelete);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
