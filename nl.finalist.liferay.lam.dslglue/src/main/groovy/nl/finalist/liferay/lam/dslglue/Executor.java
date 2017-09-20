package nl.finalist.liferay.lam.dslglue;

import java.io.Reader;

/**
 * Interface for executing DSL scripts
 */
public interface Executor {

    /**
     * Run the scripts
     */
	void runScripts(Reader... scripts);
}
