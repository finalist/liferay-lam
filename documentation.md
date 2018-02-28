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
		page( 
			privatePage: false,
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
			]
		)
		
		page( 
			privatePage: true,
			nameMap: ["nl_NL": "privatepageNL", 
						"en_US": "private page"],
			titleMap: ["nl_NL": "titel prive pagina", 
						"en_US": "title private page"],
			descriptionMap: ["nl_NL": "omschrijving prive pagina", 
								"en_US": "description private page"],
			friendlyUrlMap: ["nl_NL": "/privepagina", 
								"en_US": "/privatepage"],
			typeSettings: Templates.one_column,
			customFields: [
			    "customFieldPage": "customFieldPageValuePrivate"
			]
		)
					
		page( 
			privatePage: true,
			nameMap: ["nl_NL": "privatepageChildNL", 
						"en_US": "private child page"],
			titleMap: ["nl_NL": "titel prive subpagina", 
						"en_US": "private child page"],
			descriptionMap: ["nl_NL": "omschrijving prive subpagina", 
								"en_US": "private child page"],
			friendlyUrlMap: ["nl_NL": "/privesubpagina", 
								"en_US": "/private-child-page"],
			typeSettings: Templates.one_column,
			parentUrl: "/privatepage"
		)
		 
		page( 
			privatePage: true,
			nameMap: ["nl_NL": "verborgen pagina", "en_US": "hidden page"],
			titleMap: ["nl_NL": "titel verborgen pagina"],
			descriptionMap: ["nl_NL": "omschrijving verborgen pagina"],
			friendlyUrlMap: ["nl_NL": "/verborgenpagina", "en_US": "/hiddenpage"],
			typeSettings: Templates.one_column,
			hiddenPage: true
		)	
		
		page( 
			privatePage: true,
			nameMap: ["nl_NL": "paginalink", "en_US": "linked page"],
			titleMap: ["nl_NL": "titel paginalink"],
			descriptionMap: ["nl_NL": "omschrijving paginalink"],
			friendlyUrlMap: ["nl_NL": "/paginalink", "en_US": "/linkedpage"],
			linkedPageUrl: "/privatepage"
		)	
		
		page( 
			privatePage: true,
			nameMap: ["nl_NL": "url pagina", "en_US": "url page"],
			titleMap: ["nl_NL": "titel url pagina"],
			descriptionMap: ["nl_NL": "omschrijving url pagina"],
			friendlyUrlMap: ["nl_NL": "/urlpagina", "en_US": "/urlpage"],
			externalUrl: "http://www.nu.nl"
		)	
	}

To create a site, you have to specify a map of names for the available locales. Make sure the default locale is present, as this will be the siteKey that you will later use for updating and deleting. You also have to specify a map of descriptions and a friendly URL. If you try to add a site that already exists, an error message will be logged.

It is also possible to give a value to a custom field. Of course this custom field has to exist before you can give it a value here. CustomFields is a map where the key is the name of the custom field, and the value is the actual value you want to give it.

Pages can be added to the site, as you can see in the example above. Pages also have a map of names, titles, descriptions and friendly URLS. Both the map of names and the map of friendly urls _always_ need an "en_US" translation, even if that language is not available on your Liferay instance. (This is due to a bug in Liferay.) You also have to specify what kind of template to use in a field called typeSettings. (This can theoretically also be used to specify what portlets to deploy on the page.) You can also specify a parentURL. This is the friendly URL of the parent page. Mind you that the code assumes that your child page has the same access level as the parent (i.e. it is private or public just like the parent). It is also possible to give a value to a custom field of the page. Of course this custom field has to exist before you can give it a value here. CustomFields is a map where the key is the name of the custom field, and the value is the actual value you want to give it. If you want to hide a page from the navigation menu, you can add the hiddenPage property.

If you want to create a link to another page on your site, you can specify the linkedPageUrl property. Mind you that the code assumes that your link has the same access level as the page you are linking to (i.e. it is private or public just like the linked page). 

If you want to create a link to another url, you can specify the externalUrl property.

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
If you want to add a category that is nested under another category you can use the following script:

	create.category(
		name: [ "en_US" : "nestedStyleU",
				"nl_NL" : "nestedStyleN"],
		vocabularyName: "TestVocab5",
		title : "Testing it",
		parentCategoryName: "styleU"
	)

As you can see, this script requires the name of the parent category. 
		
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

# Tag
You can create and delete tags for specific sites or to the global scope

## Create 
The following script shows how you can create a tag for a specific site:

	create.tag(
		name: "testtag",
		forSite: "/automatedTestSite"
	)

As you can see, all you have to specify is the name and the friendly url for the site you want the tag added to. If you want to add a tag to the global scope, you can write the script with just the name like so:

	create.tag(
		name: "globaltag"
	)	
	
## Delete
The following script shows how you can delete a tag for a specific site:

	delete.tag(
		name: "testtag",
		forSite: "/automatedTestSite"
	)

All you have to specify is the name and site, just like with the creation of tags. If you want to delete a tag from the global scope you can just omit the forSite variable. 

    delete.tag(
        name: "globaltag"
    )

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

# Pages
Besides in the context of a site, you can also create, update and delete on their own. The syntax is mostly the same, but in this case you also have to pass along the siteKey. The following script gives a brief example of creating or updating a page:

	createOrUpdate.page(
        siteKey: "Fictional Bank",
        privatePage: false,
        nameMap: [
        	"en_US": "Fictional bank2", 
        	"nl_NL": "Fictieve bank2"
        ]
	) 

This is the most minimalistic way to create a page. You pass along the siteKey (which is the name of the site that you must have defined elsewhere), indicate whether the page is private or not and pass along the name map. 

# Structures and templates
You can create or update templates, structures and ADT's as well.

## Structure
The following script gives an example of adding or updating a structure for webcontent:

	createOrUpdate.structure(
		file: "/structures/myStructure.json",
		descriptionMap: [
			"nl_NL": "Dit is een test structure", 
			"en_US": "This is a test structure"
		],
		nameMap: [
			"nl_NL": "MyStructure", 
			"en_US": "MyStructure"
		],
		structureKey: "MY-STRUCTURE"
	)

As you can see you need to define a file in json format containing the actual structure and then you have to pass along the location and name of the file, along with a localized map of descriptions and names. The structure key is an identifier that you can use later to refer to this structure.

## Template
The following script gives an example of adding or updating a template for webcontent:

	createOrUpdate.template(
		file: "/templates/myTemplate.vm",
		forStructure: "MY-STRUCTURE",
		templateKey: "MY-TEMPLATE",
		descriptionMap: [
			"nl_NL": "Dit is een test template", 
			"en_US": "This is a test template"
		],
		nameMap: [
			"nl_NL": "MyTemplate", 
			"en_US": "MyTemplate"
		]
	)

As you can see you need to define a file in either Freemarker or Velocity format containing the actual template and then you have to pass along the location and name of the file, along with a localized map of descriptions and names. The template key is an identifier that you can use later to refer to this template. You also have to indicate which structure this template is meant for, here you should use the structureKey that was defined before.
	
## ADT
### create
The following script gives an example of creating or updating an ADT:


	createOrUpdate.ADT(
		file: "/adts/myADT.vm",
		adtKey: "MY-ADT",
		type: ADTTypes.ASSET_PUBLISHER,
		descriptionMap: [
			"nl_NL": "Dit is een test adt", 
			"en_US": "This is a test adt"
		],
		nameMap: [
			"nl_NL": "MyADT", 
			"en_US": "MyADT"
		]
	)

As you can see you need to define a file in either Freemarker or Velocity format containing the actual ADT and then you have to pass along the location and name of the file, along with a localized map of descriptions and names. The ADT key is an identifier that you can use later to refer to this ADT. You also have to define the type of content that this ADT applies to.

At the moment the following types are supported:

| ADT type |
|---|
| ASSET_PUBLISHER |
| DOCUMENTS_AND_MEDIA |
| CATEGORY_NAVIGATION |
| BREADCRUMBS |
| NAVIGATION_MENU |
| TAG_NAVIGATION |
| BLOGS |
| SITEMAP |
| LANGUAGE_SELECTOR |
| WIKI |
| RSS_PUBLISHER |

# WebContent
You can create, update and delete webcontent.

## CreateOrUpdate
The following script shows how you can create or update webcontent:

	createOrUpdate.webcontent(
		titleMap: [
			"en_US": "TestNOSITE", 
			"nl_NL": "TestNOSITE"
		],
		urlTitle: "test-nosite",
	   file: "/articles/testNoSite.xml",
	   id: "TSTNOSITE"
	)

As you can see you have to define the content as an xml file and then you have to pass along the location and name of the file, along with a localized map of titles. You also have to define the url title and the article id of the content. If you do not specify anything else, the webcontent that is created does not have a specific structure or template and will be added to the global scope.

It is also possible to specify structure, template and site. The following script shows how you do that:

	createOrUpdate.webcontent(
		titleMap: ["en_US": "TestWebcontent", "nl_NL": "TestWebcontent"],
		urlTitle: "test-webcontent",
	    file: "/articles/testWebcontent.xml",
	    id: "TSTWBCNT",
	    forSite:"/automatedTestSite",
	    structure:"MY-STRUCTURE",
	    template:"MY-TEMPLATE"
	)
	
The field 'forSite' contains the friendlyUrl of the site. The fields 'structure' and 'template' refer to the keys of the structure and the template respectively.

## Delete
The following script shows how you can delete a content item:

	delete.webcontent(
		urlTitle: "some-url-title"
	)
	
As you can see, all you have to do is to specify the Url title of the webcontent to be deleted . If the webcontent doesn't exist, an info message that deletion was not possible will be logged.

# Users
You can create, update and delete users

##Create
The following script shows you can create users:

	create.user(
		screenName:"t.testing",
		firstName:"test",
		lastName:"testing",
		emailAddress: "t.testing@testing.nl",
		roles: [Roles.admin],
		sites: ["/sitefriendlyurl1", "/siteFriendlyUrl2"],
		userGroups: ["usergroup1"]
	)

As you can see, you have to define a screenname, a first name, a last name and an emailaddress. These fields are all required to add a user.
Then there are some association fields like roles, sites & userGroups, these fields aren't required.
The field roles contains an array of the roles you want added for the user. See the chapter Roles on which roles you can use.
The field sites contains an array of site friendly urls of the sites the user has to be a member of.
The field userGroups contains an array of userGroup names that the user will be added to. If a role, site or userGroup can't be found with the provided name, it will not be added to the user.
If these fields aren't used at all the standard roles, sites & userGroups for a user are added as defined in the instance settings.
So for example, the following script is also correct: 
	
	create.user(
		screenName:"t.testing",
		firstName:"test",
		lastName:"testing",
		emailAddress: "t.testing@testing.nl"
	)

The password for the user will be generated and emailed to the emailAddress if a mail server has been configured at the server level.

##Update

The following script shows how you can update users:

    update.user(
		screenName:"t.testing",
		newScreenName:"t.testinga",
		lastName:"testinga"
	)

The only required field is screenName, since this is used to find the user. All other fields are only updated if they are added in the script.
If you want to update the screenName you have to add the field newScreenName. The old screenname has to be entered into the screenName field,
so the user can still be found.

##Delete

The following script shows how you can delete users:

    delete.user(
        screenName: "t.testinga"
    )

If a user with the given screenName is found it is deleted, otherwise no action is taken.

# Locales
When creating sites with LAM ('groups' in Liferay speak), a so called groupKey is generated, taken from the site's name in the default locale.
When having no en_US version of the site name, a GroupException is thrown.
If you don't want to have to specify english locales for all LAM resources, the solution for this
is to set ``company.default.locale`` in ``portal-ext.properties`` to the one locale that you want instead of en_US.
Like this:

    company.default.locale=nl_NL






# State management
LAM makes use of a state management component to keep track of which scripts are installed on which environments.

This is a powerful feature that makes LAM perfectly suitable for repeatedly deploying and redeploying on all environments, including production.

The most important consequence however is that scripts that are executed, are registered using a checksum of the contents of the script. This means that changing scripts that have been executed already is a bad practice.

In general this means: all changes belonging to the same feature/deployment/sprint should be grouped together and as soon as this script is deployed on an environment that should not be reset (acceptance / production) it should not be touched anymore.

Locally (and on a test environment?) this rule is a bit more flexible because you can reset/tune the internal state to reflect changes to scripts.

## Implementation
The state management component comprises of a third party package [Flyway](http://www.flywaydb.org), with an extension to make it work for LAM.
Because LAM uses Flyway under the hood, the same commands and structures apply.

* Scripts are ordered using natural order, and a version is derived from their file names
* Checksums are calculated based on the contents of the scripts

### Reset: command line utility
Database state can be reset using the flyway command line utility:

    flyway db reset --user=lam --password=lam --database=lam (how exactly?)

### Reset: control panel
With the module `nl.finalist.liferay.lam.admin.web` a control panel portlet is provided with which you can inspect and reset the database state.
