package com.sparta.publicclassdev.batch.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class BatchScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job postDailyCodeKata;

    @Scheduled(cron = "0 0 12 * * ?")
    public void runJob() {
        try {
            jobLauncher.run(postDailyCodeKata, new JobParameters());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
