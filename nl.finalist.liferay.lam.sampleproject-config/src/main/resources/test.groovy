CustomField customField = new CustomField(customFields: customFields)

customField.add(type: "text", companyId: 20116L, entity:(Entities.user), fieldName: "test3", defaultValue: "default", roles: [(Roles.guest)] as String[]);

println('all done')
