/*
 *
 * @Copyright 2023 VOID SOFTWARE, S.A.
 *
 */
package com.bernardo.figueiredo.cow.api.apiconfiguration.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Represents all the possible return error codes of the API
 */
@AllArgsConstructor
@Getter
public enum ErrorCode {
    HEDERA_FAILED_INVALID_PUBLIC_KEY(1, HttpStatus.BAD_REQUEST, "Hedera Error: Operator public key invalid."),
    HEDERA_FAILED_INVALID_ACCOUNT_ID(2, HttpStatus.BAD_REQUEST, "Hedera Error: Pre-checking account id invalid"),
    HEDERA_FAILED_INSUFFICIENT_PAYER_BALANCE(
            3, HttpStatus.BAD_REQUEST, "Hedera Error: Pre-checking account - insufficient payer balance "),
    HEDERA_FAILED_INSUFFICIENT_GAS(
            4, HttpStatus.BAD_REQUEST, "Hedera Error: Pre-checking transaction - insufficient gas on deploy."),
    HEDERA_NETWORK_TIMEOUT(5, HttpStatus.BAD_REQUEST, "Hedera Error: Network Timeout."),
    HEDERA_CONTRACT_ID_NOT_FOUND(6, HttpStatus.BAD_REQUEST, "Hedera Error: Pre-checking account id invalid"),
    HEDERA_RECEIPT_STATUS_NOT_FOUND(7, HttpStatus.BAD_REQUEST, "Hedera Error: Receipt Not Found."),
    HEDERA_RECEIPT_STATUS_CONTRACT_REVERT(8, HttpStatus.BAD_REQUEST, "Hedera Error: Receipt Contract Revert"),
    HEDERA_FILE_APPEND_ILLEGAL_ARGS(
            9, HttpStatus.BAD_REQUEST, "Hedera Error: File append failed - illegal args on deploy. "),
    HEDERA_APPOINTMENT_REGISTRATION_FAILED(
            10, HttpStatus.BAD_REQUEST, "Hedera Error: Registering Appointment on Hedera."),
    APPOINTMENT_NOT_FOUND(11, HttpStatus.NOT_FOUND, "Error: No appointment record found."),
    APPOINTMENT_BOVINE_NOT_FOUND(12, HttpStatus.NOT_FOUND, "No appointment records found for bovine."),
    APPOINTMENT_DEPLOY_FAILED(13, HttpStatus.BAD_REQUEST, "Hedera Error:  Appointment deploy failed."),
    APPOINTMENT_UPDATE_FAILED(14, HttpStatus.BAD_REQUEST, "Error: Appointment update failed."),
    USER_NOT_FOUND(15, HttpStatus.NOT_FOUND, "Error: No user record found."),
    BOVINE_NOT_FOUND(16, HttpStatus.NOT_FOUND, "Error: No bovine record found.");

    private final int code;

    private final HttpStatus responseStatus;

    private final String errorMessage;
}
