package com.bernardo.figueiredo.cow.api.apiconfiguration.boundary;

import java.io.IOException;
import java.util.Objects;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class BaseByteCode {
    private byte[] appointmentByteCode;
    private byte[] auctionByteCode;
    private byte[] bidByteCode;
    private byte[] bovineByteCode;
    private byte[] fieldByteCode;
    private byte[] userByteCode;

    @PostConstruct
    public void init() throws IOException {
        appointmentByteCode = loadFile("smartContracts/appointmentByteCode");
        auctionByteCode = loadFile("smartContracts/auctionByteCode");
        bidByteCode = loadFile("smartContracts/bidByteCode");
        bovineByteCode = loadFile("smartContracts/bovineByteCode");
        fieldByteCode = loadFile("smartContracts/fieldByteCode");
        userByteCode = loadFile("smartContracts/userByteCode");
    }

    private byte[] loadFile(String filePath) throws IOException {

        try (var inputStream = this.getClass().getClassLoader().getResourceAsStream(filePath)) {

            return Objects.requireNonNull(inputStream).readAllBytes();
        }
    }
}
