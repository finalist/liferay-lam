package nl.finalist.liferay.lam.dslglue

import nl.finalist.liferay.lam.api.CustomFields

class CustomField {
	CustomFields customFields
	
	def add(Map args) {
		if (args.type == "text") {
			customFields.addCustomTextField(args.companyId, args.entity, args.fieldName, args.defaultValue, args.roles)
		} else if (args.type == "number") {
			customFields.addCustomIntegerField(args.companyId, args.entity, args.fieldName, args.defaultValue, args.roles)
		} else {
			// give an error message
		}
	}
	
	def delete(Map args) {
		customFields.deleteCustomField(args.companyId, args.entity, args.fieldName)
	}
}
