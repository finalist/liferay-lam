
create.customField (
	name: 'field1',
	type: 9,
	value: 'Value1',
	defaultValue: '0',
	roles: ['Roles.guest', 'Roles.user']
)

update.portalSettings(
	virtualHostName: "virtualTestName",
	portalName: "TestName",
	availableLanguages: "nl_NL,en_GB"
)

read.portalProperties(
	"database.indexes.update.on.startup": "true",
	"auth.token.check.enabled": "true"
)



