package nl.finalist.liferay.lam.dslglue

import com.liferay.portal.kernel.log.Log
import com.liferay.portal.kernel.log.LogFactoryUtil

class Entrypoint {

    private static final Log LOG = LogFactoryUtil.getLog Entrypoint.class

    int groupId;

    Entrypoint(Map arguments) {
        this.groupId = arguments.groupId;
    }


    static def with(Map arguments, Closure closure) {
        LOG.debug "setting context..."
        //def groupId = arguments.groupId ?: 0;

        Entrypoint entrypoint = new Entrypoint(arguments);
        entrypoint.execute(closure);
    }

    def execute(Closure closure) {
        closure();
    }
}
