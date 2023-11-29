// package com.ex.laos.service.impl;
//
// import java.io.File;
// import java.io.FileInputStream;
// import java.io.FileNotFoundException;
// import java.io.IOException;
// import java.math.BigDecimal;
// import java.text.SimpleDateFormat;
// import java.time.LocalDate;
// import java.time.format.DateTimeFormatter;
// import java.util.ArrayList;
// import java.util.Date;
// import java.util.Iterator;
// import java.util.Map;
//
// import org.apache.poi.ss.formula.eval.NotImplementedFunctionException;
// import org.apache.poi.ss.usermodel.Cell;
// import org.apache.poi.ss.usermodel.Sheet;
// import org.apache.poi.ss.usermodel.DateUtil;
// import org.apache.poi.ss.usermodel.FormulaEvaluator;
// import org.apache.poi.ss.usermodel.Row;
//
// import org.apache.poi.ss.usermodel.Workbook;
// import org.apache.poi.ss.usermodel.WorkbookFactory;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
//
// import com.ex.laos.dam.dao.DamDao;
// import com.ex.laos.dam.dto.DamObservationDto;
// import com.ex.laos.dam.service.DamService;
//
// import org.dhatim.fastexcel.reader.*;
// // import org.dhatim.fastexcel.reader.Sheet;
// // import org.dhatim.fastexcel.reader.ReadableWorkbook;
//
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// @Service
// @RequiredArgsConstructor
// public class DamServiceImpl implements DamService {
//
// 	private final DamDao damDao;
//
// 	@Value("${model.dam.file}")
// 	private String filePath;
//
// 	@Override
// 	public void registerAllData(String selected) {
// 		String excelPath = filePath + "Download_by_GoogleDriveAPI.xlsx";
// 		try (FileInputStream fis = new FileInputStream(new File(excelPath));
// 			 Workbook workbook = WorkbookFactory.create(fis)
// 		) {
// 			// 시트 이름으로 시트 가져오기
// 			Sheet sheet = findSheetByName(workbook, selected);
//
// 			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
//
// 			ArrayList<DamObservationDto> damObservationDtoArrayList = new ArrayList<>();
//
// 			String damId = findDamId(selected);
// 			if (sheet != null) {
// 				System.out.println("Sheet Name: " + selected);
//
// 				// 첫 번째 행은 헤더 행으로 간주하고 건너뜀
// 				Iterator<Row> rowIterator = sheet.iterator();
// 				if (rowIterator.hasNext()) {
// 					rowIterator.next(); // 첫 번째 행 스킵
// 				}
//
// 				// 각 행별로 반복
// 				while (rowIterator.hasNext()) {
// 					Row row = rowIterator.next();
//
// 					DamObservationDto damObservationDto = new DamObservationDto();
// 					damObservationDto.setDamId(damId);
//
// 					int cellIndex = 0;
// 					// 각 셀별로 반복
// 					for (Cell cell : row) {
// 						// String cellValue = getCellValueAsString(cell, evaluator);
// 						processCell(damObservationDto, cell, evaluator, cellIndex);
// 						cellIndex++;
// 					}
// 					damObservationDtoArrayList.add(damObservationDto);
//
// 					if(row.getRowNum() % 1000 == 0) {
// 						damDao.insertDamObservationDtoList(damObservationDtoArrayList);
// 						damObservationDtoArrayList.clear();
// 					}
// 				}
// 				damDao.insertDamObservationDtoList(damObservationDtoArrayList);
//
// 			} else {
// 				System.out.println("Sheet not found: " + selected);
// 			}
//
// 		} catch (IOException e) {
// 			e.printStackTrace();
// 		}
// 	}
//
//
// 	@Override
// 	public void uploadPreviousData(String selected, int previousDays) {
// 		String excelPath = filePath + "Download_by_GoogleDriveAPI.xlsx";
// 		try (FileInputStream fis = new FileInputStream(new File(excelPath));
// 			 Workbook workbook = WorkbookFactory.create(fis)
// 		) {
// 			// 시트 이름으로 시트 가져오기
// 			Sheet sheet = findSheetByName(workbook, selected);
//
// 			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
//
// 			ArrayList<DamObservationDto> damObservationDtoArrayList = new ArrayList<>();
//
// 			String damId = findDamId(selected);
// 			if (sheet != null) {
// 				System.out.println("Sheet Name: " + selected);
//
// 				// 첫 번째 행은 헤더 행으로 간주하고 건너뜀
// 				Iterator<Row> rowIterator = sheet.iterator();
// 				if (rowIterator.hasNext()) {
// 					rowIterator.next(); // 첫 번째 행 스킵
// 				}
//
// 				// 오늘 날짜부터 5일 전까지의 데이터 대상
// 				LocalDate endDate = LocalDate.now();
// 				LocalDate startDate = endDate.minusDays(previousDays);
//
// 				// 각 행별로 반복
// 				while (rowIterator.hasNext()) {
// 					Row row = rowIterator.next();
//
// 					DamObservationDto damObservationDto = new DamObservationDto();
// 					damObservationDto.setDamId(damId);
//
// 					int cellIndex = 0;
// 					// 각 셀별로 반복
// 					for (Cell cell : row) {
// 						// String cellValue = getCellValueAsString(cell, evaluator);
// 						processCell(damObservationDto, cell, evaluator, cellIndex);
// 						cellIndex++;
// 					}
//
// 					LocalDate obsrvnYmd = LocalDate.parse(damObservationDto.getObsrvnYmd(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
// 					if (obsrvnYmd.isAfter(startDate) && obsrvnYmd.isBefore(endDate) || obsrvnYmd.isEqual(endDate)) {
// 						damObservationDtoArrayList.add(damObservationDto);
// 						System.out.println(damObservationDto);
// 						// upsertDamObservationData(damObservationDto);
// 					}
// 				}
// 				// damDao.upsertLastFiveDaysDamObservationDtoList(damObservationDtoArrayList);
//
// 			} else {
// 				System.out.println("Sheet not found: " + selected);
// 			}
//
// 		} catch (IOException e) {
// 			e.printStackTrace();
// 		}
// 	}
//
//
//
// 	private void processCell(DamObservationDto damObservationDto, Cell cell, FormulaEvaluator evaluator, int cellIndex) {
// 		String cellValue = getCellValueAsString(cell, evaluator);
// 		switch (cellIndex) {
// 			case 0:
// 				damObservationDto.setObsrvnYmd(cellValue);
// 				break;
// 			case 1:
// 				damObservationDto.setWl(handleNullCellValue(cellValue));
// 				break;
// 			case 2:
// 				damObservationDto.setVol(handleNullCellValue(cellValue));
// 				break;
// 			case 3:
// 				damObservationDto.setInflow(handleNullCellValue(cellValue));
// 				break;
// 			case 4:
// 				damObservationDto.setPg(handleNullCellValue(cellValue));
// 				break;
// 			case 5:
// 				damObservationDto.setFsp(handleNullCellValue(cellValue));
// 				break;
// 			case 6:
// 				damObservationDto.setFg(handleNullCellValue(cellValue));
// 				break;
// 			case 7:
// 				damObservationDto.setFto(handleNullCellValue(cellValue));
// 				break;
// 			case 8:
// 				damObservationDto.setTofl(handleNullCellValue(cellValue));
// 				break;
// 			case 9:
// 				damObservationDto.setTwl(handleNullCellValue(cellValue));
// 				break;
// 			case 10:
// 				damObservationDto.setRf(handleNullCellValue(cellValue));
// 				break;
// 		}
// 	}
//
//
//
// 	private static String getCellValueAsString(Cell cell, FormulaEvaluator evaluator) {
// 		switch (cell.getCellType()) {
// 			case STRING:
// 				return cell.getStringCellValue();
// 			case NUMERIC:
//
// 				if (DateUtil.isCellDateFormatted(cell)) {
// 					Date dateValue = cell.getDateCellValue();
// 					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
// 					return dateFormat.format(dateValue);
// 				} else {
// 					return Double.toString(cell.getNumericCellValue());
// 				}
// 			case BOOLEAN:
// 				return Boolean.toString(cell.getBooleanCellValue());
// 			case FORMULA:
// 				try {
// 					switch (cell.getCachedFormulaResultType()){
// 						case STRING:
// 							String formulaResult = cell.getStringCellValue();
// 							return formulaResult != null ? formulaResult : "0";
// 						case NUMERIC:
// 							Double doubleValue = cell.getNumericCellValue();
// 							return Double.toString(doubleValue);
//
// 					}
// 				} catch (NotImplementedFunctionException e) {
// 					return "NotImplementedFunctionException: " + e.getMessage();
// 				}
// 			default:
// 				return "";
// 		}
// 	}
//
//
// 	private static Sheet findSheetByName(Workbook workbook, String sheetName) {
// 		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
// 			Sheet sheet = workbook.getSheetAt(i);
//
// 			if (sheet.getSheetName().equals(sheetName)) {
// 				return sheet;
// 			}
// 		}
// 		return null;
// 	}
//
//
// 	// private static Sheet findSheetByName(ReadableWorkbook workbook, String sheetName) {
// 	// 	return workbook.getSheets()
// 	// 		.filter(sheet -> sheetName.equals(sheet.getName()))
// 	// 		.findFirst()
// 	// 		.orElse(null);
// 	// }
//
//
// 	private static BigDecimal handleNullCellValue(String cellValue) {
// 		if (cellValue != null && !cellValue.isEmpty()) {
// 			try {
// 				return new BigDecimal(cellValue);
// 			} catch (NumberFormatException e) {
// 				return BigDecimal.ZERO;
// 			}
// 		} else {
// 			return BigDecimal.ZERO;
// 		}
// 	}
//
//
// 	private String findDamId(String damName){
//
// 		String adjustedDamName = damName.replace("Nam", "Nam ");
// 		if(damName.equals("NamLik12")){
// 			adjustedDamName = "Nam Lik 1/2";
// 		}
//
// 		Map<String, String> result = damDao.getDamId(adjustedDamName);
//
// 		return result.get("dam_id");
// 	}
// }
