package com.app.mowerproject;

import com.app.mowerproject.config.ApplicationConfig;
import com.app.mowerproject.service.MowerServe;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
class MowerProjectApplicationTests {

    @InjectMocks
    private MowerServe mowerServe;

    @Mock
    private ApplicationConfig appConfig;

    public static final String EXTERNAL_FILES_INPUT_TXT = "/externalFiles/input.txt";

    @Test
    void contextLoads() throws IOException {
        when(appConfig.getFilePath()).thenReturn(EXTERNAL_FILES_INPUT_TXT);

        InputStream inputStream = MowerProjectApplicationTests.class.getResourceAsStream(appConfig.getFilePath());

        assertNotNull(inputStream);

        mowerServe.execute(inputStream);
    }
}
