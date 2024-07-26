package com.sparta.publicclassdev.global.config;

import com.sparta.publicclassdev.domain.teams.service.TeamsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {

    private final TeamsService teamsService;

//    // 애플리케이션 시작 후 10초 후에 한 번 실행
//    @Scheduled(initialDelay = 10000, fixedDelay = Long.MAX_VALUE)
//    public void initialDelete() {
//        teamsService.deleteAllTeams();
//    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteTeamsMidnight() {
        teamsService.deleteAllTeams();
    }
}
