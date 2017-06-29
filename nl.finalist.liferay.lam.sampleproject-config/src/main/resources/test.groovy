test CustomFieldsOperation.create with type: "text", companyId:20116L, entity: Entities.user, fieldName: "testMeta", defaultValue: "default", roles: [Roles.guest] as String[]
test CustomFieldsOperation.delete with companyId: 20116L, entity: Entities.user, fieldName: 'someOtherCustomField'

println('all done?')