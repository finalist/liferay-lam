package nl.finalist.liferay.lam.dslglue

import nl.finalist.liferay.lam.api.CustomFields

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