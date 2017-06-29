//customFields create with groupId: 123, companyId: 123, className: 'User', fieldName: 'myCustomField'
//customField update with groupId: 3, companyId: 1, className: 'Site', fieldName: 'someOtherCustomField'

CustomField customField = new CustomField(customFields: customFields)

customField.add(type: "text", companyId: 20116L, entity:(Entities.user), fieldName: "test3", defaultValue: "default", roles: [(Roles.guest)] as String[]);

println('somethig done?')
