package com.kata.automowersimulation.batch.writer;

import com.kata.automowersimulation.model.Mower;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author LAHRICHI Youssef
 */
@Component
@Setter
public class MowerItemWriter implements ItemWriter<Mower> {
    private Logger logger = LoggerFactory.getLogger(MowerItemWriter.class);

    @Override
    public void write(Chunk<? extends Mower> chunk) throws Exception {
        List<? extends Mower> mowers = chunk.getItems();
        for (Mower mower : mowers) {
            logger.info("Mower at final position: ({}, {}) with orientation {}",
                    mower.getPosition().getX(),
                    mower.getPosition().getY(),
                    mower.getPosition().getOrientation());
        }
    }
}
