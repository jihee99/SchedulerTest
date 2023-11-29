package com.ex.laos.dam.service.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ex.laos.dam.service.APIService;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class APIServiceImpl implements APIService {

	private static final String JBT_FILE = "D:\\dev_etc\\laos\\dam\\depp_liwrms-403006-93e12407891a.json";
	private static final String OUTPUT_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	// @Value("${model.dam.file}")
	// private static String FILE_PAHT;


	@SneakyThrows
	@Override
	public String apiExcelFileDownload() {
		GoogleCredentials credentials;
		try {

			credentials = GoogleCredentials
				.fromStream(new FileInputStream(JBT_FILE))
				.createScoped(DriveScopes.DRIVE);
			credentials.refreshIfExpired();

			HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

			Drive service = new Drive.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), requestInitializer)
				.setApplicationName("Drive samples")
				.build();

			FileList fileList = service.files().list().execute();

			Optional<File> targetFile = fileList.getFiles().stream().filter(f -> f.getName().equals("NamNgum_Basin")).findFirst();

			if (targetFile.isPresent()) {
				try {
					Drive.Files.Export export = service.files().export(targetFile.get().getId(), OUTPUT_MIME_TYPE);

					String downloadFilePath = "D:\\dev_etc\\laos\\dam\\" + getCurrentDateTime() +".xlsx";

					try (FileOutputStream fos = new FileOutputStream(downloadFilePath)) {
						export.executeMediaAndDownloadTo(fos);
					}

					log.info("파일 다운로드 성공. 파일 경로: {}", downloadFilePath);
					return downloadFilePath; // 파일 다운로드 성공 시 파일 경로 반환

				} catch (IOException e) {
					log.error("파일 다운로드 중 오류 발생", e);
					throw new RuntimeException("파일 다운로드 중 오류 발생", e);
				}
			} else {
				log.warn("다운로드할 파일을 찾을 수 없습니다");
				throw new RuntimeException("파일을 찾을 수 없습니다");
			}
		} catch (IOException e) {
			log.error("Google 자격 증명 초기화 중 오류 발생", e);
			throw new RuntimeException("Google 자격 증명 초기화 중 오류 발생", e);
		}
	}

	// @SneakyThrows
	// @Override
	// public String apiExcelFileDownload() {
	// 	GoogleCredentials credentials = GoogleCredentials
	// 		.fromStream(new FileInputStream(JBT_FILE))
	// 		.createScoped(DriveScopes.DRIVE);
	//
	// 	credentials.refreshIfExpired();
	//
	// 	HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
	//
	// 	Drive service = new Drive.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), requestInitializer)
	// 		.setApplicationName("Drive samples")
	// 		.build();
	//
	// 	FileList fileList = service.files().list().execute();
	//
	//
	// 	fileList.getFiles().stream().filter(f -> f.getName().equals("NamNgum_Basin")).forEach(f -> {
	// 		try {
	// 			Drive.Files.Export export = service.files().export(f.getId(), OUTPUT_MIME_TYPE);
	//
	// 			String downloadFilePath = "D:\\dev_etc\\laos\\dam\\" + getCurrentDateTime() +".xlsx";
	//
	// 			try(FileOutputStream fos = new FileOutputStream(downloadFilePath)) {
	// 				export.executeMediaAndDownloadTo(fos);
	// 			}
	//
	// 		} catch(RuntimeException re) {
	// 			re.printStackTrace();
	// 		} catch(IOException e) {
	// 			throw new RuntimeException(e);
	// 		}
	// 	});
	//
	// }

	public static String getCurrentDateTime() {
		Date today = new Date();
		Locale currentLocale = new Locale("KOREAN", "KOREA");
		String pattern = "yyyyMMddHHmmss"; //hhmmss로 시간,분,초만 뽑기도 가능
		SimpleDateFormat formatter = new SimpleDateFormat(pattern,
			currentLocale);
		return formatter.format(today);
	}

}
