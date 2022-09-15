package hedera.starter.cowBovine;

import com.hedera.hashgraph.sdk.*;
import hedera.starter.cowBovine.models.Bovine;
import hedera.starter.cowBovine.models.BovineDTO;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

@Service
public class BovineService {

    public Client client = Client.forTestnet();

    public BovineDTO convertToDto(Bovine bovine){
        return new BovineDTO(bovine.getIdBovine(), bovine.getIdContract(), bovine.getIdOwner(),
                bovine.getIdField(), bovine.getSerialNumber(), bovine.getBirthDate(), bovine.getWeight(),
                bovine.getHeight(), bovine.getBreed(), bovine.getColor(), bovine.getActive(),
                bovine.getObservation(), bovine.getIdBovineParent1(), bovine.getIdBovineParent2(),
                bovine.getGender());
    }

}
