package com.ex.laos.dmh.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ex.laos.dmh.dto.DmhObservationData;
import com.ex.laos.dmh.dto.DmhObservatory;
import com.ex.laos.dmh.mapper.DmhObservationDataParsingMapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DmhObservationDataParsingService {

	@Value("${model.dmh.dir}")
	private String DMH_DIR;

	@Value("${model.dmh.folders}")
	private String DMH_FOLDERS;

	private final DmhObservationDataParsingMapper dao;

	@SneakyThrows
	@Transactional
	public void parseObservationData(String type, String obsvtrNm) throws FileNotFoundException {
		String newType = getType(type);
		String filePath = DMH_DIR + newType + ".xlsx";

		try (ReadableWorkbook workbook = new ReadableWorkbook(new FileInputStream(new File(filePath)))) {
			Sheet sheet = findSheetByName(workbook, obsvtrNm);
			String obsvtrId = findObsbtrId(type, obsvtrNm);

			List<DmhObservationData> list = new ArrayList<>();
			DmhObservatory obsvtr = new DmhObservatory();
			obsvtr.setObsvtrId(obsvtrId);

			if (sheet != null) {
				try (Stream<Row> rowStream = sheet.openStream()) {
					List<Row> rowList = rowStream.collect(Collectors.toList());
					int rowCount = rowList.size();

					Map<Integer, String> header = new HashMap<>();
					final Integer[] rowIndex = {0};
					rowList.stream().forEach(row -> {

						DmhObservationData obsData = new DmhObservationData();
						obsData.setObsvtrId(obsvtrId);

						int cellIndex = 0;
						for (Cell cell : row) {
							if(rowIndex[0] == 1 && cellIndex == 0){
								obsvtr.setPsnDataBgngYmd(cell.asString());
							}
							if((rowIndex[0] == rowCount-1) && cellIndex==0){
								obsvtr.setPsnDataEndYmd(cell.asString());
							}
							if(rowIndex[0] == 0){
								header.put(cellIndex, cell.getRawValue());
							}else{
								processCell(obsData, cell, header.get(cellIndex));
							}
							cellIndex++;
						}
						if(obsData.getObsVal()!=null){
							list.add(obsData);
						}

						if (row.getRowNum() % 1000 == 0) {
							insertObservationDataByObsvtrType(type, list);
							list.clear();
						}

						rowIndex[0]++;
					});
					updateObservatoryData(type, obsvtr);
					if(!list.isEmpty() ){
						insertObservationDataByObsvtrType(type, list);
					}
				}
			}
		}
	}


	private void processCell(DmhObservationData obsData, Cell cell, String cellIndex) {

		switch (cellIndex.trim()){
			case "obs_dt":
				obsData.setObservationDate(cell.asString());
				break;
			case "water level":
			case "rf":
			case "obs_val":
				obsData.setObsVal(cell.asNumber());
				break;
			case "mean_min_temp":
				obsData.setMeanMinTemp(cell.asNumber());
				break;
			case "mean_max_temp":
				obsData.setMeanMaxTemp(cell.asNumber());
				break;
		}
	}


	private String findObsbtrId(String type, String obsvtrNm) {
		String obsvtrId = "";
		if(type.equals("wl")){
			obsvtrId = dao.selectWLObsvtrIdByObsvtrNm(obsvtrNm);
		}else if(type.equals("rf")){
			obsvtrId = dao.selectRFObsvtrIdByObsvtrNm(obsvtrNm);
		}else if(type.equals("fm")){
			obsvtrId = dao.selectFMObsvtrIdByObsvtrNm(obsvtrNm);
		}

		return obsvtrId;
	}

	private void insertObservationDataByObsvtrType(String type, List<DmhObservationData> list) {
		log.info("{}",list);
		if(type.equals("wl")){
			dao.insertWaterLevelObservationDataList(list);
		}else if(type.equals("rf")){
			dao.insertRainfallObservationDataList(list);
		}else if(type.equals("fm")){
			dao.insertFlowmeterObservationDataList(list);
		}
	}

	private void updateObservatoryData(String type, DmhObservatory obsvtr) {
		if(type.equals("wl")){
			dao.updateWaterLevelObservatoryData(obsvtr);
		}else if(type.equals("rf")){
			dao.updateRainfallObservatoryData(obsvtr);
		}else if(type.equals("fm")){
			dao.updateFlowmeterObservatoryData(obsvtr);
		}
	}

	private String getType(String type){
		String newType = "";
		switch (type){
			case "wl":
				newType = "Waterlevel"; break;
			case "rf":
				newType = "Rainfall"; break;
			case "fm":
				newType =  "Evaporation"; break;
		}
		return newType;
	}

	private Sheet findSheetByName(ReadableWorkbook workbook, String sheetName) {
		return workbook.getSheets()
			.filter(sheet -> sheet.getName().startsWith(sheetName))
			.findFirst()
			.orElse(null);
	}
}
