package ru.openblocks.management;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Security;
import java.util.TimeZone;

@SpringBootApplication
public class ManagementApplication {

	public static void main(String[] args) {
		initializeApplication();
		SpringApplication.run(ManagementApplication.class, args);
	}

	private static void initializeApplication() {

		// Set default time zone (Europe/Moscow)
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));

		// Set default security provider - Bouncy Castle
		Security.addProvider(new BouncyCastleProvider());
	}

}
