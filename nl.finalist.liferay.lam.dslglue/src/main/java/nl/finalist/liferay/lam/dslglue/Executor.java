package nl.finalist.liferay.lam.dslglue;

/**
 * Interface for executing DSL scripts
 */
public interface Executor {

    /**
     * Run the scripts
     */
	void runScripts(String... scripts);
}
