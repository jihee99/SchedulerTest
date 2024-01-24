package com.ex.laos.dmh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DmhWaterlevelDownload {
    String station;
    String obsDt;
    Double waterlevel;
    Double discharge;
}
