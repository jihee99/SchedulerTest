package com.ex.laos.eqpmnt.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EqpmntDao {
	List<Map<String, String>> selectEqpmntInspectionHistoryList();

	List<Map<String, String>> selectEqpmntInspectionHistorySearchList(Map<String, String> map);
}
