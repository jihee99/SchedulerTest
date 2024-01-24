package com.ex.laos.dmh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DmhRainfallDownload {
    String station;
    String obsDt;
    Double rf;
}
