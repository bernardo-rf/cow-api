/*
 *
 * @Copyright 2023 VOID SOFTWARE, S.A.
 *
 */
package com.bernardo.figueiredo.cow.api.apiconfiguration.utils;

import com.hedera.hashgraph.sdk.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HederaReceipt {

    private TransactionReceipt transactionReceipt;

    public FileId getFileId() {

        return transactionReceipt.fileId;
    }

    public AccountId getAccountId() {

        return transactionReceipt.accountId;
    }

    public ContractId getContractId() {

        return transactionReceipt.contractId;
    }

    public Status getStatus() {

        return transactionReceipt.status;
    }

    public TopicId getTopicId() {

        return transactionReceipt.topicId;
    }
}
