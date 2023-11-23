package com.ex.laos.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
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

	@Override
	public void registerAllData(String selected) {
		String excelPath = filePath + "Download_by_GoogleDriveAPI.xlsx";
		try (ReadableWorkbook workbook = new ReadableWorkbook(new FileInputStream(new File(excelPath)))) {
			// Get the sheet by name
			Sheet sheet = findSheetByName(workbook, selected);


			ArrayList<DamObservationDto> damObservationDtoArrayList = new ArrayList<>();

			String damId = findDamId(selected);
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

	@Override
	public void uploadPreviousData(String selected, int previousDays) {

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

	private String findDamId(String damName){

		String adjustedDamName = damName.replace("Nam", "Nam ");
		if(damName.equals("NamLik12")){
			adjustedDamName = "Nam Lik 1/2";
		}

		Map<String, String> result = damDao.getDamId(adjustedDamName);

		return result.get("dam_id");
	}
}
