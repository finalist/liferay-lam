package nl.finalist.liferay.lam.statemgnt.flyway;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;

import org.flywaydb.core.api.MigrationType;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.migration.MigrationChecksumProvider;
import org.flywaydb.core.api.resolver.MigrationExecutor;
import org.flywaydb.core.internal.resolver.ResolvedMigrationImpl;

import nl.finalist.liferay.lam.statemgnt.api.ScriptMigration;

/**
 * FlywayLAMMigration enables the versioning of migration scripts.
 * @author danielle.ardon
 *
 */
public class FlywayLAMMigration extends ResolvedMigrationImpl implements MigrationChecksumProvider {

    private static final Log LOG = LogFactoryUtil.getLog(FlywayLAMMigration.class);

    private nl.finalist.liferay.lam.statemgnt.api.MigrationExecutor executor;

    FlywayLAMMigration(ScriptMigration s) {
        setVersion(MigrationVersion.fromVersion(getUsableVersion(s.getName())));
        setType(MigrationType.CUSTOM);
        setDescription(s.getName());
        setPhysicalLocation(s.getName());
        setScript(s.getName());
        setChecksum(getChecksumSafe(s));

        executor = s.getMigrationExecutor();
    }

    @Override
    public MigrationExecutor getExecutor() {

        return new MigrationExecutor() {
            @Override
            public void execute(Connection connection) throws SQLException {
               executor.execute();
            }

            @Override
            public boolean executeInTransaction() {
                return false;
            }
        };
    }

    private String getUsableVersion(String scriptName){
        String usableVersion = scriptName
                        .replaceAll("[^\\d.]", "") // remove all chars that aren't numeric or dot
                        .replaceAll("[\\D]+$","") // remove trailing dots
                        .replaceAll("^[\\D]+",""); // remove heading dots
        LOG.debug(String.format("Setting migration version from script name '%s' to version '%s'",
                        scriptName, usableVersion));
        return usableVersion;
    }

    private Integer createChecksum(InputStream inputStream) throws IOException, NoSuchAlgorithmException {
        byte[] buffer = new byte[1024];
        MessageDigest complete;
        complete = MessageDigest.getInstance("SHA-1");

        int numRead;
        do {
            numRead = inputStream.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        inputStream.close();
        return new BigInteger(complete.digest()).intValue();
    }

    private Integer getChecksumSafe(ScriptMigration scriptMigration) {
        try {
            return createChecksum(scriptMigration.getStream());
        } catch (IOException | NoSuchAlgorithmException e) {
            LOG.info("Exception while generating checksum. Will use checksum 0", e);
            return 0;
        }
    }
}
