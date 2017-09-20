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
	name: [ "en_US" : "TestVocabulary US",
			"nl_NL" : "TestVocabulary NL",
			"en_GB" :"TestVocabulary GB"]
)
update.vocabulary(
	existingName : "TestVocabulary US",
	name: [ "en_US" : "TestVocabulary US Updated",
			"nl_NL" : "TestVocabulary NL Updated",
			"en_GB" :"TestVocabulary GB Updated"]
)

create.vocabulary(
	name: [ "en_US" : "Test US",
			"nl_NL" : "Test NL",
			"en_GB" :"Test GB"]
)
update.vocabulary(
	existingName : "Test US",
	name: [ "en_US" : "US Updated",
			"nl_NL" : "NL Updated",
			"en_GB" : "GB Updated"]
)

delete.vocabulary(
	existingName: "Test US"
)

create.vocabulary(
name: [ "en_US" : "TestVocab5",
			"nl_NL" : "TestVocab5 NL",
			"en_GB" :"TestVocab5 GB"]
)

create.category(
	name: [ "en_US" : "styleUS",
			"nl_NL" : "styleNL",
			"en_GB" :"styleGB"],
	vocabularyName: "TestVocab5",
	title : "Testing it"
)

create.category(
name: [ "en_US" : "style2US",
			"nl_NL" : "style2NL",
			"en_GB" :"style2GB"],
	vocabularyName: "TestVocab5",
	title : "Testing it2"
)


delete.category(
	title: "style2US",
	vocabularyName: "TestVocab5"
)
create.category(
	name: [ "en_US" : "styleU",
			"nl_NL" : "styleN",
			"en_GB" :"styleG"],
	vocabularyName: "TestVocab5",
	title : "Testing it"
)
update.category(
categoryName: "styleU",
	vocabularyName: "TestVocab5",
	updateName: [ "en_US" : "styleUpdate",
			"nl_NL" : "styleNpdate",
			"en_GB" :"styleGpdate"]
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
				nameMap: ["nl_NL": "paginanaam", "en_US": "page name", "en_GB": "pagenameGB"],
				titleMap: ["nl_NL": "pagina titel", "en_US": "page title US", "en_GB": "page titleGB"],
				descriptionMap: ["nl_NL": "pagina omschrijving","en_US": "page description US", "en_GB": "page description GB"],
				friendlyUrlMap: ["nl_NL": "/paginanaam", "en_US": "/pagenameUS", "en_GB": "/pagenameGB"],
				typeSettings: Templates.one_column,
				customFields: [
				    "customFieldPage": "customFieldPageValue"
				])
	
	page( privatePage: true,
				nameMap: ["nl_NL": "prive pagina", "en_US": "private page"],
				titleMap: ["nl_NL": "titel prive pagina", "en_US": "title private page"],
				descriptionMap: ["nl_NL": "omschrijving prive pagina", "en_US": "description private page"],
				friendlyUrlMap: ["nl_NL": "/privepagina", "en_US": "/privatepage"],
				typeSettings: Templates.one_column,
				customFields: [
				    "customFieldPage": "customFieldPageValuePrivate"
				])
		
	page( privatePage: true,
				nameMap: ["nl_NL": "privatepageChildNL", "en_US": "private child page"],
				titleMap: ["nl_NL": "titel prive subpagina", "en_US": "private child page"],
				descriptionMap: ["nl_NL": "omschrijving prive subpagina", "en_US": "private child page"],
				friendlyUrlMap: ["nl_NL": "/privesubpagina", "en_US": "/private-child-page"],
				typeSettings: Templates.one_column,
				parentUrl: "/private-page"
	) 
}
