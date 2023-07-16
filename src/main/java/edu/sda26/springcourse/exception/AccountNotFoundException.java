package edu.sda26.springcourse.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountNotFoundException extends Exception{

    private int errorCode;
    private String message;

    public AccountNotFoundException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String toString() {
        return "CustomerNotFoundException{" +
                "errorCode=" + errorCode +
                ", message='" + message + '\'' +
                '}';
    }
}
