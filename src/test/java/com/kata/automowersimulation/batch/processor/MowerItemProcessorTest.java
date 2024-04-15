package com.kata.automowersimulation.batch.processor;

import com.kata.automowersimulation.model.Lawn;
import com.kata.automowersimulation.model.Mower;
import com.kata.automowersimulation.model.Orientation;
import com.kata.automowersimulation.model.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author LAHRICHI Youssef
 */
class MowerItemProcessorTest {

    @Test
    public void shouldIncrementYWhenProcessingForwardInstruction() throws Exception {
        MowerItemProcessor processor = new MowerItemProcessor();
        Mower inputMower = new Mower(new Position(0, 0, Orientation.N), new Lawn(5, 5), "A");

        Mower processedMower = processor.process(inputMower);
        assertNotNull(processedMower);
        assertEquals(1, processedMower.getPosition().getY());
    }


}