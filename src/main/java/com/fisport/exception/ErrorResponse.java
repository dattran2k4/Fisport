package com.fisport.exception;

import lombok.*;

import java.io.*;
import java.util.Date;

@Getter
@Setter
public class ErrorResponse implements Serializable {

    private Date timestamp;
    private int status;
    private String path;
    private String error;
    private String message;

}
