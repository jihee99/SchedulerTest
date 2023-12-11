package com.ex.laos.eqpmnt.service;

import java.util.List;
import java.util.Map;

import com.ex.laos.eqpmnt.dto.EqpmntInspectionDto;
import com.ex.laos.eqpmnt.dto.EqpmntInspectionItemDto;

public interface EqpmntService {

	List<Map<String, String>> selectEqpmntInspectionHistoryList();
	List<Map<String, String>> selectEqpmntInspectionHistoryDetailList();

	Map<String, Object> selectEqpmntInspectionDetailsByHstryId(String hstryCode);

	List<Map<String, String>> selectEqpmntInspectionHistorySearchList(String type, String station, String period);

	List<EqpmntInspectionDto> selectEqpmntInspectionItemList();

	List<EqpmntInspectionDto> selectEqpmntInspectionItemListByType(String type);

	void insertEqpmntInspectionArtcl(EqpmntInspectionDto dto);

	EqpmntInspectionItemDto selectEqpmntInspectionItemDetailById(String artclId);
}
