package com.ex.laos.service;

public interface DamService {

	void uploadOldDataToDatabase(String selected);

	void registerAllData(String selected);

	void uploadPreviousData(String filePath, String selected, int previousDays);

}
