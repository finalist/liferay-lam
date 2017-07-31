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