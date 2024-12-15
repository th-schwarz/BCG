package codes.thischwa.bcg.service;

import codes.thischwa.bcg.Person;
import codes.thischwa.bcg.conf.BCGConf;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

/**
 * Service responsible for generating and synchronizing birthday calendars.
 */
@Service
@Slf4j
public class BirthdayCalGenerator {

  private final BCGConf conf;
  private final CalHandler calHandler;
  private final CardHandler cardHandler;
  private final VdirSyncerCaller vdirSyncerCaller;

  public BirthdayCalGenerator(
      BCGConf conf,
      CalHandler calHandler,
      CardHandler cardHandler,
      VdirSyncerCaller vdirSyncerCaller) {
    this.conf = conf;
    this.calHandler = calHandler;
    this.cardHandler = cardHandler;
    this.vdirSyncerCaller = vdirSyncerCaller;
  }

  /**
   * Processes the birthday calendar by performing a series of operations.
   * The actual approach is 'brute-force'.
   *
   * This method performs the following steps sequentially:
   * 1. Cleans the directory specified by the `vdirsyncerStatusDir` configuration.
   * 2. Logs the start and completion of the calendar clearing process.
   * 3. Clears the calendar using the `calHandler`.
   * 4. Clears the calendar directory using the `cardHandler`.
   * 5. Calls the `vdirSyncerProcessor` to discover calendar settings.
   * 6. Logs the start and completion of the iCalendar file generation process.
   * 7. Generates iCalendar files using the `cardHandler`.
   * 8. Logs the start and completion of the synchronization process.
   * 9. Calls the `vdirSyncerProcessor` to synchronize the calendar.
   *
   * <p>Currently this is the "brute-force" way.
   *
   * @throws IOException if an I/O error occurs during directory cleaning,
   *         calendar clearing, or file generation.
   */
  public void processBirthdayCal() throws IOException {
    Path statusDir = Paths.get(conf.vdirsyncerStatusDir());
    if (Files.exists(statusDir)) {
      FileUtils.cleanDirectory(statusDir.toFile());
    }
    log.info("Processing clean-up ...");
    calHandler.clearRemoteCalendar();
    calHandler.clearCalendarDir();
    log.info("Processed clean-up successfully.");


    log.info("Processing #generateICalFiles ...");
    List<Person> people = cardHandler.readPeopleWithBirthday();
    calHandler.generateICalFiles(people);
    log.info("Processed #generateICalFiles successfully.");

    vdirSyncerCaller.callDiscover();

    log.info("Processing #callSync ...");
    vdirSyncerCaller.callSync();
    log.info("Processed #callSync successfully.");
  }
}
