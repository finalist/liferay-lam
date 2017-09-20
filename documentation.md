# Intro
This project allows you to define a script to set some of the Liferay settings that you would normally configure by hand. 
This allows for easier migrations of your Liferay projects between environments, as there is less chance of mistakes.

# Custom fields
You can create/update and delete custom fields.

## Create/update
The following script shows how to define a custom field in your script:

    create.customField (
		name: 'fieldGroupTest',
		type: CustomFieldType.TEXT_GROUP,
		defaultValue: 'a,b,c',
		entityName: Entities.user,
		roles: [Roles.guest, Roles.user],
		displayType: DisplayType.CHECKBOX
	)

If the custom field of this name already exists, it will be overwritten. For each custom field you have to define 
a name, a type (string or integer are the only two supported types at this moment), an entity name (the supported 
entities are listed in the table below), a default value, and the roles that will be allowed to view and update the 
custom field. For custom fields of the type TEXT_GROUP, you can also define a displayType. The newly created field will be added to the default Liferay instance. 

At the moment the following entities are supported:

| Entity name | Lifery object |
|---|---|
| user | User |
| usergroup | UserGroup |
| page | Layout |
| organization | Organization |
| role | Role |
| site | Group |
| webcontent | JournalArticle |

At the moment the following types are supported:

| Type |
|---|
| CustomFieldType.INTEGER_32 |
| CustomFieldType.TEXT |
| CustomFieldType.TEXT_GROUP |

This is the list of display types for TEXT_GROUP:

| Display type |
|---|
| DisplayType.CHECKBOX |
| DisplayType.RADIO |
| DisplayType.SELECTION_LIST |
| DisplayType.TEXT_BOX |

## Delete
The following script shows how to specify the deletion of a custom field in your script:

    delete.customField(
        name: 'fieldTest',
        entityName: Entities.user
    )

For each custom field you want to delete you have to specify the name and the entity name (see the table in the previous section for supported entities). Once again these custom fields are associated with the default Liferay instance.

# Portal properties
As portal properties require a restart of the server, we only validate them and you will still have to correct them by hand.

The following script shows how you can define the expected portal properties:

    validate.portalProperties(
        "database.indexes.update.on.startup": "true",
        "auth.token.check.enabled": "true"
    )

For each property you have to specify the name and the expected value. You will then get feedback on whether all the properties are valid, whether a given property is missing from portal-ext.properties, or when the value was different from what was expected. The software can not tell you when there are properties in portal-ext.properties that you were not expecting. All the feedback is written to the log files, so you will have to configure your server to output the logging in the way that suits you best.
Beware that the name of the property has to be put between quotes as well as the value!

# Portal settings
Portal settings can be updated. 

The following script shows how you can update the portal settings:

	update.portalSettings(
		virtualHostName: "virtualTestName",
		portalName: "TestName",
		availableLanguages: "nl_NL,en_GB"
	)

For each setting you have to specify the name and the value. Once again these settings will be associated with the default Liferay instance.

At the moment, the following settings are supported:

| setting |
|---|
| availableLanguage |
| defaultLandingPage |
| defaultLanguage |
| defaultLogoutPage |
| emailDomain |
| emailNotificationAddress |
| emailNotificationName |
| homeURL |
| portalName |
| termsOfUseRequired |
| timeZone |
| virtualHostName |

# Vocabulary
You can create, update and delete vocabularies.

## Create
The following script shows how you can create a vocabulary:

	create.vocabulary(
				name: [ "en_US" : "TestVocab5",
							"nl_NL" : "TestVocab5 NL",
							"en_GB" :"TestVocab5 GB"]
						)

As you can see, all you have to specify is the map of name of the vocabulary. This vocabulary will be added to the global scope. default Locale vocabulary name should be provided always otherwise it doesn't work as expected.

## Update
The following script shows how you can update a vocabulary:

	update.vocabulary(
				existingName : "Test US",
				name: [ "en_US" : "US Updated",
						  "nl_NL" : "NL Updated",
						  "en_GB" : "GB Updated"]
			)

To update a vocabulary, you have to specify the map of vocabulary names and the existing name of the vocabulary for which names should be updated. If the vocabulary doesn't exist in the global scope, an error message will be logged.

## Delete
The following script shows how you can delete a vocabulary:

	delete.vocabulary(
		existingName: "TestVocabulary"
	)
	
As you can see, all you have to specify is the name of the vocabulary. If the vocabulary doesn't exist in the global scope, an error message will be logged.

# Sites
You can create, update and delete sites.

## Create
The following script shows how you can create a vocabulary:

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
					nameMap: ["nl_NL": "paginanaam", 
								"en_US": "pagenameUS", 
								"en_GB": "pagenameGB"],
					titleMap: ["nl_NL": "pagina titel", 
								"en_US": "page title US", 
								"en_GB": "page titleGB"],
					descriptionMap: ["nl_NL": "pagina omschrijving",
										"en_US": "page description US", 
										"en_GB": "page description GB"],
					friendlyUrlMap: ["nl_NL": "/paginanaam", 
										"en_US": "/pagenameUS", 
										"en_GB": "/pagenameGB"],
					typeSettings: Templates.one_column,
					customFields: [
					    "customFieldPage": "customFieldPageValue"
					])
		
		page( privatePage: true,
					nameMap: ["nl_NL": "privatepageNL", 
								"en_US": "privatepageUS"],
					titleMap: ["nl_NL": "titel prive pagina", 
								"en_US": "title private page"],
					descriptionMap: ["nl_NL": "omschrijving prive pagina", 
										"en_US": "description private page"],
					friendlyUrlMap: ["nl_NL": "/privepagina", 
										"en_US": "/privatepage"],
					typeSettings: Templates.one_column,
					customFields: [
					    "customFieldPage": "customFieldPageValuePrivate"
					])
					
		page( privatePage: true,
					nameMap: ["nl_NL": "privatepageChildNL", 
								"en_US": "private child page"],
					titleMap: ["nl_NL": "titel prive subpagina", 
								"en_US": "private child page"],
					descriptionMap: ["nl_NL": "omschrijving prive subpagina", 
										"en_US": "private child page"],
					friendlyUrlMap: ["nl_NL": "/privesubpagina", 
										"en_US": "/private-child-page"],
					typeSettings: Templates.one_column,
					parentUrl: "/privatepageus"
		) 
	}

To create a site, you have to specify a map of names for the available locales. Make sure the default locale is present, as this will be the siteKey that you will later use for updating and deleting. You also have to specify a map of descriptions and a friendly URL. If you try to add a site that already exists, an error message will be logged.

It is also possible to give a value to a custom field. Of course this custom field has to exist before you can give it a value here. CustomFields is a map where the key is the name of the custom field, and the value is the actual value you want to give it.

Pages can be added to the site, as you can see in the example above. Pages also have a map of names, titles, descriptions and friendly URLS. This last map _always_ needs an "en_US" translation, even if that language is not available on your Liferay instance. (This is due to a bug in Liferay.) You also have to specify what kind of template to use in a field called typeSettings. (This can theoretically also be used to specify what portlets to deploy on the page.) You can also specify a parentURL. This is the friendly URL of the parent page, with "us" postfixed to the name. It is also possible to give a value to a custom field of the page. Of course this custom field has to exist before you can give it a value here. CustomFields is a map where the key is the name of the custom field, and the value is the actual value you want to give it.


This site will be created at the top level, and will be an open site. It will have the default restrictions on membership, will not inherit content, and will be immediately active.

## Update
The following script shows how you can update a site:

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
			nameMap: ["nl_NL": "sitepageNL", "en_US": "sitepageUS"],
			titleMap: ["nl_NL": "title of page"],
			descriptionMap: ["nl_NL": "description of page"],
			friendlyUrlMap: ["nl_NL": "/pagename"],
			typeSettings: Templates.one_column
		]
	]
	)

To update a site, you have to specify the siteKey, which is the title associated with the default locale. You will also have to specify a map of names and one of descriptions per locale and a friendlyURL.

It is also possible to add or update the value of a custom field. Of course this custom field has to exist before you can give it a value here. CustomFields is a map where the key is the name of the custom field, and the value is the actual value you want to give it.
Updating the existing page or adding a page is also possible.

## Delete
The following script shows how you can delete a site:

	delete.site(
		siteKey: "AutomatedTestSite"
	)
	
As you can see, all you have to specify is the siteKey of the site. This is the title associated with the default locale.

# Category
You can create, update and delete categories.

## Create
The following script shows how you can create a category to a vocabulary:

		create.category(
			name: [ "en_US" : "styleU",
					"nl_NL" : "styleN",
					"en_GB" :"styleG"],
			vocabularyName: "TestVocab5",
			title : "Testing it"
		)


As you can see, all you have to do is to specify the name of the vocabulary, name of category along with the locale and the title to the category. This category will be added to the vocabulary.

## Update
The following script shows how you can update a category:

	update.category(
			categoryName: "styleU",
			vocabularyName: "TestVocab5",
			updateName: [ "en_US" : "styleUpdate",
							 "nl_NL" : "styleNpdate",
							 "en_GB" :"styleGpdate"]
	)

To update a category, you have to specify the name of the category, the vocabulary name and the name it should be updated to along with the locale. If the category doesn't exist in the vocabulary, an error message will be logged.

## Delete
The following script shows how you can delete a category:

	delete.category(
	title: "categoryName",
	vocabularyName: "TestVocabulary"
	)
	
As you can see, all you have to do is to specify the title of the default locale  to be deleted and the vocabulary name where the category exists. If the categroy doesn't exist in the given vocabulary name, an error message will be logged.

# Roles and permissions
You can add roles with permissions.

The following script shows how you can add a role and the accompanying permissions:

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
    
As you can see, to add a role, you have to specify the name, the type, one or more titles, one or more descriptions, and
a map of permissions. Titles are a map of locales with the title of the role in that language. The same goes for
descriptions. Permissions are a map of entities with a list of accompanying actions.

The list of role types is as follows:

| type |
|---|
| TypeOfRole.REGULARROLES |
| TypeOfRole.SITEROLES |
| TypeOfRole.ORGANISATIONROLES | 
| TypeOfRole.STANDARD |

The list of entities is the same as that for custom fields, which is described above. The list of actions should be 
looked up in the liferay documentation, as it is too long to include here.

Beware that the name of the entity that is used as a key should be between parenthesis, as Groovy will treat it as a 
literal string otherwise.

#User groups
You can add user groups.

The following script shows how you can delete a vocabulary:

    create.userGroup(
        name: "usergroup1",
        description: "SomeUseGroupWeTested",
        customFields: [
            "someField": "another value"
        ]
    )
	
As you can see, you have to specify the name and the description. It is also possible to give a value to a custom field.
Of course this custom field has to exist before you can give it a value here. CustomFields is a map where the key is
the name of the custom field, and the value is the actual value you want to give it.
# WebContent
You can create, update and delete webcontent.

## Create
The following script shows how you can create a category to a vocabulary:

	create.webcontent(
	titleMap:["en_US": "SomeWebcontent title in locale US english"],
	descriptionMap: ["en_US": "Description of the webcontent in Locale US english"],
	content : "Content of the webcontent",
	urlTitle: "UrlTitle of the webcontent"
)


As you can see, all you have to do is to specify titleMap , descriptionMap, content and urlTitle of the webcontent. Default locale should be present in titleMap and descriptionMap otherwise webcontent is not added.

## Update
The following script shows how you can update a webcontent:

	update.webcontent(
	titleMap:["en_US" :"Update the title of the webcontent"],
	descriptionMap:["en_US":"Update the description of the webcontent"],
	content: "content of the webcontent to be updated",
	urlTitle: "Existing urlTitle"
	)

To update a webcontent, you have to specify the titleMap, the desciprionMap and the contenet which is to be updated to with the  existing urlTitle. If the webcontent with UrlTitle doesn't exist then it is considered to be a new webcontent and added.

## Delete
The following script shows how you can delete a category:

	delete.webcontent(
	urlTitle: "some-url-title"
	)
	
	
As you can see, all you have to do is to specify the Url title of the webcontent to be deleted . If the webcontent doesn't exist, an info message that deletion was not possible will be logged.
