import nl.finalist.liferay.lam.dslglue.Templates

create.page(
        siteKey: "Fictional Bank",
        privatePage: false,
        nameMap: ["nl_NL": "De Nederlandsche Bank", "en_US": "De Nederlandsche Bank"],
        titleMap: ["nl_NL": "De Nederlandsche Bank"],
        friendlyUrlMap: ["nl_NL": "/dnb", "en_US": "/dnb"],
        externalUrl: "https://www.dnb.nl"
)

// TODO: create a useful/sensible example from this
create.page( privatePage: true,
        nameMap: ["nl_NL": "verborgen pagina", "en_US": "hidden page"],
        titleMap: ["nl_NL": "titel verborgen pagina"],
        descriptionMap: ["nl_NL": "omschrijving verborgen pagina"],
        friendlyUrlMap: ["nl_NL": "/verborgenpagina", "en_US": "/hiddenpage"],
        typeSettings: Templates.one_column,
        hiddenPage: true
)

// TODO: create a useful/sensible example from this
create.page(
        privatePage: false,
        nameMap: ["nl_NL": "paginalink", "en_US": "linked page"],
        titleMap: ["nl_NL": "titel paginalink"],
        descriptionMap: ["nl_NL": "omschrijving paginalink"],
        friendlyUrlMap: ["nl_NL": "/paginalink", "en_US": "/linkedpage"],
        linkedPageUrl: "/privatepage"
)