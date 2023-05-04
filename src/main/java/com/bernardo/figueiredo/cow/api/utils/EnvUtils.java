/*
 *
 * @Copyright 2023 Politécnico de Leiria, @bernardo-rf.
 *
 */

package com.bernardo.figueiredo.cow.api.utils;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.Objects;

public class EnvUtils {

    static Dotenv getEnv() {
        return Dotenv.load();
    }

    public static AccountId getOperatorId() {
        return AccountId.fromString(Objects.requireNonNull(getEnv().get("OPERATOR_ID")));
    }

    public static PrivateKey getOperatorKey() {
        return PrivateKey.fromString(Objects.requireNonNull(getEnv().get("OPERATOR_KEY")));
    }

    public static String getProjectPath() {
        return Objects.requireNonNull(getEnv().get("PROJECT_PATH"));
    }
}
