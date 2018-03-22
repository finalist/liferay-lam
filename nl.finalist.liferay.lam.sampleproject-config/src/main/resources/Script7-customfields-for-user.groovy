import nl.finalist.liferay.lam.dslglue.CustomFieldType
import nl.finalist.liferay.lam.dslglue.Entities
import nl.finalist.liferay.lam.dslglue.Roles

create.customField (
	name: 'secondEmailAdres',
	type: CustomFieldType.TEXT,
	entityName: Entities.user,
	roles: [Roles.guest, Roles.user]
)

create.user(
	screenName:"t.ester",
	firstName:"Theo",
	lastName:"Ester",
	emailAddress: "t.ester@fictionalbank.nl",
	customFields: [
		"secondEmailAdres": "theo@ester.nl"
	]
)

update.user(
	screenName:"t.ester",
	customFields: [
		"secondEmailAdres": "t.ester@ester.nl"
	]
)
