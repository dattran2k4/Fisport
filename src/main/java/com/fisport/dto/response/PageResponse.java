package com.fisport.dto.response;

import lombok.*;

@Getter
@Builder
public class PageResponse<T> {
   int pageNumber;
   int pageSize;
   int totalElements;
   int totalPages;
   T data;
}