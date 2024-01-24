package com.ex.laos.dmh.service;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ex.laos.dmh.mapper.DmhFileParsingMapper;
import com.ex.laos.dmh.dto.DmhEvaporation;
import com.ex.laos.dmh.dto.DmhRainfall;
import com.ex.laos.dmh.dto.DmhWaterlevel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DmhFileParsingService {

	private final DmhFileParsingMapper dao;

	@Value("${model.dmh.dir}")
	private String DMH_DIR;

	@Value("${model.dmh.folders}")
	private String DMH_FOLDERS;

	public void dmhEvaporationFileParsing(int pathIdx) {
		String uuid = UUID.randomUUID().toString();
		String[] folders = DMH_FOLDERS.split(",");

		for(String folder : folders) {
			File[] files = Paths.get(DMH_DIR, folder).toFile().listFiles();
			if(files != null) {
				for(File file : files) {
					if(folder.startsWith("Eva")) {
						dmhEvaporationFileParsing(file, uuid);
					} else if (folder.startsWith("Rain"))  {
						dmhRainfallFileParsing(file, uuid);
					} else { // Water Lever
						dmhWaterLevelFileParsing(file, uuid);
					}
				}
			}
		}
	}

	public void dmhWaterLevelFileParsing(File file, String uuid){
		try(FileInputStream is = new FileInputStream(file)) {
			List<DmhRainfall> voList = new ArrayList<>();

			Workbook workbook = null;
			if (file.getPath().endsWith(".xls")) {
				workbook = new HSSFWorkbook(is);
			} else if (file.getPath().endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(is);
			}
			// 해당 인덱스에 있는 시트를 반환.
			int sheets = workbook.getNumberOfSheets();

			for(int s = 0; s < sheets; s++) {
				Sheet sheet = workbook.getSheetAt(s);
				// 모든 행의 개수를 반환.
				int rows = sheet.getLastRowNum();

				for(int i = 0; i <= rows; i++){
					Row row = sheet.getRow(i);
					String station = file.getName().replace(".xlsx", "").replace(".xls","").replaceAll("[0-9]+.|-|_|[0-9]|Y", "").trim();
					int year = 0;
					int dateRowIdx = 2;


					if (file.getName().startsWith("1.") || file.getName().startsWith("5.") ) { /* discharge 없음 */
						if (file.getName().startsWith("1.")) {
							if(sheet.getRow(i) != null &&
								sheet.getRow(i).getCell(0) != null &&
								sheet.getRow(i).getCell(0).getCellType() == CellType.STRING &&
								sheet.getRow(i).getCell(0).getStringCellValue().toLowerCase(Locale.ROOT).contains("year")
							) {
								year = Integer.valueOf(sheet.getRow(i).getCell(0).getStringCellValue().replaceAll("[^0-9]", ""));
							}

						}
						else{
							if (sheet.getRow(i) != null &&
								sheet.getRow(i).getCell(0) != null &&
								sheet.getRow(i).getCell(0).getCellType() == CellType.STRING &&
								sheet.getRow(i).getCell(0).getStringCellValue().toLowerCase(Locale.ROOT).contains("daily")
							) {
								year = (int) sheet.getRow(i+1).getCell(6).getNumericCellValue();
								dateRowIdx += 1;
							}
						}

						if(year != 0) {
							Row dateRow = sheet.getRow(i+dateRowIdx);
							if(dateRow.getCell(0).getCellType() == CellType.STRING &&
								dateRow.getCell(0).getStringCellValue().toLowerCase(Locale.ROOT).startsWith("da")){
								for(int j = 1; j < 13; j++ ) {
									for(int k = 1; k < 32; k++ ) {
										DmhWaterlevel vo = new DmhWaterlevel();
										vo.setStation(station);
										vo.setYear(year);
										vo.setMonth(j);
										vo.setDay(k);
										vo.setUuid(uuid);
										Cell dataCell = sheet.getRow(i + k + dateRowIdx).getCell(j);
										if (dataCell.getCellType() == CellType.NUMERIC) {
											vo.setWaterlevel(dataCell.getNumericCellValue());
											//                                            log.info("{}", vo);

											try{
												LocalDate ld = LocalDate.of(year, j, k);
												dao.insertTnWaterlevel(vo);
											}
											catch (Exception e) {}
										}
									}
								}
							}
						}
					} else {
						if (sheet.getRow(i) != null &&
							sheet.getRow(i).getCell(0) != null &&
							sheet.getRow(i).getCell(0).getCellType() == CellType.STRING &&
							sheet.getRow(i).getCell(0).getStringCellValue().toLowerCase(Locale.ROOT).contains("year")
						) {
							year = Integer.valueOf(sheet.getRow(i).getCell(0).getStringCellValue().replaceAll("[^0-9]", ""));
						} else if ( /* 6 번 */
							sheet.getRow(i) != null &&
								sheet.getRow(i).getCell(0) != null &&
								sheet.getRow(i).getCell(0).getCellType() == CellType.STRING &&
								sheet.getRow(i).getCell(0).getStringCellValue().toLowerCase(Locale.ROOT).contains("daily")
						) {
							year = (int) sheet.getRow(i+1).getCell(6).getNumericCellValue();
							dateRowIdx += 1;
						} else if (
							sheet.getRow(i) != null &&
								sheet.getRow(i).getCell(0) != null &&
								sheet.getRow(i).getCell(1) != null &&
								sheet.getRow(i).getCell(1).getCellType() == CellType.STRING &&
								sheet.getRow(i).getCell(1).getStringCellValue().toLowerCase(Locale.ROOT).contains("year")
						) {
							year = Integer.valueOf(sheet.getRow(i).getCell(1).getStringCellValue().replaceAll("[^0-9]", ""));
						}
						if(year != 0) {
							Row dateRow = sheet.getRow(i+dateRowIdx);
							if(dateRow.getCell(0).getCellType() == CellType.STRING &&
								dateRow.getCell(0).getStringCellValue().toLowerCase(Locale.ROOT).startsWith("da")){
								for(int j = 1; j < 13; j++ ) {
									for(int k = 1; k < 32; k++ ) {
										DmhWaterlevel vo = new DmhWaterlevel();
										vo.setStation(station);
										vo.setYear(year);
										vo.setMonth(j);
										vo.setDay(k);
										vo.setUuid(uuid);
										Cell dataCell = sheet.getRow(i + k + dateRowIdx).getCell(j);
										if (dataCell.getCellType() == CellType.NUMERIC) {
											vo.setWaterlevel(dataCell.getNumericCellValue());
										}

										Cell dataCell2 = sheet.getRow(i + k + dateRowIdx).getCell(j+15);
										if (dataCell2.getCellType() == CellType.NUMERIC) {
											vo.setDischarge(dataCell2.getNumericCellValue());
										}

										if(vo.getDischarge() != null || vo.getWaterlevel() != null) {
											//                                            log.info("{}", vo);
											try{
												LocalDate ld = LocalDate.of(year, j, k);
												dao.insertTnWaterlevel(vo);
											}
											catch (Exception e) {}
										}
									}
								}
							}
						}
					}
					// if(row != null && row.getCell(0) != null) END
				} // ROWS END
			} // sheets END

			workbook.close();


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dmhRainfallFileParsing(File file, String uuid) {
		try(FileInputStream is = new FileInputStream(file)) {
			List<DmhRainfall> voList = new ArrayList<>();

			Workbook workbook = null;
			if (file.getPath().endsWith(".xls")) {
				workbook = new HSSFWorkbook(is);
			} else if (file.getPath().endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(is);
			}
			// 해당 인덱스에 있는 시트를 반환.
			int sheets = workbook.getNumberOfSheets();

			for(int s = 0; s < sheets; s++) {
				Sheet sheet = workbook.getSheetAt(s);
				String sheetNm = sheet.getSheetName();
				// 모든 행의 개수를 반환.
				int rows = sheet.getLastRowNum();

				String station = file.getName().replace(".xlsx", "").replace(".xls","").replaceAll("[0-9]+.|-|_|[0-9]|Y", "").trim();

				if(file.getName().startsWith("7.") || file.getName().startsWith("17.")) {
					log.info(file.getName());
					for(int i = 1; i <= rows; i++){
						Row row = sheet.getRow(i);
						try{
							LocalDate date = LocalDate.of(1900, 1, 1).plusDays((long)row.getCell(0).getNumericCellValue() - 2);
							DmhRainfall vo = new DmhRainfall();
							vo.setStation(station);
							vo.setYear(Integer.valueOf(date.getYear()));
							vo.setMonth(Integer.valueOf(date.getMonthValue()));
							vo.setDay(Integer.valueOf(date.getDayOfMonth()));
							vo.setUuid(uuid);
							if(row.getCell(1).getCellType() == CellType.STRING) {
								vo.setRainfall(Double.valueOf(row.getCell(1).getStringCellValue()));
							} else if(row.getCell(1).getCellType() == CellType.NUMERIC) {
								vo.setRainfall(row.getCell(1).getNumericCellValue());
							}
							if(vo.getRainfall() != null) {
								log.info("{}", vo);
								dao.insertTnRainfall(vo);
							}
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				else {
					for(int i = 0; i <= rows; i++){
						Row row = sheet.getRow(i);
						String yearStr = "";
						int year = 0;

						if(row != null && row.getCell(0) != null) {
							if(row.getCell(0).getCellType() == CellType.STRING) {
								if(isMerged(sheet, row.getCell(0))) {

									Row yearRow = sheet.getRow(i+1);
									yearStr = yearRow.getCell(0).getStringCellValue().toLowerCase(Locale.ROOT).split("year")[1].trim();
									year = Integer.valueOf(yearStr);

									int dateRowIdx = 3;
									Row dateRow = sheet.getRow(i+dateRowIdx);
									if(dateRow.getCell(0).getCellType() == CellType.STRING &&
										dateRow.getCell(0).getStringCellValue().startsWith("Day")){
										for(int j = 1; j < 13; j++ ) {
											for(int k = 1; k < 32; k++ ) {
												DmhRainfall vo = new DmhRainfall();
												vo.setStation(station);
												vo.setYear(year);
												vo.setMonth(j);
												vo.setDay(k);
												vo.setUuid(uuid);
												Cell dataCell = sheet.getRow(i + k + dateRowIdx).getCell(j);
												if (dataCell.getCellType() == CellType.NUMERIC) {
													vo.setRainfall(dataCell.getNumericCellValue());
													//                                                log.info("{}", vo);

													try{
														LocalDate ld = LocalDate.of(year, j, k);
														dao.insertTnRainfall(vo);
													}
													catch (Exception e) {}
												}
											}
										}
									}


									i += 40;
								}
								else {
									String _station = row.getCell(0).getStringCellValue();
									if(_station.startsWith("Station") || _station.contains("Station name")){

										int dateRowIdx = 4;
										if(sheet.getRow(i+2) != null && sheet.getRow(i+2).getCell(6).getCellType() == CellType.STRING){
											if(sheet.getRow(i+2).getCell(6).getStringCellValue().contains(":")) {
												year = Integer.valueOf(sheet.getRow(i+2).getCell(6).getStringCellValue().split(":")[1].trim());
											} else {
												yearStr = sheet.getRow(i+2).getCell(6).getStringCellValue().toLowerCase(Locale.ROOT).replace("year", "");
												year = Integer.valueOf(yearStr.trim());
											}
										}
										else if(sheet.getRow(i+6) != null && sheet.getRow(i+6).getCell(6).getCellType() == CellType.NUMERIC) {
											year = (int) sheet.getRow(i+6).getCell(6).getNumericCellValue();
											dateRowIdx = 8;
										}
										else if(sheet.getRow(i+7) != null && sheet.getRow(i+7).getCell(5).getCellType() == CellType.STRING) {
											year = Integer.valueOf(sheet.getRow(i+7).getCell(5).getStringCellValue().split("=")[1].trim());
											dateRowIdx = 9;
										}

										Row dateRow = sheet.getRow(i+dateRowIdx);
										if(dateRow.getCell(0).getCellType() == CellType.STRING &&
											dateRow.getCell(0).getStringCellValue().startsWith("Date")){
											for(int j = 1; j < 13; j++ ) {
												for(int k = 1; k < 32; k++ ) {
													DmhRainfall vo = new DmhRainfall();
													vo.setStation(station);
													vo.setYear(year);
													vo.setMonth(j);
													vo.setDay(k);
													vo.setUuid(uuid);
													Cell dataCell = sheet.getRow(i + k + dateRowIdx).getCell(j);
													if (dataCell.getCellType() == CellType.NUMERIC) {
														vo.setRainfall(dataCell.getNumericCellValue());
														//                                                    log.info("{}", vo);
														try{
															LocalDate ld = LocalDate.of(year, j, k);
															dao.insertTnRainfall(vo);
														}
														catch (Exception e) {}
													}
												}
											}
										}


									}
								}
							} else {
								continue;
							}
						} // if(row != null && row.getCell(0) != null) END
					} // ROWS END
				}
			} // sheets END

			workbook.close();


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dmhEvaporationFileParsing(File file, String uuid) {
		try(FileInputStream is = new FileInputStream(file)) {
			Workbook workbook = null;
			if (file.getPath().endsWith(".xls")) {
				workbook = new HSSFWorkbook(is);
			} else if (file.getPath().endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(is);
			}
			// 해당 인덱스에 있는 시트를 반환.
			int sheets = workbook.getNumberOfSheets();

			Map<String, DmhEvaporation> map = new LinkedHashMap<>();
			for(int s = 0; s < sheets; s++) {
				Sheet sheet = workbook.getSheetAt(s);
				String sheetNm = sheet.getSheetName();
				// 모든 행의 개수를 반환.
				int rows = sheet.getLastRowNum();

				for(int i = 0; i <= rows; i++){
					Row row = sheet.getRow(i);
					String station = file.getName().replace(".xlsx", "").replace(".xls","").replaceAll("[0-9]+.|-|_|[0-9]|Y", "").trim();
					String yearStr = "";
					int year = 0;
					if(row != null && row.getCell(0) != null) {
						if(row.getCell(0).getCellType() == CellType.STRING) {
							if(row.getCell(0).getStringCellValue().startsWith("Station")){

								Row yearRow = sheet.getRow(i+2);
								if(yearRow.getCell(6) != null && yearRow.getCell(6).getCellType() == CellType.STRING) {
									if(yearRow.getCell(6).getStringCellValue().toLowerCase(Locale.ROOT).startsWith("year")){
										yearStr = yearRow.getCell(6).getStringCellValue().replaceAll("[^0-9]", "");

										if("".equals(yearStr)) {
											year = (int) yearRow.getCell(7).getNumericCellValue();
										} else {
											year = Integer.valueOf(yearStr);
										}
									}
								}
								Row dateRow = sheet.getRow(i+4);
								if(dateRow.getCell(0).getCellType() == CellType.STRING &&
									dateRow.getCell(0).getStringCellValue().startsWith("Date")){
									for(int j = 1; j < 13; j++ ) {
										for(int k = 1; k < 32; k++ ) {
											String mapKey = String.format("%s %s/%s/%s", station, yearStr, j, k);
											DmhEvaporation vo = new DmhEvaporation();
											vo.setStation(station);
											vo.setYear(year);
											vo.setMonth(j);
											vo.setDay(k);
											vo.setUuid(uuid);
											map.putIfAbsent(mapKey, vo);
											Cell dataCell = sheet.getRow(i + k + 4).getCell(j);
											if (dataCell.getCellType() == CellType.NUMERIC) {
												DmhEvaporation vo1 = map.get(mapKey);
												Double value = dataCell.getNumericCellValue();
												if(sheetNm.startsWith("Tn")){
													vo1.setMeanMinTemp(value);
												}if(sheetNm.startsWith("Tx")){
													vo1.setMeanMaxTemp(value);
												}if(sheetNm.startsWith("Piche")){
													vo1.setObsVal(value);
												}
												map.put(mapKey, vo1);



											} else {
												continue;
											}
										}
									}
								}
							}
						}



					} // if(row != null && row.getCell(0) != null) END
				} // ROWS END
			} // sheets END

			workbook.close();

			for (String key : map.keySet()) {
				DmhEvaporation vo2 = map.get(key);
				if(vo2.getMeanMaxTemp() != null || vo2.getMeanMinTemp() != null || vo2.getObsVal() != null ) {
					log.info("{}", vo2);
					try{
						LocalDate ld = LocalDate.of(vo2.getYear(), vo2.getMonth(), vo2.getDay());
						dao.insertTnEvaporation(vo2);
					}
					catch (Exception e) {}
				}
			}
		} catch (Exception e) {
			//            log.info("{}",file.getPath());
		}
	}

	// 현재 셀이 병합된 셀인지 확인하는 함수
	private static boolean isMerged(Sheet sheet, Cell cell) {
		for (CellRangeAddress mergedRegion : sheet.getMergedRegions()) {
			if (mergedRegion.isInRange(cell.getRowIndex(), cell.getColumnIndex())) {
				return true;
			}
		}
		return false;
	}
}
