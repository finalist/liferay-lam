import nl.finalist.liferay.lam.api.CustomFields;

CustomFields.metaClass.addCustomTextField << { Map args ->
	customFields.addCustomTextField(args.companyId, args.entity, args.fieldName, args.defaultValue, args.roles)
}

customFields.addCustomTextField(20116L, (userClassName), "test3", "default", [(guestRole)] as String[]);
customFields.addCustomTextField(companyId:20116L, entity: (userClassName), fieldName: "testMeta", defaultValue: "default", roles: [(guestRole)] as String[])

println('we got there')