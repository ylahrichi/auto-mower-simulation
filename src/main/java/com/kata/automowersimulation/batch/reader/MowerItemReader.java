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
    public Mower read() throws Exception, UnexpectedInputException {
        String mowerLine = reader.readLine();
        if (mowerLine == null) {
            return null;
        }

        String[] mowerDetails = mowerLine.split(" ");
        int x = Integer.parseInt(mowerDetails[0]);
        int y = Integer.parseInt(mowerDetails[1]);
        Orientation orientation = Orientation.valueOf(mowerDetails[2]);

        String instructionsLine = reader.readLine();
        if (instructionsLine == null) {
            throw new UnexpectedInputException("Instructions expected for mower at line");
        }

        Position position = new Position(x, y, orientation);
        return new Mower(position, lawn, instructionsLine);
    }


    private void initializeReader() throws IOException {
        reader = new BufferedReader(new InputStreamReader(inputFile.getInputStream()));
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        try {
            reader = new BufferedReader(new InputStreamReader(inputFile.getInputStream()));
            if (executionContext.containsKey("lineNumber")) {
                this.lineNumber = executionContext.getInt("lineNumber");
                for (int i = 0; i < lineNumber; i++) {
                    reader.readLine();
                }
            } else {
                initializeLawn();
            }
        } catch (IOException e) {
            throw new ItemStreamException("Failed to initialize reader", e);
        }
    }

    private void initializeLawn() throws IOException {
        String line = reader.readLine();
        if (line != null) {
            String[] lawnDimensions = line.split(" ");
            int width = Integer.parseInt(lawnDimensions[0]);
            int height = Integer.parseInt(lawnDimensions[1]);
            lawn = new Lawn(width, height);
        } else {
            throw new UnexpectedInputException("Lawn dimensions are expected at the first line.");
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
