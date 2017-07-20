<<<<<<< Updated upstream
=======
import nl.finalist.liferay.lam.dslglue.CustomFieldType
>>>>>>> Stashed changes

create.customField (
	name: 'fieldTest',
	type: CustomFieldType.TEXT,
	defaultValue: 'test',
	entityName: Entities.user,
	roles: [Roles.guest, Roles.user]
)
create.customField (
	name: 'fieldGroupTest',
	type: CustomFieldType.TEXT_GROUP,
	defaultValue: 'a,b,c',
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
