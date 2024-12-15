package codes.thischwa.bcg.service;

import codes.thischwa.bcg.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class VdirSyncerCallerTest extends AbstractIntegrationTest {

    @Autowired private VdirSyncerCaller processor;

    @Test
    void test() {
        processor.testCall();
    }
}
