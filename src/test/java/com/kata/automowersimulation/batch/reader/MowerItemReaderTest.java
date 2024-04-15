package com.kata.automowersimulation.batch.reader;

import com.kata.automowersimulation.model.Mower;
import com.kata.automowersimulation.model.Orientation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * @author LAHRICHI Youssef
 */
@ExtendWith(MockitoExtension.class)
class MowerItemReaderTest {
    @Mock
    private Resource resource;

    private MowerItemReader mowerItemReader;

    @BeforeEach
    void setUp() throws IOException {
        String simulatedFileContent = "5 5\n1 2 N\nGAGAGAGAA\n3 3 E\nAADAADADDA";
        InputStream is = new ByteArrayInputStream(simulatedFileContent.getBytes(StandardCharsets.UTF_8));

        when(resource.getInputStream()).thenReturn(is);

        mowerItemReader = new MowerItemReader(resource);
        mowerItemReader.open(new ExecutionContext()); // Manually open the reader to initialize streams
    }

    @Test
    void shouldReadFirstMowerCorrectly() throws Exception {
        Mower firstMower = mowerItemReader.read(); // This should now correctly read the first mower setup.
        assertNotNull(firstMower, "First mower should not be null");
        assertEquals(1, firstMower.getPosition().getX(), "Expected X position does not match for the first mower");
        assertEquals(2, firstMower.getPosition().getY(), "Expected Y position does not match for the first mower");
        assertEquals(Orientation.N, firstMower.getPosition().getOrientation(), "Expected orientation does not match for the first mower");

        Mower secondMower = mowerItemReader.read(); // Read the second mower
        assertNotNull(secondMower, "Second mower should not be null");
        assertEquals(3, secondMower.getPosition().getX(), "Expected X position does not match for the second mower");
        assertEquals(3, secondMower.getPosition().getY(), "Expected Y position does not match for the second mower");
        assertEquals(Orientation.E, secondMower.getPosition().getOrientation(), "Expected orientation does not match for the second mower");

        mowerItemReader.close(); // Close the reader
    }

}
