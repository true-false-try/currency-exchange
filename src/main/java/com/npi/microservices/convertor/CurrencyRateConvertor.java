package com.npi.microservices.convertor;

import com.npi.microservices.dto.external_endpoint.ExternalEndpointResponseMdDto;
import com.npi.microservices.exception.CsvParseException;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.npi.microservices.constant.Constants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrencyRateConvertor {

    public List<ExternalEndpointResponseMdDto> parseCSV(String csvContent, Long posixDate) {
        try {
            log.debug("Before convert to UTF-8: {}", csvContent);
            String utf8Content = ensureUTF8(csvContent);
            log.info("After convert to UTF-8: {}", utf8Content);

            // Process CSV content
            try (BufferedReader reader = new BufferedReader(new StringReader(utf8Content))) {
                String line;
                StringBuilder csvData = new StringBuilder();
                String date = null;
                boolean headerFound = false;

                // Process CSV content to extract date and prepare data
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith(CSV_FIELD_DATE)) {
                        // Extract date from the line
                        String[] parts = line.split(";");
                        if (parts.length > 1) {
                            date = parts[1].trim();
                        }
                    }else  if (line.isEmpty()) {
                        headerFound = false;

                    } else if (!headerFound) {
                        // Look for the actual header line
                        if (line.contains(CSV_FIELD_CURRENCY) &&
                                line.contains(CSV_FIELD_CODE) &&
                                line.contains(CSV_FIELD_SHORT_CODE) &&
                                line.contains(CSV_FIELD_NOMINAL) &&
                                line.contains(CSV_FIELD_RATE)) {
                            headerFound = true;
                            csvData.append(line).append("\n");
                        }
                    } else {
                        // Collect the data lines
                        csvData.append(line).append("\n");
                    }
                }

                if (date == null) {
                    throw new IllegalArgumentException("Date or CSV header not found.");
                }

                // Log CSV data for debugging
                log.debug("CSV Data to be parsed: \n{}", csvData);

                // Parse the collected CSV data
                try (StringReader stringReader = new StringReader(csvData.toString())) {
                    CsvToBean<ExternalEndpointResponseMdDto> csvToBean = new CsvToBeanBuilder<ExternalEndpointResponseMdDto>(stringReader)
                            .withType(ExternalEndpointResponseMdDto.class)
                            .withIgnoreLeadingWhiteSpace(true)
                            .withSeparator(';')
                            .withQuoteChar('"')
                            .build();

                    List<ExternalEndpointResponseMdDto> result = csvToBean.parse();

                    // Set the extracted date in each DTO
                    for (ExternalEndpointResponseMdDto dto : result) {
                        dto.setDate(posixDate);
                    }

                    log.debug("Parsed CSV Data: \n{}", result);

                    return result;
                }
            }
        } catch (CsvParseException | IOException ex) {
            throw new CsvParseException("Failed to parse CSV data");
        }
    }

    private String ensureUTF8(String content) {
        return new String(content.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }
}
