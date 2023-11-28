package com.ex.laos.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ex.laos.dao.DamDao;
import com.ex.laos.dao.TankDao;
import com.ex.laos.dto.tank.PredictionSummaryDto;
import com.ex.laos.service.TankService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TankServiceImpl implements TankService {

	private final DamDao damDao;
	private final TankDao tankDAO;

	@Value("${model.tank.dir}")
	private String dirPath;
	@Value("${model.tank.template}")
	private String templatePath;
	@Value("${model.tank.paramModel}")
	private String modelPath;


	/**
	 * 유역만 선택해 db에서 관측자료를 불러와 input 파일 생성하는 메서드
	 * @param floatingSelect 선택한 댐
	 * */
	@Override
	public PredictionSummaryDto generateTankInputFile2(String floatingSelect) throws
		InterruptedException {

		// 업로드된 파일 처리 로직 작성
		// 입력 파일 생성용 빌더
		StringBuilder stringBuilder = new StringBuilder();

		String damId = findDamId(adjustDamNameForDatabase(floatingSelect));

		// DB 입력값으로 바꾸기
		Map<String, BigDecimal> tankInputParameters = tankDAO.getTankInputParametersByDamId(damId);

		try {
			String tmplPath = templatePath + floatingSelect; // floatingSelect 명으로 input 파일 생성

			// 파일이 존재하지 않으면 기본 템플릿 내용 복사
			// Path path = Paths.get(tmplPath);
			Path baseTmplatePath = Paths.get(templatePath,"base",floatingSelect);
			String fileContents = Files.readString(baseTmplatePath, StandardCharsets.UTF_8);

			// 관측값이 중복 적용 됨
			// String fileContents;
			// Path path = Paths.get(tmplPath);
			//
			// if (Files.exists(path)) {
			// 	fileContents = Files.readString(path, StandardCharsets.UTF_8);
			// 	// stringBuilder.append(floatingSelect).append("\n")
			// 	// 				.append(floatingSelect).append(" watershed").append("\n")
			// 	// 				.append(String.format("%10s",tankInputParameters.get("evats_coeff")))
			// 	// 				.append(String.format("%10s",tankInputParameters.get("value_of_mask")))
			// 	// 				.append("   ").append(tankInputParameters.get("basin_area"));
			// }else{
			// 	Path baseTmplatePath = Paths.get(templatePath,"base",floatingSelect);
			// 	fileContents = Files.readString(baseTmplatePath, StandardCharsets.UTF_8);
			// }

			try (FileWriter writer = new FileWriter(tmplPath)) {
				// tankInputParameters
				for (Map.Entry<String, BigDecimal> entry : tankInputParameters.entrySet()) {
					String keyword = "$${"+ entry.getKey()  +"}";
					log.info("{}", entry.getValue().stripTrailingZeros().toPlainString());
					fileContents = fileContents.replace(keyword, formatStringWithPaddingBySelectedDamId(entry.getValue(), floatingSelect));
				}


				Path templateExcelPath = Paths.get(templatePath, "tank_data_230914.xlsx");

				// 엑셀 파일 읽기
				List<List<String>> excelData = readExcel(Files.newInputStream(templateExcelPath));

				// 시작일과 종료일 구하기
				String startDate = excelData.get(1).get(0);
				String endDate = excelData.get(excelData.size() - 1).get(0);

				fileContents = fileContents.replace("$${StartDate}", formatDate(startDate));
				fileContents = fileContents.replace("$${EndDate}", formatDate(endDate));

				stringBuilder.append(fileContents);

				for(int i=1; i<excelData.size(); i++){
					stringBuilder.append(formatData(String.valueOf(excelData.get(i)))).append("\n");
				}

				writer.write(stringBuilder.toString());
				System.out.println("File created successfully at: " + tmplPath);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 관측 데이터를 입력받아 input 파일 생성하는 메서드
	 * @param file 관측자료 (type : excel)
	 * @param floatingSelect 선택한 유역(damId)
	 * */
	@Override
	public PredictionSummaryDto generateTankInputFile(MultipartFile file, String floatingSelect) throws InterruptedException {
		return null;
	}

	@Override
	public PredictionSummaryDto readEstimateInflowModelResult(String fileName, String floatingSelect) {
		return null;
	}

	@Override
	public void downloadTemplateFileByDam(HttpServletResponse response, String name) {

	}

	@Override
	public String formatData(String input) {
		String[] parts = input.replace("[", "").replace("]", "").split(", ");
		return String.format("%10s%10s%9s%11s", parts[0], parts[1], parts[2], parts[3]);
	}


	/**
	 * 인풋 데이터에 넣을 관측 시작일, 관측 종료일 값을 date formate에 맞게 조정하는 메서드
	 * @param date 조정해야하는 날짜
	 * */
	public String formatDate(String date){
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date parsedDate = null;
		try {
			parsedDate = inputFormat.parse(date);
			return outputFormat.format(parsedDate);
		} catch (ParseException e) {
		   throw new RuntimeException(e);
		}
	}

	/**
	 * 엑셀 데이터를 읽어서 List타입으로 반환하는 메서드
	 * @param inputStream 엑셀 파일이 저장된 경로를 스트림으로 전달
	 * */
	private List<List<String>> readExcel(InputStream inputStream) throws IOException {
		List<List<String>> excelData = new ArrayList<>();

		// ReadableWorkbook workbook = new ReadableWorkbook(inputStream);
		// Sheet sheet = workbook.getFirstSheet();
		try (ReadableWorkbook workbook = new ReadableWorkbook(inputStream)) {
			Sheet sheet = workbook.getFirstSheet();
			try (Stream<Row> rowStream = sheet.openStream()){
				rowStream.forEach(row -> {
					List<String> rowData = new ArrayList<>();
					for (Cell cell : row) {
						rowData.add(cell.asString());
					}
					System.out.println(rowData);
					excelData.add(rowData);
				});
			}
		}
		return excelData;
	}


	private String capitalize(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}
		System.out.println(Character.toUpperCase(str.charAt(0)) + str.substring(1));
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}

	/**
	 * 댐 유역 명을 데이터 베이스에 저장된 형식에 맞게 조정하는 메서드
	 * @param selected 선택된 유역명
	 * */
	private String adjustDamNameForDatabase(String selected){
		String adjustedDamName = selected.replace("Nam", "Nam ");
		if(selected.equals("NamLik12")){
			adjustedDamName = "Nam Lik 1/2";
		}
		return  adjustedDamName;
	}

	/**
	 * 유역명에 맞춰 댐 아이디를 조회하는 메서드
	 * @param adjustedDamName 조회할 댐 유역 명 (adjustDamNameForDatabase 가 반드시 선행되어야 함)
	 * */
	private String findDamId(String adjustedDamName){

		Map<String, String> result = damDao.getDamId(adjustedDamName);

		return result.get("dam_id");
	}

	/**
	 * input file 생성시 유역별로 기본 템플릿 공백을 조정하는 메서드
	 * @param value 파일에 들어가야하는 파라미터 값
	 * @param selected 선택된 유역
	 * */
	private String formatStringWithPaddingBySelectedDamId(BigDecimal value, String selected) {
		int defaultWidth = 20; // 기본 폭
		int adjustedWidth;
		switch (selected) {
			case "NamSana":
				adjustedWidth = 18;
				break;
			case "NamNgum3":
			case "NamMang3":
			case "NamSong":
				adjustedWidth = 20;
				break;
			case "NamLik12":
			case "NamLeuk":
			case "NamNgum4":
				adjustedWidth = 21;
				break;
			case "NamNgum1":
				adjustedWidth = 22;
				break;
			case "NamNgum2":
			case "NamNgum5":
			case "NamPhay":
			case "NamLik1":
				adjustedWidth = 23;
				break;
			default:
				adjustedWidth = defaultWidth;
		}
		String formattedValue = value.stripTrailingZeros().toPlainString();
		return String.format("%-" + adjustedWidth + "s", formattedValue);
	}


}
