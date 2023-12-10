package com.ex.laos.eqpmnt.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ex.laos.eqpmnt.dto.EqpmntInspectionDto;

@Mapper
public interface EqpmntDao {
	List<Map<String, String>> selectEqpmntInspectionHistoryList();

	List<Map<String, String>> selectEqpmntInspectionDetailsByHstryId(String hstryCode);

	Map<String, String> selectEqpmntInspectionSummaryByHstryId(String hstryCode);

	List<Map<String, String>> selectEqpmntInspectionHistoryDetailList();

	List<Map<String, String>> selectEqpmntInspectionHistorySearchList(Map<String, String> map);

	List<EqpmntInspectionDto> selectEqpmntInspectionItemList();

	List<EqpmntInspectionDto> selectEqpmntInspectionItemListByType(String type);

	void insertEqpmntInspectionArtcl(EqpmntInspectionDto dto);
}
