package com.TruckBooking.Security;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

	@Value("${transporter.firebase.credentials.file}")
	private String transporterCredentialsFile;

	@Value("${transporter.firebase.app.name}")
	private String transporterAppName;

	@Value("${shipper.firebase.credentials.file}")
	private String shipperCredentialsFile;

	@Value("${shipper.firebase.app.name}")
	private String shipperAppName;

	public static void main(String[] args) {
		SpringApplication.run(FirebaseConfig.class, args);
	}

	@PostConstruct
	private void initializeFirebaseApps() {
		initializeFirebaseApp(transporterCredentialsFile, transporterAppName);
		initializeFirebaseApp(shipperCredentialsFile, shipperAppName);
	}

	private void initializeFirebaseApp(String credentialsFile, String appName) {
		try (InputStream serviceAccount = FirebaseConfig.class.getClassLoader().getResourceAsStream(credentialsFile)) {
			assert serviceAccount != null;
			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.build();

			FirebaseApp.initializeApp(options, appName); // Provide a unique app name
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}



