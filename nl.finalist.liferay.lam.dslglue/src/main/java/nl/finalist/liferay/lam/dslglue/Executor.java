package nl.finalist.liferay.lam.dslglue;

import java.io.Reader;
import java.util.Map;
/**
 * Interface for executing DSL scripts
 */
public interface Executor {

    /**
     * Run the scripts
     */
    void runScripts(Map<String, String> structures, Reader... scripts);
}
