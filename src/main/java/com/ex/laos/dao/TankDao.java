package com.ex.laos.dao;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ex.laos.dto.tank.PredictionSummaryDto;

@Mapper
public interface TankDao {

	Map<String, BigDecimal> getTankInputParametersByDamId(String damId);

	void insertDamTankPredictionHistory(PredictionSummaryDto psd);

	void insertDamTankPredictionSummary(PredictionSummaryDto psd);

	void insertDamTankPredictionResultOverwrite(PredictionSummaryDto psd);
}
