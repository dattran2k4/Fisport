package com.fisport.dto.response;

<<<<<<< HEAD:src/main/java/com/Fisport/dto/response/VoucherResponse.java
import com.fisport.common.EVoucherStatus;
=======
>>>>>>> 53a0974c0fa4f2d0a8289ecdc996934af2e6fd11:src/main/java/com/fisport/dto/response/VoucherResponse.java
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class VoucherResponse {
    private Long id;
    private String code;
    private String description;
    private Integer discount;
    private Integer limit;
    private LocalDate startDate;
    private LocalDate endDate;
    private EVoucherStatus status;
    private Integer usedCount; // For display
    private boolean isActive;
}
