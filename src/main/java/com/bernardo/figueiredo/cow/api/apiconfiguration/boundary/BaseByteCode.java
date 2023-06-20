package com.bernardo.figueiredo.cow.api.apiconfiguration.boundary;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    public void init() throws IOException, URISyntaxException {
        appointmentByteCode = loadFile("smartContracts/appointmentByteCode");
        auctionByteCode = loadFile("smartContracts/auctionByteCode");
        bidByteCode = loadFile("smartContracts/bidByteCode");
        bovineByteCode = loadFile("smartContracts/bovineByteCode");
        fieldByteCode = loadFile("smartContracts/fieldByteCode");
        userByteCode = loadFile("smartContracts/userByteCode");
    }

    private byte[] loadFile(String filePath) throws IOException, URISyntaxException {
        return Files.readAllBytes(Paths.get(
                Objects.requireNonNull(this.getClass().getClassLoader().getResource(filePath))
                        .toURI()));
    }
}
