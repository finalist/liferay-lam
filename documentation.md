# Intro
This project allows you to define a script to set some of the Liferay settings that you would normally configure by hand. 
This allows for easier migrations of your Liferay projects between environments, as there is less chance of mistakes.

# Custom fields
You can create/update and delete custom fields.

## Create/update
The following script shows how to define a custom field in your script:

    create.customField (
        name: 'fieldTest',
        type: 'String',
        defaultValue: 'test',
        entityName: Entities.user,
        roles: [Roles.guest, Roles.user]
    )

If the custom field called 'fieldTest' already exists, it will be overwritten. For each custom field you have to define a name, a type (string or integer are the only two supported types at this moment), an entity name (the supported entities are listed in the table below), a default value, and the roles that will be allowed to view and update the custom field. The newly created field will be added to the default Liferay instance. 

| Entity name | Lifery object |
|---|---|
| user | User |
| usergroup | UserGroup |
| page | Layout |
| organization | Organization |
| role | Role |
| site | Group |
| webcontent | JournalArticle |

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
		name: "TestVocabulary"
	)

As you can see, all you have to specify is the name of the vocabulary. This vocabulary will be added to the global scope.

## Update
The following script shows how you can update a vocabulary:

	update.vocabulary(
		name: "TestVocabulary",
		forLanguage: "en_GB",
		translation: "TestVocabularyTranslation"
	)

To update a vocabulary, you have to specify the name, the language and the translation of the name. If the vocabulary doesn't exist in the global scope, an error message will be logged.

## Delete
The following script shows how you can delete a vocabulary:

	delete.vocabulary(
		name: "TestVocabulary"
	)
	
As you can see, all you have to specify is the name of the vocabulary. If the vocabulary doesn't exist in the global scope, an error message will be logged.

# Sites
You can create, update and delete sites.

## Create
The following script shows how you can create a vocabulary:

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

To create a site, you have to specify a map of names for the available locales. Make sure the default locale is present, as this will be the groupKey that you will later use for updating and deleting. You also have to specify a map of descriptions and a friendlyURL. If you try to add a site that already exists, an error message will be logged.

This group will be created at the top level, and will be an open group. It will have the default restrictions on membership, will not have a site associated with it, will not inherit content, and will be immediately active.

## Update
The following script shows how you can update a site:

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

To update a site, you have to specify the groupKey, which is the title associated with the default locale. You will also have to specify a map of names and one of descriptions per locale and a friendlyURL.

## Delete
The following script shows how you can delete a site:

	delete.site(
		groupKey: "AutomatedTestSite"
	)
	
As you can see, all you have to specify is the groupKey of the site. This is the title associated with the default locale.
	