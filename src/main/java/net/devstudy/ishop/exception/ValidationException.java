package net.devstudy.ishop.exception;

public class ValidationException extends IllegalArgumentException {
    public ValidationException(String s){
        super(s);
    }
}

