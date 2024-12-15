package codes.thischwa.bcg;

import codes.thischwa.bcg.conf.BCGConf;
import codes.thischwa.bcg.conf.DavConf;
import codes.thischwa.bcg.service.BirthdayCalGenerator;
import codes.thischwa.bcg.service.VdirSyncerConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

/** The ApplicationStartup class processes some tasks if the application is ready to start. */
@Component
@Profile("!test")
@Slf4j
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

  private final BCGConf config;

  private final DavConf davConf;

  private final BirthdayCalGenerator birthdayCalGenerator;

  private final Environment env;

  private final VdirSyncerConfigurationService vdirSyncerConfigurationService;

    /**
   * Initializes the application on startup.
     */
  public ApplicationStartup(
          BCGConf config,
          DavConf davConf, BirthdayCalGenerator birthdayCalGenerator,
          Environment env,
          VdirSyncerConfigurationService vdirSyncerConfigurationService) {
    this.config = config;
    this.davConf = davConf;
      this.birthdayCalGenerator = birthdayCalGenerator;
      this.env = env;
    this.vdirSyncerConfigurationService = vdirSyncerConfigurationService;
  }

  @Override
  public void onApplicationEvent(final ApplicationReadyEvent event) {
    String profiles = String.join(",", env.getActiveProfiles());
    log.info("*** Settings for BCG:");
    log.info("  * active profile(s): {}", !StringUtils.hasText(profiles) ? "n/a" : profiles);
    log.info("  * cron: {}", config.cron());
    log.info("  * run on start: {}", config.runOnStart());
    log.info("  * card-dav-url: {}", davConf.cardUrl());
    log.info("  * cal-dav-url: {}", davConf.calUrl());
    log.info("  * user: {}", davConf.user());

      try {
          vdirSyncerConfigurationService.checkConfig();
      } catch (IOException e) {
          throw new RuntimeException(e);
      }

      if (config.runOnStart()) {
        try {
            log.info("Processing on start ...");
            birthdayCalGenerator.processBirthdayCal();
            log.info("Processed on start successfully.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
  }
}
