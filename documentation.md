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

	