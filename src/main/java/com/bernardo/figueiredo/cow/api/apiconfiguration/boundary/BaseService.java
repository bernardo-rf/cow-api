/*
 *
 * @Copyright 2023 VOID SOFTWARE, S.A.
 *
 */
package com.bernardo.figueiredo.cow.api.apiconfiguration.boundary;

import static com.bernardo.figueiredo.cow.api.apiconfiguration.utils.HederaExecutor.execute;
import static com.bernardo.figueiredo.cow.api.apiconfiguration.utils.HederaExecutor.freezeWithExecute;

import com.bernardo.figueiredo.cow.api.apiconfiguration.exceptions.ErrorCode;
import com.bernardo.figueiredo.cow.api.apiconfiguration.exceptions.ErrorCodeException;
import com.bernardo.figueiredo.cow.api.apiconfiguration.utils.HederaReceipt;
import com.bernardo.figueiredo.cow.api.utils.EnvUtils;
import com.hedera.hashgraph.sdk.*;
import java.util.concurrent.TimeoutException;

public abstract class BaseService {

    public HederaReceipt getHederaContractFile(Client client)
            throws PrecheckStatusException, TimeoutException, ReceiptStatusException {

        FileCreateTransaction fileCreateTransaction = new FileCreateTransaction().setKeys(EnvUtils.getOperatorKey());

        return freezeWithExecute(client, fileCreateTransaction);
    }

    public HederaReceipt getHederaContractFileAppend(
            Client client, FileId fileId, byte[] bytecode, int maxChunks, long maxTransactionFee)
            throws PrecheckStatusException, TimeoutException, ReceiptStatusException {

        FileAppendTransaction fileAppendTransaction = new FileAppendTransaction()
                .setFileId(fileId)
                .setContents(bytecode)
                .setMaxChunks(maxChunks)
                .setMaxTransactionFee(new Hbar(maxTransactionFee));

        return execute(client, fileAppendTransaction);
    }

    protected void validateClientInstance(Client client) {

        client.setOperator(EnvUtils.getOperatorId(), EnvUtils.getOperatorKey());

        if (client.getOperatorPublicKey() == null) {
            throw new ErrorCodeException(ErrorCode.HEDERA_FAILED_INVALID_PUBLIC_KEY);
        }
    }

    protected void validateReceiptStatus(HederaReceipt receipt) {

        if (receipt.getStatus() == Status.RECEIPT_NOT_FOUND) {
            throw new ErrorCodeException(ErrorCode.HEDERA_RECEIPT_STATUS_NOT_FOUND);
        }
    }

    protected void validateReceiptStatusRevert(HederaReceipt receipt) {

        if (receipt.getStatus() == Status.CONTRACT_REVERT_EXECUTED) {
            throw new ErrorCodeException(ErrorCode.HEDERA_RECEIPT_STATUS_CONTRACT_REVERT);
        }
    }
}
