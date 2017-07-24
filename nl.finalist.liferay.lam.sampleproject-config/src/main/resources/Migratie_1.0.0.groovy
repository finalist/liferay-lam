
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