package com.kata.automowersimulation.batch.reader;

import com.kata.automowersimulation.model.Lawn;
import com.kata.automowersimulation.model.Mower;
import com.kata.automowersimulation.model.Orientation;
import com.kata.automowersimulation.model.Position;
import lombok.Setter;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author LAHRICHI Youssef
 */

@Component
@Setter
public class MowerItemReader implements ItemReader<Mower>, ItemStream {
    private BufferedReader reader;
    private Lawn lawn;
    private int lineNumber = 0;
    private final Resource inputFile;

    public MowerItemReader(@Value("${app.file-path-input}") Resource inputFile) {
        this.inputFile = inputFile;
    }

    @Override
    public Mower read() throws Exception {
        if (reader == null) {
            open(new ExecutionContext());
        }

        if (lawn == null) {
            String line = reader.readLine();
            lineNumber++;
            if (line != null) {
                String[] lawnDimensions = line.split(" ");
                int width = Integer.parseInt(lawnDimensions[0]);
                int height = Integer.parseInt(lawnDimensions[1]);
                lawn = new Lawn(width, height);
            }
        }

        String mowerLine = reader.readLine();
        if (mowerLine == null) {
            return null; // EOF
        }
        lineNumber++;

        String[] mowerDetails = mowerLine.split(" ");
        int x = Integer.parseInt(mowerDetails[0]);
        int y = Integer.parseInt(mowerDetails[1]);
        Orientation orientation = Orientation.valueOf(mowerDetails[2]);

        String instructionsLine = reader.readLine();
        if (instructionsLine == null) {
            throw new UnexpectedInputException("Instructions expected for mower at line " + lineNumber);
        }
        lineNumber++;

        Position position = new Position(x, y, orientation);
        return new Mower(position, lawn, instructionsLine);
    }


    private void initializeReader() throws IOException {
        reader = new BufferedReader(new InputStreamReader(inputFile.getInputStream()));
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        try {
            if (executionContext.containsKey("lineNumber")) {
                this.lineNumber = executionContext.getInt("lineNumber");
            }
            initializeReader();
            // Skip lines until you reach the last read line
            for (int i = 0; i < lineNumber; i++) {
                reader.readLine();
            }
        } catch (IOException e) {
            throw new ItemStreamException("Failed to initialize reader", e);
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        executionContext.putInt("lineNumber", lineNumber);
    }

    @Override
    public void close() throws ItemStreamException {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                throw new ItemStreamException("Error closing reader", e);
            }
        }
    }
}
