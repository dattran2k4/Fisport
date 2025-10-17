package com.Fisport.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {

    private Integer rating;
    private String comment;
}
