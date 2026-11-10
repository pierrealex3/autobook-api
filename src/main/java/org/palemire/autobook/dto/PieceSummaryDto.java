package org.palemire.autobook.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Getter
@Setter
public class PieceSummaryDto {
    private Long id;
    private String partNumber;
    private String category;
}
