package com.ex.laos.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;

import org.dhatim.fastexcel.reader.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ex.laos.dao.DamDao;
import com.ex.laos.service.DamService;
import com.ex.laos.dto.dam.DamObservationDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DamServiceImpl2 implements DamService {

	private final DamDao damDao;

	@Value("${model.dam.file}")
	private String filePath;

	/**
	 * 	2022년 이전 관측 데이터를 업로드 하는 메서드
	 *
	 * 	@param selected  업로드 하고자 하는 댐 명칭 ( 시트 명으로 사용 )
	 */
	@Override
	public void uploadOldDataToDatabase(String selected) {

		String excelPath = filePath + "NamMang3.xlsx";
		try (
			ReadableWorkbook workbook = new ReadableWorkbook(new FileInputStream(new File(excelPath)))
		) {
			Sheet sheet = workbook.getFirstSheet();
			ArrayList<DamObservationDto> damObservationDtoArrayList = new ArrayList<>();
			String damId = findDamId(adjustDamNameForDatabase(selected));
			if (sheet != null) {

				// 첫 번째 행 제외 후 각 행 별로 반복
				try (Stream<Row> rowStream = sheet.openStream().skip(1)) {
					rowStream.forEach(row -> {
						DamObservationDto damObservationDto = new DamObservationDto();
						damObservationDto.setDamId(damId);

						int cellIndex = 0;
						// 각 셀별로 반복
						for (Cell cell : row) {

							if (cellIndex == 0) {
								cellIndex++;
								continue;
							}
							processCell(damObservationDto, cell, cellIndex-1);
							cellIndex++;
						}
						damObservationDto.setRf(BigDecimal.ZERO);

						damObservationDtoArrayList.add(damObservationDto);
						if (row.getRowNum() % 1000 == 0) {
							damDao.insertDamObservationDtoList(damObservationDtoArrayList);
							damObservationDtoArrayList.clear();
						}
					});
					if(!damObservationDtoArrayList.isEmpty() ){
						damDao.insertDamObservationDtoList(damObservationDtoArrayList);
					}
				}

			}

		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}


	/**
	 * 	전체 관측 데이터를 업로드 하는 메서드
	 *
	 * 	@param selected  업로드 하고자 하는 댐 명칭 ( 시트 명으로 사용 )
	 */
	@Override
	public void registerAllData(String selected) {
		String excelPath = filePath + "Download_by_GoogleDriveAPI.xlsx";
		try (ReadableWorkbook workbook = new ReadableWorkbook(new FileInputStream(new File(excelPath)))) {
			// Get the sheet by name
			Sheet sheet = findSheetByName(workbook, selected);

			ArrayList<DamObservationDto> damObservationDtoArrayList = new ArrayList<>();

			String damId = findDamId(adjustDamNameForDatabase(selected));
			if (sheet != null) {
				System.out.println("Sheet Name: " + selected);
				// 각 행별로 반복
				// 단 첫 번째 행 제외
				try (Stream<Row> rowStream = sheet.openStream().skip(1)) {
					rowStream.forEach(row -> {
						DamObservationDto damObservationDto = new DamObservationDto();
						damObservationDto.setDamId(damId);

						int cellIndex = 0;
						// 각 셀별로 반복
						for (Cell cell : row) {
							processCell(damObservationDto, cell, cellIndex);
							cellIndex++;
						}
						damObservationDtoArrayList.add(damObservationDto);
						if (row.getRowNum() % 1000 == 0) {
							damDao.insertDamObservationDtoList(damObservationDtoArrayList);
							damObservationDtoArrayList.clear();
						}
					});

					if(!damObservationDtoArrayList.isEmpty() ){
						damDao.insertDamObservationDtoList(damObservationDtoArrayList);
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Sheet not found: " + selected);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 다운로드 한 API 파일에서 선택된 댐 명칭 해당하는 이전 데이터를 데이터베이스에 입력하는 메서드
	 *
	 * @param filePath     API를 요청해 다운로드한 파일의 경로
	 * @param selected     선택된 댐 명칭
	 * @param previousDays 선택된 기준일로부터의 이전 날짜 범위 (일 수)
	 */
	@Override
	public void uploadPreviousData(String filePath, String selected, int previousDays) {
		// String excelPath = filePath + "Download_by_GoogleDriveAPI.xlsx";
		try (ReadableWorkbook workbook = new ReadableWorkbook(new FileInputStream(new File(filePath)))) {

			Sheet sheet = findSheetByName(workbook, selected);

			ArrayList<DamObservationDto> damObservationDtoArrayList = new ArrayList<>();

			String damId = findDamId(adjustDamNameForDatabase(selected));
			if (sheet != null) {
				System.out.println("Sheet Name: " + selected);

				// 각 행별로 반복
				// 단 첫 번째 행 제외
				try (Stream<Row> rowStream = sheet.openStream().skip(1)) {
					// 오늘 날짜부터 5일 전까지의(가변) 데이터 대상
					LocalDate endDate = LocalDate.now();
					// LocalDate endDate = endDate2.minusDays(previousDays);
					LocalDate startDate = endDate.minusDays(previousDays);

					rowStream.forEach(row -> {
						DamObservationDto damObservationDto = new DamObservationDto();
						damObservationDto.setDamId(damId);

						int cellIndex = 0;
						// 각 셀별로 반복
						for (Cell cell : row) {
							processCell(damObservationDto, cell, cellIndex);
							cellIndex++;
						}

						LocalDate obsrvnYmd = LocalDate.parse(damObservationDto.getObsrvnYmd(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
						if (obsrvnYmd.isAfter(startDate) && obsrvnYmd.isBefore(endDate) || obsrvnYmd.isEqual(endDate)) {
							damObservationDtoArrayList.add(damObservationDto);
							System.out.println(damObservationDto);
						}

					});
					damDao.upsertLastFiveDaysDamObservationDtoList(damObservationDtoArrayList);

				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Sheet not found: " + selected);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void processCell(DamObservationDto damObservationDto, Cell cell, int cellIndex) {
		String cellValue = getCellValueAsString(cell);
		switch (cellIndex) {
			case 0:
				damObservationDto.setObsrvnYmd(cellValue);
				break;
			case 1:
				damObservationDto.setWl(handleNullCellValue(cellValue));
				break;
			case 2:
				damObservationDto.setVol(handleNullCellValue(cellValue));
				break;
			case 3:
				damObservationDto.setInflow(handleNullCellValue(cellValue));
				break;
			case 4:
				damObservationDto.setPg(handleNullCellValue(cellValue));
				break;
			case 5:
				damObservationDto.setFsp(handleNullCellValue(cellValue));
				break;
			case 6:
				damObservationDto.setFg(handleNullCellValue(cellValue));
				break;
			case 7:
				damObservationDto.setFto(handleNullCellValue(cellValue));
				break;
			case 8:
				damObservationDto.setTofl(handleNullCellValue(cellValue));
				break;
			case 9:
				damObservationDto.setTwl(handleNullCellValue(cellValue));
				break;
			case 10:
				damObservationDto.setRf(handleNullCellValue(cellValue));
				break;
		}
	}

	private static String getCellValueAsString(Cell cell) {
		switch (cell.getType()) {
			case STRING:
				return cell.asString();
			case NUMBER:
				if (cell.asDate() != null) {
					LocalDateTime dateValue = cell.asDate();
					DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					return dateFormat.format(dateValue);
				} else {
					BigDecimal numericValue = cell.asNumber();
					DecimalFormat decimalFormat = new DecimalFormat("#.##");
					return decimalFormat.format(numericValue);
				}
			case BOOLEAN:
				return cell.asBoolean().toString();
			case FORMULA:
				try {
					Object cellValue = cell.getValue();
					if (cellValue instanceof Cell) {
						Cell formulaCell = (Cell) cellValue;
						switch (formulaCell.getType()) {
							case STRING:
								String stringResult = formulaCell.asString();
								return stringResult != null ? stringResult : "0";
							case NUMBER:
								BigDecimal numericValue = formulaCell.asNumber();
								return numericValue.toString();
							default:
								return "0";
						}
					} else if (cellValue instanceof BigDecimal) {
						BigDecimal numericValue = (BigDecimal) cellValue;
						DecimalFormat decimalFormat = new DecimalFormat("#.##");
						return decimalFormat.format(numericValue);
					} else {
						return "0";
					}
				} catch (Exception e) {
					return "Exception: " + e.getMessage();
				}
			default:
				return "";
		}
	}




	private static Sheet findSheetByName(ReadableWorkbook workbook, String sheetName) {
		return workbook.getSheets()
			.filter(sheet -> sheet.getName().equals(sheetName))
			.findFirst()
			.orElse(null);
	}

	private BigDecimal handleNullCellValue(String cellValue) {
		if (cellValue != null && !cellValue.isEmpty()) {
			try {
				return new BigDecimal(cellValue);
			} catch (NumberFormatException e) {
				return BigDecimal.ZERO;
			}
		} else {
			return BigDecimal.ZERO;
		}
	}

	private String adjustDamNameForDatabase(String selected){
		String adjustedDamName = selected.replace("Nam", "Nam ");
		if(selected.equals("NamLik12")){
			adjustedDamName = "Nam Lik 1/2";
		}
		return  adjustedDamName;
	}
	private String findDamId(String adjustedDamName){

		Map<String, String> result = damDao.getDamId(adjustedDamName);

		return result.get("dam_id");
	}
}
