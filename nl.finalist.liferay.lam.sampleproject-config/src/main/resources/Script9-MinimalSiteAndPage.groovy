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

createOrUpdate.page(
	siteKey: "Updated created site",
	privatePage: true,
	nameMap: [
		"en_US": "Original name"
	],
	titleMap: [
		"en_US": "Original title"
	],
	descriptionMap: [
		"en_US": "Original description"
	],
	friendlyUrlMap: [
		"en_US": "/originalurl"
	]
)

createOrUpdate.page(
	siteKey: "Updated created site",
	privatePage: true,
	nameMap: [
		"en_US": "New name"
	],
	friendlyUrlMap: [
		"en_US": "/originalurl"
	]
)

createOrUpdate.page(
	siteKey: "Updated created site",
	privatePage: true,
	titleMap: [
		"en_US": "New title"
	],
	friendlyUrlMap: [
		"en_US": "/originalurl"
	]
)

createOrUpdate.page(
	siteKey: "Updated created site",
	privatePage: true,
	descriptionMap: [
		"en_US": "New description"
	],
	friendlyUrlMap: [
		"en_US": "/originalUrl"
	]
)
