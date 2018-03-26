create.site(
	nameMap: [
		"en_US": "CreatedSite"
	],
	descriptionMap: [
	    "en_US": "Description of site"
	],
	friendlyURL: "/createdSite"
)

update.site(
	siteKey: "CreatedSite",
	descriptionMap: [
		"en_US": "New description"
	]
)

update.site(
	siteKey: "CreatedSite",
	nameMap: [
		"en_US": "Updated created site"
	]
)

update.site(
	siteKey: "Updated created site",
	friendlyURL: "/updatedSite"
)
