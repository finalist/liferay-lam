create.customField (
	name: 'contactEmailAdres',
	type: CustomFieldType.TEXT,
	entityName: Entities.usergroup,
	roles: [Roles.guest, Roles.user]
)
create.userGroup(
	name: "beleggers",
	description: "Dit zijn mensen die beleggen.",
	customFields: [
	    "contactEmailAdres": "beleggen@fictionalbank.nl"
	]
)
createOrUpdate.structure(
	file: "/structures/myStructure.json",
	descriptionMap: ["nl_NL": "Dit is een nieuws structure", "en_GB": "This is a news structure"],
	nameMap: ["nl_NL": "NieuwStructure", "en_GB": "NewsStructure"],
	structureKey: "NIEUWS-STRUCTURE"
)

createOrUpdate.template(
	file: "/templates/myTemplate.vm",
	forStructure: "NIEUWS-STRUCTURE",
	templateKey: "NIEUWS-TEMPLATE",
	descriptionMap: ["nl_NL": "Dit is een nieuws template", "en_GB": "This is a news template"],
	nameMap: ["nl_NL": "NieuwsTemplate", "en_GB": "NewsTemplate"]
)

createOrUpdate.ADT(
	file: "/adts/myADT.vm",
	adtKey: "NEWS-ADT",
	type: ADTTypes.ASSET_PUBLISHER,
	descriptionMap: ["nl_NL": "Dit is een nieuws adt", "en_GB": "This is a news adt"],
	nameMap: ["nl_NL": "NewsADT", "en_GB": "NewsADT"]
)
createOrUpdate.webcontent(
	titleMap: ["en_GB": "NewsWebcontent", "nl_NL": "NieuwsWebcontent"],
	urlTitle: "test-webcontent",
    file: "/articles/testWebcontent.xml",
    id: "NEWS1",
    forSite:"/bank",
    forStructure:"NIEUWS-STRUCTURE",
    forTemplate:"NIEUWS-TEMPLATE"
)
create.user(
	screenName:"contact.beleggen",
	firstName:"contact",
	lastName:"beleggen",
	emailAddress: "beleggen@fictionalbank.nl",
	roles:[Roles.admin],
	sites:["/bank"],
	userGroups:["beleggers"]
)