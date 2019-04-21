package xyz.thekoc.sysalert.conifg;

public class NoSuchRuleException extends Exception {
    NoSuchRuleException(){}
    NoSuchRuleException(String errorMessage) {
        super(errorMessage);
    }
}
