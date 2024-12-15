package codes.thischwa.bcg.service;

import codes.thischwa.bcg.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class VdirSyncerConfigurationServiceTest extends AbstractIntegrationTest {

    @Autowired private VdirSyncerConfigurationService service;

    @Test
    void testGenerateConfig() throws IOException {
        byte[] bytes = getClass().getResourceAsStream("/vdirsyncer.config").readAllBytes();
        String expected = new String(bytes);
        String actual = service.generateConfig();
        assertEquals(expected.trim(), actual.trim());
    }
}
