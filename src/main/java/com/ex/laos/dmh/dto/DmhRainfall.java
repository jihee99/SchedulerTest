package com.ex.laos.dmh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class DmhRainfall {
    String station;
    Integer year;
    Integer month;
    Integer day;
    Double rainfall;
    String uuid;
}
