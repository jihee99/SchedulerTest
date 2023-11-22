package com.ex.laos.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class EstimateInflowVo {
    private String date;
    private String RMm;   //강우
    private String QoCms;  //관측
    private String QsCms;  //계산치
}

