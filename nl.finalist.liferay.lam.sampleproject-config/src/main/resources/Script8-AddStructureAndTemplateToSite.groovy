createOrUpdate.structure(
	file: "/structures/myStructure.json",
	descriptionMap: [
	   "nl_NL": "Dit is een nieuws structure op de site",
	   "en_US": "This is a news structure"],
	nameMap: ["nl_NL": "NieuwStructure2", "en_US": "NewsStructure2"],
	structureKey: "NIEUWS-STRUCTURE2",
	siteKey: "Fictional Bank"
)

createOrUpdate.template(
	file: "/templates/myTemplate.vm",
	forStructure: "NIEUWS-STRUCTURE2",
	templateKey: "NIEUWS-TEMPLATE2",
	descriptionMap: ["nl_NL": "Dit is een nieuws template op de site", "en_US": "This is a news template"],
	nameMap: ["nl_NL": "NieuwsTemplate2", "en_US": "NewsTemplate2"],
	siteKey: "Fictional Bank"
)
