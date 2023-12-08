package com.ex.laos.eqpmnt.service;

import java.util.List;
import java.util.Map;

public interface EqpmntService {

	List<Map<String, String>> selectEqpmntInspectionHistoryList();
	List<Map<String, String>> selectEqpmntInspectionHistoryDetailList();

	Map<String, Object> selectEqpmntInspectionDetailsByHstryId(String hstryCode);

	List<Map<String, String>> selectEqpmntInspectionHistorySearchList(String type, String station, String period);
}
