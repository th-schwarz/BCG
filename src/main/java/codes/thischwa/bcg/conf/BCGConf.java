package codes.thischwa.bcg.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bcg")
public record BCGConf(
    String product,
    String calendarCategory,
    String calendarDir,
    String cron,
    boolean runOnStart,
    String cleanConfigFile,
    String vdirsyncerConfig,
    String vdirsyncerStatusDir
) {}
