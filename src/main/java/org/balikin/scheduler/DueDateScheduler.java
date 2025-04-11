package org.balikin.scheduler;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.balikin.service.impl.UtangPiutangServiceImpl;

@Slf4j
@ApplicationScoped
public class DueDateScheduler {
    @Inject
    UtangPiutangServiceImpl utangPiutangService;
    @Scheduled(cron = "0 0 0 * * ?")
    public void dueDate (ScheduledExecution execution) throws Exception {
        utangPiutangService.sendToInbox();
    }

}
