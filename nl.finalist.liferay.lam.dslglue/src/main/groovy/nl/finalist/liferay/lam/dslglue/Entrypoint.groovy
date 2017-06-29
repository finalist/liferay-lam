package nl.finalist.liferay.lam.dslglue

import com.liferay.portal.kernel.log.Log
import com.liferay.portal.kernel.log.LogFactoryUtil


class Entrypoint {

    private static final Log LOG = LogFactoryUtil.getLog Entrypoint.class

    int groupId;

    static def with(Map arguments, Closure closure) {

        // Set the 'scope' of the closure for when it's executed
        closure.delegate = new ExecutionContext(arguments);
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure();

    }


    enum CustomFieldsOperation {
        create, update, delete
    }
    static def customFields(CustomFieldsOperation op) {
        [
            with: {
                Map fields -> println "Here we actually do the operation ${op}, with fields: " + fields
            }
        ]
    }
}
