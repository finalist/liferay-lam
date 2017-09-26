package nl.finalist.liferay.lam.dslglue;

import java.io.Reader;

import org.osgi.framework.Bundle;
/**
 * Interface for executing DSL scripts
 */
public interface Executor {

    /**
     * Run the scripts
     */
    void runScripts(Bundle bundle, Reader... scripts);
}
