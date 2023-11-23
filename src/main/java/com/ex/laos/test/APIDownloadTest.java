package com.ex.laos.test;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;

public class APIDownloadTest {

	// private static final String DEPP_FILE = "D:\\dev\\laos-test\\depp_liwrms-403006-93e12407891a.json";
	private static final String JBT_FILE = "D:\\dev_etc\\laos\\dam\\depp_liwrms-403006-93e12407891a.json";
	// private static final String JBT_FILE = "D:\\dev\\laos-test\\enjoybt_liwrms-403105-ed37fd4f9d74.json";
	private static final String OUTPUT_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	@Value("${model.dam.file}")
	private static String filePath;

	public static void main(String[] args) throws Exception {

		GoogleCredentials credentials = GoogleCredentials
			.fromStream(new FileInputStream(JBT_FILE))
			.createScoped(DriveScopes.DRIVE);

		credentials.refreshIfExpired();

		HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

		Drive service = new Drive.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), requestInitializer)
			.setApplicationName("Drive samples")
			.build();

		FileList fileList = service.files().list().execute();


		fileList.getFiles().stream().filter(f -> f.getName().equals("NamNgum_Basin")).forEach(f -> {
			try {
				Drive.Files.Export export = service.files().export(f.getId(), OUTPUT_MIME_TYPE);

				// String downloadFilePath = filePath + getCurrentDateTime() + ".xlsx";
				String downloadFilePath = "D:\\dev_etc\\laos\\dam\\" + getCurrentDateTime() +".xlsx";

				try(FileOutputStream fos = new FileOutputStream(downloadFilePath)) {
					export.executeMediaAndDownloadTo(fos);
				}

			} catch(RuntimeException re) {
				re.printStackTrace();
			} catch(IOException e) {
				throw new RuntimeException(e);
			}
		});

	}

	public static String getCurrentDateTime() {
		Date today = new Date();
		Locale currentLocale = new Locale("KOREAN", "KOREA");
		String pattern = "yyyyMMddHHmmss"; //hhmmss로 시간,분,초만 뽑기도 가능
		SimpleDateFormat formatter = new SimpleDateFormat(pattern,
			currentLocale);
		return formatter.format(today);
	}

}
