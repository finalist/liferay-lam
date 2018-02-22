package nl.finalist.liferay.lam.statemgnt.flyway;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.InfrastructureUtil;

import java.util.List;
import java.util.stream.Collectors;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import nl.finalist.liferay.lam.statemgnt.api.ScriptMigration;
import nl.finalist.liferay.lam.statemgnt.api.StateManager;

@Component(service = StateManager.class)
public class FlywayStateManager implements StateManager {

    private static final Log LOG = LogFactoryUtil.getLog(FlywayStateManager.class);


    private Flyway flyway = new Flyway();

    @Activate
    public void init() {
        LOG.debug("Initializing Flyway State Manager...");

        flyway.setSkipDefaultResolvers(true);

        flyway.setDataSource(InfrastructureUtil.getDataSource());

        flyway.setTable("LAM_Changelog");
        flyway.setBaselineOnMigrate(true);
        flyway.setBaselineVersion(MigrationVersion.fromVersion("0"));
        flyway.setBaselineDescription("Baseline by LAM statemanagement");
        flyway.migrate();
    }

    private void setScripts(List<ScriptMigration> scripts) {
        flyway.setResolvers(() -> scripts
            .stream()
            .map(FlywayLAMMigration::new)
            .collect(Collectors.toList()));
    }


    @Override
    public void migrate(List<ScriptMigration> scripts) {
        LOG.debug("Executing Flyway migrations...");
        setScripts(scripts);
        flyway.migrate();
    }

    @Override
    public void baseline() {
        LOG.debug("Executing Flyway baseline()...");
        flyway.baseline();
    }

    @Override
    public void repair() {
        LOG.debug("Executing Flyway repair()...");
        flyway.repair();
    }

    public void setFlyway(Flyway flyway) {
        this.flyway = flyway;
    }
}
