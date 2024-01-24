package com.ex.laos.dmh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DmhEvaporationDownload {
    String station;
    String obsDt;
    Double obsVal;
    Double meanMinTemp;
    Double meanMaxTemp;
}
