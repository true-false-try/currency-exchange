package com.npi.microservices.convertor;

import com.opencsv.bean.AbstractBeanField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CommaDecimalConverter  extends AbstractBeanField<Double,String> {

    @Override
    protected BigDecimal convert(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        String normalized = value.replace(",", ".");
        return new BigDecimal(normalized);
    }
}
