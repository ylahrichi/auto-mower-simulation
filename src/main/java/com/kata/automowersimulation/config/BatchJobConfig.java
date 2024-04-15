package com.kata.automowersimulation.config;

import com.kata.automowersimulation.batch.processor.MowerItemProcessor;
import com.kata.automowersimulation.batch.reader.MowerItemReader;
import com.kata.automowersimulation.batch.writer.MowerItemWriter;
import com.kata.automowersimulation.model.Mower;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.support.DatabaseType;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @author LAHRICHI Youssef
 */

@Configuration
@EnableBatchProcessing
public class BatchJobConfig {

    @Value("${app.file-path-input}")
    private Resource inputFile;

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder.setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public JobRepository jobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource());
        factory.setTransactionManager(transactionManager());
        factory.setDatabaseType(DatabaseType.H2.name());
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    public Job processMowersJob(Step processMowersStep) throws Exception {
        return new JobBuilder("processMowersJob", jobRepository())
                .incrementer(new RunIdIncrementer())
                .start(processMowersStep)
                .build();
    }

    @Bean
    public Step processMowersStep() throws Exception {
        return new StepBuilder("processMowersStep", jobRepository())
                .<Mower, Mower>chunk(10, transactionManager())
                .reader(new MowerItemReader(inputFile))
                .processor(new MowerItemProcessor())
                .writer(new MowerItemWriter())
                .build();
    }

}
