package xyz.thekoc.sysalert.conifg;

public class FieldMissingException extends Exception {
    FieldMissingException(){}
    FieldMissingException(String errorMessage) {
        super(errorMessage);
    }
}
