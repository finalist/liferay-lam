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


create.webcontent(
	titleMap: [ 
		"en_US": "SomeRole"
			],
	descriptionMap: [
	    "en_US": "Description of Helping Webcontent"
	],
	content: "Content about the Helping Webcontent",
	urlTitle: "somerole"
)
update.webcontent(
	titleMap: [ 
		"en_US": "SomeRoleUpdate"
	],
	descriptionMap: [
	    "en_US": "Description of Helping Webcontent"
	],
	content: "Content about the Helping Webcontent",
	urlTitle: "somerolenew"
)

delete.webcontent(
	urlTitle: "some-url-title"
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
	],
	pages: [
		[
			privatePage: false,
			nameMap: ["nl_NL": "pagename", "en_US": "pagename"],
			titleMap: ["nl_NL": "title of page"],
			descriptionMap: ["nl_NL": "description of page"],
			friendlyUrlMap: ["nl_NL": "/pagename"],
			typeSettings: Templates.one_column
		],
		[
			privatePage: true,
			nameMap: ["nl_NL": "privatepageNL", "en_US": "privatepageUS"],
			titleMap: ["nl_NL": "title of private page"],
			descriptionMap: ["nl_NL": "description of private page"],
			friendlyUrlMap: ["nl_NL": "/privatepage"],
			typeSettings: Templates.one_column
		]
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
	],
	pages: [
		[
			privatePage: false,
			nameMap: ["nl_NL": "pagenameNL", "en_US": "pagenameUS"],
			titleMap: ["nl_NL": "title of page"],
			descriptionMap: ["nl_NL": "description of page"],
			friendlyUrlMap: ["nl_NL": "/pagename"],
			typeSettings: Templates.one_column
		],
		[
			privatePage: true,
			nameMap: ["nl_NL": "pageNL", "en_US": "pageUS"],
			titleMap: ["nl_NL": "title of pageNL"],
			descriptionMap: ["nl_NL": "description of page"],
			friendlyUrlMap: ["nl_NL": "/updateaddpage"],
			typeSettings: Templates.one_column
		]
	]
)
create.structure(file: 'mystructure.json')