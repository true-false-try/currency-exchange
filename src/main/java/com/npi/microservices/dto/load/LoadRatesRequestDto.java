package com.npi.microservices.dto.load;

import com.npi.microservices.validation.annotation.Alpha2Code;
import com.npi.microservices.validation.annotation.Posix;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Posix
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoadRatesRequestDto {
    private Long date;

    @NotNull
    @NotEmpty
    @Alpha2Code
    private String country;

}
