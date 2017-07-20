package nl.finalist.liferay.lam.dslglue;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.sql.Connection;
import java.sql.SQLException;

import org.flywaydb.core.api.MigrationType;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.migration.MigrationChecksumProvider;
import org.flywaydb.core.api.resolver.MigrationExecutor;
import org.flywaydb.core.internal.resolver.ResolvedMigrationImpl;

public class FlywayLAMMigration extends ResolvedMigrationImpl implements MigrationChecksumProvider {

    private static final Log LOG = LogFactoryUtil.getLog(FlywayLAMMigration.class);

    private final Executor executor;
    private Script script;

    public FlywayLAMMigration(Executor executor, Script script) {
        this.executor = executor;
        this.script = script;

        String usableVersion = script.getName()
            .replaceAll("[^\\d.]", "") // remove all chars that aren't numeric or dot
            .replaceAll("[\\D]+$","") // remove trailing dots
            .replaceAll("^[\\D]+",""); // remove heading dots
        LOG.debug(String.format("Setting migration version from script name '%s' to version '%s'",
            script.getName(), usableVersion));

        this.setVersion(MigrationVersion.fromVersion(usableVersion));
        setType(MigrationType.CUSTOM);
        setDescription(script.getName());
        setPhysicalLocation(script.getName());
        setScript(script.getName());
    }

    @Override
    public MigrationExecutor getExecutor() {

        return new MigrationExecutor() {
            @Override
            public void execute(Connection connection) throws SQLException {
                executor.runScripts(script.getReader());
            }

            @Override
            public boolean executeInTransaction() {
                return false;
            }
        };
    }

}
