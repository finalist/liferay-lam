update.portalSettings(
        virtualHostName: "virtualTestName",
        portalName: "LAM Sample portal",
        availableLanguages: "nl_NL,en_US",
        timeZone: "Europe/Paris"
)
create.vocabulary(
        name: ["en_US": "Bank products",
               "nl_NL": "Bankierproducten"]
)

create.category(
        name: ["en_US": "Savings account",
               "nl_NL": "Spaarrekening"],
        vocabularyName: "Bank products"
)
create.category(
        name: ["en_US": "Debit account",
               "nl_NL": "Betaalrekening"],
        vocabularyName: "Bank products"
)

create.site(
        nameMap: [
                "en_US": "Fictional Bank",
                "nl_NL": "Fictiebank"
        ],
        friendlyURL: "/bank"
)

/* Minimalistic page: only the required parameters */
create.page(
       siteKey: "Fictional Bank",
       privatePage: false,
       nameMap: ["en_US": "Fictional bank2", "nl_NL": "Fictieve bank2"]
)

create.tag(
    name: "blockchain",
    forSite: "/bank"
)

create.tag(
    name: "cryptocurrency",
    forSite: "/bank"
)