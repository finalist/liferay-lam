package nl.finalist.liferay.lam.statemgnt.api;

import java.util.Collection;

public interface MigrationResolver {

    Collection<ScriptMigration> resolveMigrations();

}
