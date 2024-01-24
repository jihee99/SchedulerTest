package com.ex.laos.dmh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ex.laos.dmh.dto.DmhObservationData;
import com.ex.laos.dmh.dto.DmhObservatory;

@Mapper
public interface DmhObservationDataParsingMapper {
	// String selectObsvtrIdByObsvtrNm(String type, String obsvtrNm);
	String selectWLObsvtrIdByObsvtrNm(String obsvtrNm);

	String selectRFObsvtrIdByObsvtrNm(String obsvtrNm);

	String selectFMObsvtrIdByObsvtrNm(String obsvtrNm);

	void insertWaterLevelObservationDataList(List<DmhObservationData> list);

	void insertRainfallObservationDataList(List<DmhObservationData> list);

	void insertFlowmeterObservationDataList(List<DmhObservationData> list);

	void updateWaterLevelObservatoryData(DmhObservatory obsvtr);

	void updateRainfallObservatoryData(DmhObservatory obsvtr);

	void updateFlowmeterObservatoryData(DmhObservatory obsvtr);
}
