create.customField (
	name: 'fieldTest',
	type: CustomFieldType.TEXT,
	defaultValue: 'test',
	entityName: Entities.user,
	roles: [Roles.guest, Roles.user]
)
create.customField (
	name: 'fieldGroupTest',
	type: CustomFieldType.TEXT_GROUP,
	defaultValue: 'a,b,c',
	entityName: Entities.user,
	roles: [Roles.guest, Roles.user],
	displayType: DisplayType.CHECKBOX
)
create.customField (
	name: 'automatedField',
	type: CustomFieldType.TEXT,
	entityName: Entities.site,
	roles: [Roles.guest, Roles.user]
)

create.customField (
	name: 'someField',
	type: CustomFieldType.TEXT,
	entityName: Entities.usergroup,
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
	forLanguage: "nl_NL",
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

create.userGroup(
	name: "usergroup1",
	description: "SomeUseGroupWeTested",
	customFields: [
	    "someField": "another value"
	]
)

create.site(
	nameMap: [
		"en_US": "AutomatedTestSite",
		"nl_NL": "AutomatedTestSite"
	],
	descriptionMap: [
	    "nl_NL": "Description of automated site"
	],
	friendlyURL: "/automatedTestSite",
	customFields: [
	    "automatedField": "value"
	]
)

update.site(
	siteKey: "AutomatedTestSite",
	nameMap: [
		"en_US": "AutomatedTestSite",
		"nl_NL": "AutomatedTestSiteNL"
	],
	descriptionMap: [
	    "en_US": "Description",
	    "nl_NL": "Beschrijving"
	],
	friendlyURL: "/automatedTestSite",
	customFields: [
		"automatedField": "automated value"
	]
)
delete.site(
	siteKey: "AutomatedTestSite"
)