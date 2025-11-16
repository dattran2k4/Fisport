package com.fisport.dto.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
public class PageResponse<T> implements Serializable {
   private int pageNumber;
   private int pageSize;
   private Long totalElements;
   private int totalPages;
   private transient List<T> data;
}