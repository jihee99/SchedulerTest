// package com.ex.laos.dmh.service.adapter;
//
// import java.util.List;
//
// import org.springframework.stereotype.Service;
//
// import com.ex.laos.dmh.dto.DmhObservationData;
// import com.ex.laos.dmh.mapper.DmhObservationDataParsingMapper;
//
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// @Service
// @RequiredArgsConstructor
// public class DmhDataParsingAdapter {
//
// 	private final DmhObservationDataParsingMapper dao;
//
// 	public String findObsbtrId(String type, String obsvtrNm) {
// 		String obsvtrId = "";
// 		if(type.equals("wl")){
// 			obsvtrId = dao.selectWLObsvtrIdByObsvtrNm(obsvtrNm);
// 		}else if(type.equals("rf")){
// 			obsvtrId = dao.selectRFObsvtrIdByObsvtrNm(obsvtrNm);
// 		}else if(type.equals("fm")){
// 			obsvtrId = dao.selectFMObsvtrIdByObsvtrNm(obsvtrNm);
// 		}
//
// 		return obsvtrId;
// 	}
//
// 	public void insertObservationDataByObsvtrType(String type, List<DmhObservationData> list) {
// 		if(type.equals("wl")){
// 			dao.insertWaterLevelObservationDataList(list);
// 		}else if(type.equals("rf")){
// 			dao.insertRainfallObservationDataList(list);
// 		}else if(type.equals("fm")){
// 			dao.insertFlowmeterObservationDataList(list);
// 		}
// 	}
//
// }
