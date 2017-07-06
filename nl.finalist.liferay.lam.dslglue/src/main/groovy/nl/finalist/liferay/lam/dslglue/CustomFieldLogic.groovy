package nl.finalist.liferay.lam.dslglue

import nl.finalist.liferay.lam.api.CustomFields

// todo: figure out how to pass the original java CustomFields class instance to this place

static def test(CustomFieldsOperation op) {
    [
        with: {
            Map fields -> 
            switch(op) {
            	case CustomFieldsOperation.create:
            		if (fields.type == "text") {
            			println "creating text field"
						fields.customFields.addCustomTextField(fields.companyId, fields.entity, fields.fieldName, fields.defaultValue, fields.roles)
					} else if (fields.type == "number") {
						println "creating number field"
						fields.customFields.addCustomIntegerField(fields.companyId, fields.entity, fields.fieldName, fields.defaultValue, fields.roles)
					} else {
						println "This type is not supported"
					}
            		break
            	case CustomFieldsOperation.delete:
            		println "deleting field"
            		fields.customFields.deleteCustomField(fields.companyId, fields.entity, fields.fieldName)
            		break
            	default:
            		println "This action is not supported"
            }
        }
    ]
}
