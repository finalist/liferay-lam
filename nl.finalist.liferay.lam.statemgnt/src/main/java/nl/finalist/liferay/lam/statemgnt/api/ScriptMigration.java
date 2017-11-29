package nl.finalist.liferay.lam.statemgnt.api;

import java.io.IOException;
import java.io.InputStream;

public interface ScriptMigration {

    String getName();

    InputStream getStream() throws IOException;

    MigrationExecutor getMigrationExecutor();
}