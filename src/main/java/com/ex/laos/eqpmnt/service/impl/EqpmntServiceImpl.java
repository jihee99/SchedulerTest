package com.ex.laos.eqpmnt.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ex.laos.eqpmnt.dao.EqpmntDao;
import com.ex.laos.eqpmnt.service.EqpmntService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EqpmntServiceImpl implements EqpmntService {

	private final EqpmntDao eqpmntDao;
	@Override
	public List<Map<String, String>> selectEqpmntInspectionHistoryList() {
		return eqpmntDao.selectEqpmntInspectionHistoryList();
	}

	@Override
	public List<Map<String, String>> selectEqpmntInspectionHistoryDetailList() {
		return eqpmntDao.selectEqpmntInspectionHistoryDetailList();
	}

	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> selectEqpmntInspectionDetailsByHstryId(String hstryCode) {
		Map<String, Object> response = new HashMap<>();
		try {
			List<Map<String, String>> listResult = eqpmntDao.selectEqpmntInspectionDetailsByHstryId(hstryCode);
			if (!listResult.isEmpty()) {
				response.put("list", listResult);
				response.put("summary", eqpmntDao.selectEqpmntInspectionSummaryByHstryId(hstryCode));
			}else{
				response.put("error", "Transaction error occurred");
			}
		} catch (Exception e) {
			response.put("error", "Transaction error occurred");
			log.error("Error in selectEqpmntInspectionDetailsByHstryId: ", e);
		}
		return response;
	}

	@Override
	public List<Map<String, String>> selectEqpmntInspectionHistorySearchList(String type, String station, String period) {

		Map<String, String> map = new HashMap<>();

		map.put("type", type);
		map.put("station", station);
		if(!period.isEmpty()){
			String[] dates = period.split("-");
			map.put("startDate", dates[0].trim());
			map.put("endDate", dates[1].trim());
		}else{
			map.put("startDate", "");
			map.put("endDate", "");
		}
		return eqpmntDao.selectEqpmntInspectionHistorySearchList(map);
	}
}
