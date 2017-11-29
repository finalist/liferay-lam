package nl.finalist.liferay.lam.statemgnt.api;

import java.util.List;

public interface StateManager {

    void migrate(List<ScriptMigration> scripts);

    void baseline();

    void repair();
}
