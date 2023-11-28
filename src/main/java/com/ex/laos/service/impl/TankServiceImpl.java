package com.ex.laos.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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
			// String fileContents = Files.readString(path, StandardCharsets.UTF_8);

			String fileContents;
			Path path = Paths.get(tmplPath);

			if (Files.exists(path)) {
				fileContents = Files.readString(path, StandardCharsets.UTF_8);
				// stringBuilder.append(floatingSelect).append("\n")
				// 				.append(floatingSelect).append(" watershed").append("\n")
				// 				.append(String.format("%10s",tankInputParameters.get("evats_coeff")))
				// 				.append(String.format("%10s",tankInputParameters.get("value_of_mask")))
				// 				.append("   ").append(tankInputParameters.get("basin_area"));
			}else{
				Path baseTmplatePath = Paths.get(templatePath,"base",floatingSelect);
				fileContents = Files.readString(baseTmplatePath, StandardCharsets.UTF_8);
			}

			try (FileWriter writer = new FileWriter(tmplPath)) {
				// tankInputParameters
				for (Map.Entry<String, BigDecimal> entry : tankInputParameters.entrySet()) {
					String keyword = "$${"+ entry.getKey()  +"}";
					log.info("{}", entry.getValue().stripTrailingZeros().toPlainString());
					fileContents = fileContents.replace(keyword, formatStringWithPaddingBySelectedDamId(entry.getValue(), floatingSelect));
				}

				stringBuilder.append(fileContents);
				writer.write(stringBuilder.toString());
				System.out.println("File created successfully at: " + tmplPath);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

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

	private String capitalize(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}
		System.out.println(Character.toUpperCase(str.charAt(0)) + str.substring(1));
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
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
