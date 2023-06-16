/*
 *
 * @Copyright 2023 VOID SOFTWARE, S.A.
 *
 */
package com.bernardo.figueiredo.cow.api.apiconfiguration.utils;

import com.bernardo.figueiredo.cow.api.apiconfiguration.exceptions.ErrorCode;
import com.bernardo.figueiredo.cow.api.apiconfiguration.exceptions.ErrorCodeException;
import com.hedera.hashgraph.sdk.*;
import java.util.concurrent.TimeoutException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HederaExecutor {

    public static Client getHederaClient() {

        return Client.forTestnet();
    }

    public static <T extends Transaction<T>> HederaReceipt execute(Client client, Transaction<T> transaction)
            throws PrecheckStatusException, TimeoutException, ReceiptStatusException {

        TransactionResponse transactionResponse = transaction.execute(client);
        return new HederaReceipt(transactionResponse.getReceipt(client));
    }

    public static <T extends Transaction<T>> HederaReceipt freezeWithExecute(Client client, Transaction<T> transaction)
            throws PrecheckStatusException, TimeoutException, ReceiptStatusException {

        TransactionResponse transactionResponse = transaction.freezeWith(client).execute(client);
        return new HederaReceipt(transactionResponse.getReceipt(client));
    }

    public static <T extends Transaction<T>> HederaReceipt freezeWithSignExecute(
            Client client, PrivateKey submitKey, Transaction<T> transaction)
            throws PrecheckStatusException, TimeoutException, ReceiptStatusException {

        TransactionResponse transactionResponse =
                transaction.freezeWith(client).sign(submitKey).execute(client);
        return new HederaReceipt(transactionResponse.getReceipt(client));
    }

    public static ContractFunctionResult execute(Client client, ContractCallQuery query)
            throws PrecheckStatusException, TimeoutException {

        return query.execute(client);
    }

    public static AccountInfo execute(Client client, AccountInfoQuery query)
            throws PrecheckStatusException, TimeoutException {

        return query.execute(client);
    }

    public static void validateGas(ReceiptStatusException e) {

        if (e.receipt != null && e.receipt.status == Status.INSUFFICIENT_GAS) {
            throw new ErrorCodeException(ErrorCode.HEDERA_FAILED_INSUFFICIENT_GAS);
        }
    }

    public static ErrorCode validateErrorCode(PrecheckStatusException e, ErrorCode defaultErrorCode) {

        if (e.status.equals(Status.INSUFFICIENT_PAYER_BALANCE)) {
            return ErrorCode.HEDERA_FAILED_INSUFFICIENT_PAYER_BALANCE;
        }

        if (e.status.equals(Status.INVALID_ACCOUNT_ID)) {
            return ErrorCode.HEDERA_FAILED_INVALID_ACCOUNT_ID;
        }

        return defaultErrorCode;
    }
}
