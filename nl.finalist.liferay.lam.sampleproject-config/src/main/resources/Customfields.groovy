import nl.finalist.liferay.lam.builder.CustomFieldsFactoryBuilder

def builder = new CustomFieldsFactoryBuilder()

def customField = builder.customField (
	name: 'field1',
	type: 0,
	value: 'Value1',
	defaultValue: '0',
	roles: ['Roles.guest', 'Roles.user']
)


def customField2 = builder.customField (
	name: 'field2',
	type: 1,
	value: 'Value2',
	defaultValue: '0',
	roles: ['Roles.guest', 'Roles.Owner']
)



println(customField);
println(customField2);
//println('done')