package nl.finalist.liferay.lam.statemgnt.flyway;

import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class FlywayStateManagerTest {


    @Mock
    private Flyway flyway;

    
    @InjectMocks
    private FlywayStateManager flywayStateManager;

    @Before
    public void setUp() {
        flywayStateManager = new FlywayStateManager();
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void init() {
        flywayStateManager.init();
        verify(flyway).migrate();
    }

    @Test
    public void initWithBaselineOnMigrate() {
        flywayStateManager.init();
        verify(flyway).setBaselineOnMigrate(true);
    }

}