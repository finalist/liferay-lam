
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

create.site(
	nameMap: [
		"en_US": "AutomatedTestSite",
		"nl_NL": "AutomatedTestSite"
	],
	descriptionMap: [
	    "nl_NL": "Description of automated site"
	],
	friendlyURL: "/automatedTestSite"
)

update.site(
	groupKey: "AutomatedTestSite",
	nameMap: [
		"en_US": "AutomatedTestSite",
		"nl_NL": "AutomatedTestSiteNL"
	],
	descriptionMap: [
	    "en_US": "Description",
	    "nl_NL": "Beschrijving"
	],
	friendlyURL: "/automatedTestSite"
)
delete.site(
	groupKey: "AutomatedTestSite"
)
