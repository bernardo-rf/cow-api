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
    APPOINTMENT_BOVINE_NOT_FOUND(12, HttpStatus.NOT_FOUND, "Error: No appointment records found for bovine."),
    APPOINTMENT_USER_NOT_FOUND(13, HttpStatus.NOT_FOUND, "Error: No appointment records found for user."),
    APPOINTMENT_DEPLOY_FAILED(13, HttpStatus.BAD_REQUEST, "Hedera Error:  Appointment deploy failed."),
    APPOINTMENT_UPDATE_FAILED(14, HttpStatus.BAD_REQUEST, "Hedera Error: Appointment update failed."),
    SCHEDULE_APPOINTMENT_NOT_FOUND(15, HttpStatus.NOT_FOUND, "Error: No schedule appointment record found."),
    SCHEDULE_APPOINTMENT_BOVINE_NOT_FOUND(
            16, HttpStatus.NOT_FOUND, "Error: No schedule appointment records found for bovine."),
    SCHEDULE_APPOINTMENT_VETERINARY_NOT_FOUND(
            17, HttpStatus.NOT_FOUND, "Error: No schedule appointment records found for veterinary."),
    SCHEDULE_APPOINTMENT_OWNER_NOT_FOUND(
            18, HttpStatus.NOT_FOUND, "Error: No schedule appointment records found for owner."),
    SCHEDULE_APPOINTMENT_DATE_INVALID(19, HttpStatus.BAD_REQUEST, "Error: Schedule appointment date invalid."),
    AUCTION_NOT_FOUND(20, HttpStatus.NOT_FOUND, "Error: No auction record found."),
    AUCTION_DEPLOY_FAILED(21, HttpStatus.BAD_REQUEST, "Hedera Error:  Auction deploy failed."),
    AUCTION_UPDATE_FAILED(22, HttpStatus.BAD_REQUEST, "Hedera Error: Auction update failed."),
    AUCTION_START_DATE_INVALID(23, HttpStatus.BAD_REQUEST, "Error: Auction start date invalid."),
    AUCTION_END_DATE_INVALID(24, HttpStatus.BAD_REQUEST, "Error: Auction end date invalid."),
    BID_NOT_FOUND(25, HttpStatus.NOT_FOUND, "Error: No bid records found."),
    BID_DATE_INVALID(26, HttpStatus.BAD_REQUEST, "Error: Bid date invalid."),
    BID_DEPLOY_FAILED(27, HttpStatus.BAD_REQUEST, "Hedera Error: Bid deploy failed."),
    USER_NOT_FOUND(28, HttpStatus.NOT_FOUND, "Error: No user record found."),
    BOVINE_NOT_FOUND(29, HttpStatus.NOT_FOUND, "Error: No bovine record found.");

    private final int code;

    private final HttpStatus responseStatus;

    private final String errorMessage;
}
