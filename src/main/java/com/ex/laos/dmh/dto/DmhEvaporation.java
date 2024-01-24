package com.ex.laos.dmh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class DmhEvaporation {
    String station;
    Integer year;
    Integer month;
    Integer day;
    Double obsVal;
    Double meanMinTemp;
    Double meanMaxTemp;
    String uuid;
}
