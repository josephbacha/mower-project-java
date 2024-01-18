package com.app.mowerproject.service;

import com.app.mowerproject.model.Lawn;
import com.app.mowerproject.model.Mower;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.Optional;

/**
 * @author josephelbacha
 * MowerServe service logic to move the Mower through instructions
 * and print the final position
 */

@Service
public class MowerServe {

    private static final Logger log = LoggerFactory.getLogger(MowerServe.class);
    private static final String EMPTY_SPACE = " ";

    /**
     * execute program execution function that read the input file data,
     * apply the logic and print the end results.
     * @throws IOException If file not found or reading file exception
     */
    public void execute(InputStream inputStream) throws IOException {
        log.info("Execute Program");

        if(Optional.ofNullable(inputStream).isEmpty()) throw new FileNotFoundException("InputStream is null");

        log.info("Input file located and read properly");

        // Process reading lawn's dimensions
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        // read Lawn dimensions
        Lawn lawn = readLawnDimensions(reader);

        StringBuilder result = mowItNow(reader, lawn);

        if(Optional.ofNullable(result).isPresent() && result.length() > 0)  log.info("\nResult: {}", result);
    }

    /**
     *  mowItNow program logic and build the result output
     * @param reader BufferedReader to read the input file
     * @param lawn Lawn having the dimensions
     * @return StringBuilder return the final result
     * @throws IOException If reading file exception
     */
    public StringBuilder mowItNow(BufferedReader reader, Lawn lawn) throws IOException {

        String line;
        int mowerCount = 0;
        StringBuilder result = new StringBuilder();

        // Process each mower's instructions
        while ((line = reader.readLine()) != null) {
            mowerCount++;
            String instructions = reader.readLine();
            log.debug("Mower {}, InitialPosition={}, Instructions={}",
                    mowerCount, line, instructions);

            // Process the mower move and append its final position to StringBuilder for print
            Mower mower = processMower(lawn, line, instructions);
            result.append("\n").append(mower.getX()).append(EMPTY_SPACE)
                    .append(mower.getY()).append(EMPTY_SPACE).append(mower.getOrientation());
        }
        return result;
    }

    /**
     * readLawnDimensions read the lawn dimensions from the first file line
     * @param reader read the first file line
     * @return Lawn return the lawn dimensions
     * @throws IOException If reading file exception
     */
    public Lawn readLawnDimensions(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        String[] lawnSize = line.split(EMPTY_SPACE);
        Lawn lawn = new Lawn(Integer.parseInt(lawnSize[0]), Integer.parseInt(lawnSize[1]));
        if(lawn.getHeight() < 0 || lawn.getWidth() < 0) {
            log.error("Lawn Dimensions are wrong");
            throw new InvalidParameterException("WRONG LAWN DIMENSIONS");
        }
        log.debug("Lawn: Width:{} Height:{}", lawn.getWidth(), lawn.getHeight());
        return lawn;
    }

    /**
     * processMower process the instructions for a mower on the given lawn.
     * @param lawn pass the lawn dimensions
     * @param initialPosition initial mower postion
     * @param instructions mower move instructions
     * @return Mower new mower result
     */
    public Mower processMower(Lawn lawn, String initialPosition, String instructions) throws InvalidObjectException {
        Mower mower = new Mower();
        mower.setOrientation(initialPosition.charAt(4));

        String[] positionParts = initialPosition.split(EMPTY_SPACE);
        mower.setX(Integer.parseInt(positionParts[0]));
        mower.setY(Integer.parseInt(positionParts[1]));

        if (mower.getX() < 0 || mower.getX() > lawn.getWidth()
                || mower.getY() < 0 || mower.getY() > lawn.getHeight()) {
            log.error("Mower position is outside the lawn");
            throw new InvalidObjectException("WRONG MOWER INIT POSITION");
        }

        return processInstructions(mower, instructions, lawn);
    }

    /**
     * processInstructions process each move instruction for a mower.
     * @param mower coordinates
     * @param instructions to move the mower accordingly
     * @param lawn dimensions
     * @return Mower the new mover position
     */
    public Mower processInstructions(Mower mower, String instructions, Lawn lawn) throws InvalidObjectException {
        for (char instruction : instructions.toCharArray()) {
            mower.move(instruction);

            // Check if the new position is within the lawn
            if (mower.getX() < 0 || mower.getX() > lawn.getWidth()
                    || mower.getY() < 0 || mower.getY() > lawn.getHeight()) {
                // If outside the lawn, revert the move and break the loop
                mower.moveBackward();
            }
        }
        return mower;
    }
}
