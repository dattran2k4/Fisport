package com.Fisport.dto.request;


import com.Fisport.util.EFieldStatus;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class FieldRequest implements Serializable {

    private String name;
    private String address;
    private String banner;
    private String slug;
    private String description;

    private EFieldStatus status;

    private Long fieldTypeId;
    private Long wardId;


}
