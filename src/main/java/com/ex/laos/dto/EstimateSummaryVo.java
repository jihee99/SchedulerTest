package com.ex.laos.dto;

import java.util.ArrayList;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class EstimateSummaryVo {
    private String tankSimId;
    private String damId;

    private String predictionBeginYmd;
    private String predictionEndYmd;

    private String realRainfall;
    private String observedFlowDept;
    private String computedFlowDept;
    private String evapotranspiration;
    private String ratio;

    private String obsMean;
    private String obsSdev;
    private String simMean;
    private String simSdev;

    private ArrayList<EstimateInflowVo> inflows;

}
