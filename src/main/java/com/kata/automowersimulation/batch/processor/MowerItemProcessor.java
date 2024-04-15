package com.kata.automowersimulation.batch.processor;

import com.kata.automowersimulation.model.Mower;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * @author LAHRICHI Youssef
 */
@Component
public class MowerItemProcessor implements ItemProcessor<Mower, Mower> {

    @Override
    public Mower process(final Mower mower) throws Exception {
        String instructions = mower.getInstructions();

        for (char instruction : instructions.toCharArray()) {
            mower.move(instruction);
        }

        return mower;
    }
}
