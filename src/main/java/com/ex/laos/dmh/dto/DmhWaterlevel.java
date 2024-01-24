package com.ex.laos.dmh.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class DmhWaterlevel {
    String station;
    Integer year;
    Integer month;
    Integer day;
    Double waterlevel;
    Double discharge;
    String uuid;

    String observationDate;

}
