package com.kata.automowersimulation;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author LAHRICHI Youssef
 */
@SpringBootApplication(scanBasePackages = "com.kata.automowersimulation")
@EnableBatchProcessing
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(JobLauncher jobLauncher, Job job) {
        return args -> {
            JobExecution execution = jobLauncher.run(job, new JobParameters());
            System.out.println("Job Status: " + execution.getStatus());
            System.out.println("Job Completion: " + execution.getExitStatus());
        };
    }
}