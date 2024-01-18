package com.app.mowerproject.service;

import com.app.mowerproject.config.ApplicationConfig;
import com.app.mowerproject.model.Lawn;
import com.app.mowerproject.model.Mower;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.nio.file.Files;
import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author josephelbacha
 * MowerServe Test file cases
 */
@SuppressWarnings("deprecation")
class MowerServeTest {

    public static final String TEST_INPUT_TXT = "testInput.txt";
    public static final String EXTERNAL_FILES_INPUT_TXT = "/externalFiles/input.txt";
    public static final String NONEXISTENT_PATH_INPUT_FILE_TXT = "/nonexistent/path/inputFile.txt";
    public static final String INPUT_TEST_DATA = "5 5\n1 2 N\nLFLFLFLFF\n3 3 E\nFFRFFRFRRF\n";
    public static final String INPUT_WRONG_TEST_DATA = "-1 5\n1 2 N\nLFLFLFLFF\n3 3 E\nFFRFFRFRRF\n";
    public static final String INPUT_EXCEPTION_TEST_DATA = "5 5\n1 2 N\nLFBFLFLFF\n3 3 E\nFFRFFRFRRF\n";
    public static final String EXPECTED_OUTPUT = "\n1 3 N\n5 1 E";
    public static final String EXPECTED_FALSE_OUTPUT = "1 2 N\n5 1 E\n";

    @InjectMocks
    private MowerServe mowerServe;

    @Mock
    private ApplicationConfig appConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * mowItNow_success test the exact location of the file input, read it and test the output
     * @throws IOException If file not found or reading file exception
     */
    @Test
    void mowItNow_success() throws IOException {
        when(appConfig.getFilePath()).thenReturn(EXTERNAL_FILES_INPUT_TXT);

        InputStream inputStream = MowerServeTest.class.getResourceAsStream(appConfig.getFilePath());

        assertNotNull(inputStream);

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        Lawn lawn = mowerServe.readLawnDimensions(reader);

        StringBuilder result = mowerServe.mowItNow(reader, lawn);

        Assertions.assertThat(result).hasToString(EXPECTED_OUTPUT);
    }

    /**
     * mowItNow_fileNotFoundException test with error handling of wrong path file
     */
    @Test
    void mowItNow_fileNotFoundException() {
        when(appConfig.getFilePath()).thenReturn(NONEXISTENT_PATH_INPUT_FILE_TXT);

        InputStream inputStream = MowerServeTest.class.getResourceAsStream(appConfig.getFilePath());

        assertNull(inputStream);

        assertThrows(FileNotFoundException.class, () -> mowerServe.execute(inputStream));
    }

    /**
     * readLawnDimensions_lawnDimensionsException test the lawn dimensions width height exception
     * @throws IOException If file reading file exception
     */
    @Test
    void readLawnDimensions_lawnDimensionsException() throws IOException {
        File tempFile = createTempFile(TEST_INPUT_TXT, INPUT_WRONG_TEST_DATA);

        String tempFilePath = tempFile.getAbsolutePath();

        InputStream inputStream = new FileInputStream(tempFilePath);

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        assertThrows(InvalidParameterException.class, () -> mowerServe.readLawnDimensions(reader));
    }

    /**
     * execute_InValidInputFile_ShouldReturnExpectedResults create a new temp file append the data content,
     * test the content, test the output result with exception return
     * @throws IOException If file not found or reading file exception
     */
    @Test
    void execute_InValidInputFile_ShouldReturnExpectedResults() throws IOException {
        File tempFile = createTempFile(TEST_INPUT_TXT, INPUT_EXCEPTION_TEST_DATA);

        String tempFilePath = tempFile.getAbsolutePath();

        InputStream inputStream = new FileInputStream(tempFilePath);

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        Lawn lawn = mowerServe.readLawnDimensions(reader);

        assertThrows(InvalidObjectException.class, () -> mowerServe.mowItNow(reader, lawn));

        Files.delete(tempFile.toPath());
    }

    /**
     * execute_ValidInputFile_ShouldReturnExpectedResults create a temp file with valid data
     * and assert the result
     * @throws IOException If file not found or reading file exception
     */
    @Test
    void execute_ValidInputFile_ShouldReturnExpectedResults() throws IOException {
        File tempFile = createTempFile(TEST_INPUT_TXT, INPUT_TEST_DATA);

        String tempFilePath = tempFile.getAbsolutePath();

        InputStream inputStream = new FileInputStream(tempFilePath);

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        Lawn lawn = mowerServe.readLawnDimensions(reader);

        StringBuilder result = mowerServe.mowItNow(reader, lawn);

        Assertions.assertThat(result.toString()).isNotEqualTo(EXPECTED_FALSE_OUTPUT);

        Assertions.assertThat(result).hasToString(EXPECTED_OUTPUT);

        Files.delete(tempFile.toPath());
    }

    /**
     * execute_ValidInputFile_ShouldReturnExpectedResults create a temp file with valid data
     * and assert multiple result cases
     * @throws InvalidObjectException If the mower position is out of the lawn dimensions
     */
    @Test
    void execute_ValidData_TestProcessMove() throws InvalidObjectException {
        Lawn lawn = new Lawn(5,5);
        // Example 1
        String initialPosition = "0 0 N";
        String instructions = "FRFFRFLFF";
        Mower mower = mowerServe.processMower(lawn, initialPosition, instructions);
        Mower expectedMower = new Mower(4,0,'E');
        Assertions.assertThat(expectedMower).isNotNull().isEqualTo(mower);
        Mower expectedFalseMower = new Mower(1,1,'E');
        Assertions.assertThat(mower).isNotEqualTo(expectedFalseMower);

        // Example 2
        initialPosition = "2 2 N";
        instructions = "LFFRRFFLF";
        mower = mowerServe.processMower(lawn, initialPosition, instructions);
        expectedMower = new Mower(2,3,'N');
        Assertions.assertThat(expectedMower).isNotNull().isEqualTo(mower);

        // Example 3
        initialPosition = "1 1 N";
        instructions = "FFLFFRF";
        mower = mowerServe.processMower(lawn, initialPosition, instructions);
        expectedMower = new Mower(0,4,'N');
        Assertions.assertThat(expectedMower).isNotNull().isEqualTo(mower);

        // Example 4
        instructions = "FFLFFLF";
        mower = mowerServe.processMower(lawn, initialPosition, instructions);
        expectedMower = new Mower(0,2,'S');
        Assertions.assertThat(expectedMower).isNotNull().isEqualTo(mower);

        // Example 5
        instructions = "FFLFFLLF";
        mower = mowerServe.processMower(lawn, initialPosition, instructions);
        expectedMower = new Mower(1,3,'E');
        Assertions.assertThat(expectedMower).isNotNull().isEqualTo(mower);

        // Example 6
        initialPosition = "3 3 E";
        instructions = "FFLFFRFFLL";
        mower = mowerServe.processMower(lawn, initialPosition, instructions);
        expectedMower = new Mower(5,5,'W');
        Assertions.assertThat(expectedMower).isNotNull().isEqualTo(mower);

        // Example 7
        assertThrows(InvalidObjectException.class, () -> mowerServe.processMower(lawn, "6 6 E", "FFLFFRFFLL"));

        // Example 8
        assertThrows(InvalidObjectException.class, () -> mowerServe.processMower(lawn, "-1 3 E", "FFLFFRFFLL"));
    }

    /**
     * createTempFile create the temp file and return the file details
     * @param fileName file name for creation
     * @param content file content
     * @return File new temp file
     * @throws IOException If file not found or reading file exception
     */
    private File createTempFile(String fileName, String content) throws IOException {
        File tempFile = File.createTempFile(fileName, null);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(content);
        }
        return tempFile;
    }
}
