package com.kata.automowersimulation.batch.writer;

import com.kata.automowersimulation.model.Lawn;
import com.kata.automowersimulation.model.Mower;
import com.kata.automowersimulation.model.Orientation;
import com.kata.automowersimulation.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.batch.item.Chunk;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author LAHRICHI Youssef
 */
@ExtendWith(MockitoExtension.class)
class MowerItemWriterTest {
    private MowerItemWriter writer;
    @Mock
    private Logger loggerMock;
    @Mock
    private Chunk<Mower> chunkMock;

    @BeforeEach
    void setUp() {
        writer = new MowerItemWriter();
        writer.setLogger(loggerMock);
    }

    @Test
    void shouldLogMowerFinalPositionWhenWritten() throws Exception {
        // Given
        Position position = new Position(2, 3, Orientation.N);
        Lawn lawn = new Lawn(5, 5);
        Mower mower = new Mower(position, lawn);
        when(chunkMock.getItems()).thenReturn(List.of(mower));

        // When
        writer.write(chunkMock);

        // Then
        ArgumentCaptor<Integer> intCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Orientation> orientationCaptor = ArgumentCaptor.forClass(Orientation.class);

        verify(loggerMock).info(
                eq("Mower at final position: ({}, {}) with orientation {}"),
                intCaptor.capture(),
                intCaptor.capture(),
                orientationCaptor.capture()
        );

        assertEquals(2, intCaptor.getAllValues().get(0));
        assertEquals(3, intCaptor.getAllValues().get(1));
        assertEquals(Orientation.N, orientationCaptor.getValue());

    }
}