package com.shophub_backend.exception;

public class CartItemException extends RuntimeException{
    public CartItemException(String msg){
        super(msg);
    }
}
