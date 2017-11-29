package nl.finalist.liferay.lam.statemgnt.api;

public interface MigrationExecutor {

        /**
         * Executes the migration this executor is associated with.
         */
        void execute();
}
