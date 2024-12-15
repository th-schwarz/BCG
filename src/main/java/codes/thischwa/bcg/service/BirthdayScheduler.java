package codes.thischwa.bcg.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Service class that schedules the generation of the birthday calendar.
 *
 * The main task performed by this service is to call the {@link BirthdayCalGenerator#processBirthdayCal()} method,
 * which handles the generation and upload of the birthday calendar.
 */
@Service
@EnableScheduling
@Profile("!test")
@Slf4j
public class BirthdayScheduler {

    private final BirthdayCalGenerator birthdayCalGenerator;

    public BirthdayScheduler(BirthdayCalGenerator birthdayCalGenerator) {
        this.birthdayCalGenerator = birthdayCalGenerator;
    }

    @Scheduled(cron = "${bcg.cron}")
    public void process() throws IOException {
        log.info("Processing birthday calendar ...");
        birthdayCalGenerator.processBirthdayCal();
        log.info("Processed birthday successfully.");
    }
}
