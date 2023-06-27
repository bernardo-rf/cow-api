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
    HEDERA_CONTRACT_ID_NOT_FOUND(6, HttpStatus.BAD_REQUEST, "Hedera Error: Pre-checking contract id invalid"),
    HEDERA_FIELD_ID_NOT_FOUND(7, HttpStatus.BAD_REQUEST, "Hedera Error: Pre-checking field id invalid"),
    HEDERA_RECEIPT_STATUS_NOT_FOUND(8, HttpStatus.BAD_REQUEST, "Hedera Error: Receipt Not Found."),
    HEDERA_RECEIPT_STATUS_CONTRACT_REVERT(9, HttpStatus.BAD_REQUEST, "Hedera Error: Receipt Contract Revert"),
    HEDERA_FILE_APPEND_ILLEGAL_ARGS(
            10, HttpStatus.BAD_REQUEST, "Hedera Error: File append failed - illegal args on deploy. "),
    HEDERA_APPOINTMENT_REGISTRATION_FAILED(
            11, HttpStatus.BAD_REQUEST, "Hedera Error: Registering Appointment on Hedera."),
    APPOINTMENT_NOT_FOUND(12, HttpStatus.NOT_FOUND, "Error: No appointment record found."),
    APPOINTMENT_BOVINE_NOT_FOUND(13, HttpStatus.NOT_FOUND, "Error: No appointment records found for bovine."),
    APPOINTMENT_USER_NOT_FOUND(14, HttpStatus.NOT_FOUND, "Error: No appointment records found for user."),
    APPOINTMENT_DEPLOY_FAILED(15, HttpStatus.BAD_REQUEST, "Hedera Error:  Appointment deploy failed."),
    APPOINTMENT_UPDATE_FAILED(16, HttpStatus.BAD_REQUEST, "Hedera Error: Appointment update failed."),
    APPOINTMENT_DELETE_FAILED(17, HttpStatus.BAD_REQUEST, "Hedera Error: Appointment delete failed."),
    SCHEDULE_APPOINTMENT_NOT_FOUND(18, HttpStatus.NOT_FOUND, "Error: No schedule appointment record found."),
    SCHEDULE_APPOINTMENT_BOVINE_NOT_FOUND(
            19, HttpStatus.NOT_FOUND, "Error: No schedule appointment records found for bovine."),
    SCHEDULE_APPOINTMENT_VETERINARY_NOT_FOUND(
            20, HttpStatus.NOT_FOUND, "Error: No schedule appointment records found for veterinary."),
    SCHEDULE_APPOINTMENT_OWNER_NOT_FOUND(
            21, HttpStatus.NOT_FOUND, "Error: No schedule appointment records found for owner."),
    SCHEDULE_APPOINTMENT_DATE_INVALID(22, HttpStatus.BAD_REQUEST, "Error: Schedule appointment date invalid."),
    AUCTION_NOT_FOUND(23, HttpStatus.NOT_FOUND, "Error: No auction record found."),
    AUCTION_DEPLOY_FAILED(24, HttpStatus.BAD_REQUEST, "Hedera Error:  Auction deploy failed."),
    AUCTION_UPDATE_FAILED(25, HttpStatus.BAD_REQUEST, "Hedera Error: Auction update failed."),
    AUCTION_START_DATE_INVALID(26, HttpStatus.BAD_REQUEST, "Error: Auction start date invalid."),
    AUCTION_END_DATE_INVALID(27, HttpStatus.BAD_REQUEST, "Error: Auction end date invalid."),
    BID_NOT_FOUND(28, HttpStatus.NOT_FOUND, "Error: No bid records found."),
    BID_DATE_INVALID(29, HttpStatus.BAD_REQUEST, "Error: Bid date invalid."),
    BID_DEPLOY_FAILED(30, HttpStatus.BAD_REQUEST, "Hedera Error: Bid deploy failed."),
    USER_NOT_FOUND(31, HttpStatus.NOT_FOUND, "Error: No user record found."),
    USER_EMAIL_INVALID(32, HttpStatus.BAD_REQUEST, "Error: User email invalid."),
    AUTHENTICATION_FAILED(33, HttpStatus.BAD_REQUEST, "Error: Authentication failed."),
    AUTHENTICATION_CREATE_FAILED(34, HttpStatus.BAD_REQUEST, "Error: Authentication user failed."),
    USER_TYPE_NOT_FOUND(35, HttpStatus.NOT_FOUND, "Error: No user type record found."),
    USER_TYPE_DESCRIPTION_INVALID(36, HttpStatus.BAD_REQUEST, "Error: No user type description invalid."),
    USER_ACCOUNT_FAILED(37, HttpStatus.BAD_REQUEST, "Hedera Error: User account failed."),
    USER_DEPLOY_FAILED(38, HttpStatus.BAD_REQUEST, "Hedera Error: User deploy failed."),
    USER_UPDATE_FAILED(39, HttpStatus.BAD_REQUEST, "Hedera Error: User update failed."),
    USER_PASSWORD_INVALID(40, HttpStatus.BAD_REQUEST, "Error: User password invalid."),
    ACCESS_TOKEN_CREATE_FAILED(41, HttpStatus.BAD_REQUEST, "Error: User access token create failed."),
    FIELD_NOT_FOUND(42, HttpStatus.NOT_FOUND, "Error: No field record found."),
    FIELD_DEPLOY_FAILED(43, HttpStatus.BAD_REQUEST, "Hedera Error:  Field deploy failed."),
    FIELD_UPDATE_FAILED(44, HttpStatus.BAD_REQUEST, "Hedera Error: Field update failed."),
    FIELD_DELETE_FAILED(45, HttpStatus.BAD_REQUEST, "Hedera Error: Field delete failed."),
    FIELD_ADDRESS_INVALID(46, HttpStatus.BAD_REQUEST, "Error: Field address invalid."),
    FIELD_HISTORY_NOT_FOUND(47, HttpStatus.NOT_FOUND, "Error: No field history record found."),
    FIELD_HISTORY_CREATE_FAILED(48, HttpStatus.BAD_REQUEST, "Error: Field history create failed."),
    BOVINE_NOT_FOUND(49, HttpStatus.NOT_FOUND, "Error: No bovine record found."),
    BOVINE_DEPLOY_FAILED(50, HttpStatus.BAD_REQUEST, "Hedera Error:  Bovine deploy failed."),
    BOVINE_UPDATE_FAILED(51, HttpStatus.BAD_REQUEST, "Hedera Error: Bovine update failed."),
    BOVINE_DELETE_FAILED(52, HttpStatus.BAD_REQUEST, "Hedera Error: Bovine delete failed."),
    BOVINE_SERIAL_NUMBER_INVALID(53, HttpStatus.BAD_REQUEST, "Error: Bovine serial number invalid."),
    HISTORY_FIELD_CREATE_FAILED(54, HttpStatus.BAD_REQUEST, "Error: History Field create failed."),
    BOVINE_MINT_FAILED(54, HttpStatus.BAD_REQUEST, "Hedera Error: Bovine mint failed."),
    HEDERA_ACCOUNT_ID_NOT_FOUND(55, HttpStatus.NOT_FOUND, "Hedera Error: Account id not found..");

    private final int code;

    private final HttpStatus responseStatus;

    private final String errorMessage;
}
