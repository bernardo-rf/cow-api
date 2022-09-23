package hedera.starter.cowBovine;

import com.hedera.hashgraph.sdk.*;
import hedera.starter.cowBovine.models.Bovine;
import hedera.starter.cowBovine.models.BovineCreateDTO;
import hedera.starter.cowBovine.models.BovineDTO;
import hedera.starter.cowBovine.models.BovineRepository;
import hedera.starter.utilities.EnvUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

@Service
public class BovineService {

    @Autowired
    BovineRepository bovineRepository;

    public Client client = Client.forTestnet();
    private String PATH_BOVINE_BIN = "D:\\Bernardo\\PolitecnicoLeiria\\MEI_CM\\2ano\\final_project\\COW.API\\src\\main\\java\\hedera\\starter\\cowBovine\\Bovine.bin";

    public BovineDTO convertToDto(Bovine bovine){
        return new BovineDTO(bovine.getIdBovine(), bovine.getIdContract(), bovine.getIdOwner(),
                bovine.getIdField(), bovine.getSerialNumber(), bovine.getBirthDate(), bovine.getWeight(),
                bovine.getHeight(), bovine.getBreed(), bovine.getColor(), bovine.getActive(),
                bovine.getObservation(), bovine.getIdBovineParent1(), bovine.getIdBovineParent2(),
                bovine.getGender());
    }

    public Bovine convertToEntity(BovineCreateDTO bovineDTO, String idContract){
        return new Bovine(idContract, bovineDTO.getIdOwner(), bovineDTO.getIdField(), bovineDTO.getSerialNumber(),
                bovineDTO.getBirthDate(), bovineDTO.getWeight(), bovineDTO.getHeight(), bovineDTO.getBreed(),
                bovineDTO.getColor(), bovineDTO.getActive(), bovineDTO.getObservation(),
                bovineDTO.getIdBovineParent1(), bovineDTO.getIdBovineParent2(), bovineDTO.isGender());
    }

    public ContractId createBovine(BovineCreateDTO bovineDTO) {
        ContractId newContractId = null;
        try {
            File myObj = new File(PATH_BOVINE_BIN);
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
                            .setConstructorParameters(new ContractFunctionParameters().addString(bovineDTO.getIdOwner())
                                    .addUint32((int) bovineDTO.getIdField()).addUint32((int)bovineDTO.getSerialNumber())
                                    .addString(bovineDTO.getBirthDate().toString()).addBool(bovineDTO.getActive())
                                    .addString(bovineDTO.getObservation())
                                    .addUint32((int)bovineDTO.getIdBovineParent1())
                                    .addUint32((int)bovineDTO.getIdBovineParent2()).addBool(bovineDTO.isGender()))
                            .execute(client);

                    TransactionReceipt fileReceipt3 = contractCreateTransaction.getReceipt(client);
                    System.out.println("Contract Filled " + fileReceipt3.contractId);

                    newContractId = fileReceipt3.contractId;
                }
            }

        } catch (FileNotFoundException | TimeoutException | PrecheckStatusException | ReceiptStatusException e) {
            e.printStackTrace();
        }

        return newContractId;
    }

    public Bovine updateBovine(Bovine oldBovine, BovineDTO bovineDTO){
        try {
            client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

            TransactionResponse contractCreateTransaction = new ContractExecuteTransaction()
                    .setContractId(ContractId.fromString(bovineDTO.getIdContract()))
                    .setGas(300000)
                    .setFunction("setIDOwner", new ContractFunctionParameters()
                            .addString(bovineDTO.getIdOwner()))
                    .execute(client);

            TransactionReceipt fileReceipt = contractCreateTransaction.getReceipt(client);
            System.out.println("Status " + fileReceipt.status);

            oldBovine.setIdOwner(bovineDTO.getIdOwner());
            oldBovine.setIdField(bovineDTO.getIdField());
            oldBovine.setSerialNumber(bovineDTO.getSerialNumber());
            oldBovine.setBirthDate(bovineDTO.getBirthDate());
            oldBovine.setWeight(bovineDTO.getWeight());
            oldBovine.setHeight(bovineDTO.getHeight());
            oldBovine.setBreed(bovineDTO.getBreed());
            oldBovine.setColor(bovineDTO.getColor());
            oldBovine.setActive(bovineDTO.getActive());
            oldBovine.setObservation(bovineDTO.getObservation());
            oldBovine.setIdBovineParent1(bovineDTO.getIdBovineParent1());
            oldBovine.setIdBovineParent2(bovineDTO.getIdBovineParent2());
            oldBovine.setGender(bovineDTO.getGender());

            return oldBovine;
        } catch (TimeoutException | PrecheckStatusException | ReceiptStatusException e) {
            e.printStackTrace();
        }
        return oldBovine;
    }

    public void deleteBovine(String idContract){
        PrivateKey operatorKey = EnvUtils.getOperatorKey();
        client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

        try {
            assert client.getOperatorAccountId() != null;
            ContractDeleteTransaction transaction = new ContractDeleteTransaction()
                    .setTransferAccountId(client.getOperatorAccountId())
                    .setContractId(ContractId.fromString(idContract));

            TransactionResponse txResponse = transaction.freezeWith(client).sign(operatorKey).execute(client);
            txResponse.getReceipt(client);

        } catch (PrecheckStatusException | TimeoutException | ReceiptStatusException e) {
            e.printStackTrace();
        }
    }

    public List<BovineDTO> updateBovineLocation(List<BovineDTO> bovineDTOS, long idField){
        for (BovineDTO bovineDTO: bovineDTOS) {
            Bovine bovine = bovineRepository.getBovineByIDBovine(bovineDTO.getIdBovine()).get();
            if (bovine.getIdBovine() != 0){
                bovine.setIdField(idField);
                bovineRepository.save(bovine);
            }
        }
        return bovineDTOS;
    }
}
