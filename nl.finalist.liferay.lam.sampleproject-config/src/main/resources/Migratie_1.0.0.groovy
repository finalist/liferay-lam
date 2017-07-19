create.customField (
	name: 'fieldTest',
	type: 'String',
	defaultValue: 'test',
	entityName: Entities.user,
	roles: [Roles.guest, Roles.user]
)

delete.customField(
	name: 'fieldTest',
	entityName: Entities.user
)
update.portalSettings(
	virtualHostName: "virtualTestName",
	portalName: "TestName",
	availableLanguages: "nl_NL,en_GB"
)

validate.portalProperties(
	"database.indexes.update.on.startup": "true",
	"auth.token.check.enabled": "true"
)
create.vocabulary(
	name: "TestVocabulary"
)
update.vocabulary(
	name: "TestVocabulary",
	forLanguage: "en_GB",
	translation: "TestVocabularyTranslation"
)
delete.vocabulary(
	name: "TestVocabulary"
)

create.role(
    name: "SomeRole",
	type: TypeOfRole.REGULARROLES,
	titles: [
		"en_GB": "SomeRole"
	],
	descriptions: [
		"en_GB": "SomeDescription"
	],
	permissions: [
		(Entities.webcontent):[ActionKeys.VIEW, ActionKeys.DELETE]
	]
)
