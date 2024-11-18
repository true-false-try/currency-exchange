package com.npi.microservices.dto.convert;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConvertResponseDto {
    BigDecimal amount;
}
