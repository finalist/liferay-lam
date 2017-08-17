create.customField (
	name: 'fieldTest',
	type: CustomFieldType.TEXT,
	defaultValue: 'test',
	entityName: Entities.user,
	roles: [Roles.guest, Roles.user]
)
create.customField (
	name: 'someField',
	type: CustomFieldType.TEXT,
	entityName: Entities.usergroup,
	roles: [Roles.guest, Roles.user]
)
create.customField (
	name: 'customFieldPage',
	type: CustomFieldType.TEXT,
	entityName: Entities.page,
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
delete.customField(
	name: 'fieldTest',
	entityName: Entities.user
)
create.customField (
	name: 'someField',
	type: CustomFieldType.TEXT,
	entityName: Entities.usergroup,
	roles: [Roles.guest, Roles.user]
)
create.customField (
	name: 'automatedField',
	type: CustomFieldType.TEXT,
	entityName: Entities.site,
	roles: [Roles.guest, Roles.user]
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
create.vocabulary(
	name: "TestVocab5"
)
update.vocabulary(
	name: "TestVocab5",
	forLanguage: "en_GB",
	translation: "TestVocabularyTranslation"
)
create.category(
	name: "style",
	vocabularyName: "TestVocab5",
	title : "Testing it"
)
create.category(
	name: "style2",
	vocabularyName: "TestVocab5",
	title : "Testing it2"
)

delete.category(
	name: "style2",
	vocabularyName: "TestVocab5"
)


update.category(
	name: "style",
	updateName:"styleUpdate",
	vocabularyName: "TestVocab5"
)
	
delete.category(
	name: "styleUpdate",
	vocabularyName: "TestVocab5"
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
		"en_US": "AutomatedTestSite_en",
		"en_GB": "AutomatedTestSite_gb",
		"nl_NL": "AutomatedTestSite_nl"
	],
	descriptionMap: [
	    "en_US": "Description of automated site US",
	    "en_GB": "Description of automated site GB",
	    "nl_NL": "Omschrijving automated site"
	],
	friendlyURL: "/automatedTestSite",
	customFields: [
	    "automatedField": "value"
	]

){
	page( privatePage: false,
				nameMap: ["nl_NL": "paginanaam", "en_US": "pagenameUS", "en_GB": "pagenameGB"],
				titleMap: ["nl_NL": "pagina titel", "en_US": "page title US", "en_GB": "page titleGB"],
				descriptionMap: ["nl_NL": "pagina omschrijving","en_US": "page description US", "en_GB": "page description GB"],
				friendlyUrlMap: ["nl_NL": "/paginanaam", "en_US": "/pagenameUS", "en_GB": "/pagenameGB"],
				typeSettings: Templates.one_column,
				customFields: [
				    "customFieldPage": "customFieldPageValue"
				])
	
					
	page( privatePage: true,
				nameMap: ["nl_NL": "privatepageNL", "en_US": "privatepageUS"],
				titleMap: ["nl_NL": "titel prive pagina", "en_US": "title private page"],
				descriptionMap: ["nl_NL": "omschrijving prive pagina", "en_US": "description private page"],
				friendlyUrlMap: ["nl_NL": "/privepagina", "en_US": "/privatepage"],
				typeSettings: Templates.one_column,
				customFields: [
				    "customFieldPage": "customFieldPageValuePrivate"
				])
	 
}
