package nl.finalist.liferay.lam.statemgnt.flyway;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.resolver.MigrationResolver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import nl.finalist.liferay.lam.statemgnt.api.MigrationExecutor;
import nl.finalist.liferay.lam.statemgnt.api.ScriptMigration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class FlywayStateManagerTest {


    @Mock
    private Flyway flyway;

    @InjectMocks
    private FlywayStateManager flywayStateManager;


    @Captor
    ArgumentCaptor<MigrationResolver[]> captor;


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

    @Test
    public void resolvers() {
        flywayStateManager.migrate(Collections.singletonList(new ScriptMigration() {

            @Override
            public String getName() {
                return "";
            }

            @Override
            public InputStream getStream() throws IOException {
                return null;
            }

            @Override
            public MigrationExecutor getMigrationExecutor() {
                return null;
            }
        }));

        verify(flyway).setResolvers(captor.capture());
        MigrationResolver[] registeredResolvers = captor.getValue();
        assertEquals(1, registeredResolvers.length);
    }
}