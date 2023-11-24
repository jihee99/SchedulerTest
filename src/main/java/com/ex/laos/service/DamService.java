package com.ex.laos.service;

public interface DamService {

	void registerAllData(String selected);

	void uploadPreviousData(String filePath, String selected, int previousDays);

}
