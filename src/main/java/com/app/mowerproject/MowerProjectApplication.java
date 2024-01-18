package com.app.mowerproject;

import com.app.mowerproject.config.ApplicationConfig;
import com.app.mowerproject.service.MowerServe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@SpringBootApplication
public class MowerProjectApplication {

    @Autowired
    private ApplicationConfig appConfig;

    public static void main(String[] args) {
        SpringApplication.run(MowerProjectApplication.class, args);
    }

    @Component
    public class MyStartupRunner implements CommandLineRunner {

        private final MowerServe mowerServe;

        public MyStartupRunner(MowerServe mowerServe) {
            this.mowerServe = mowerServe;
        }

        @Override
        public void run(String... args) throws Exception {
            InputStream inputStream = MowerServe.class.getResourceAsStream(appConfig.getFilePath());
            mowerServe.execute(inputStream);
        }
    }
}
