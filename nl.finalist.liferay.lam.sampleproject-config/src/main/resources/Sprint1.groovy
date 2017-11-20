update.portalSettings(
        virtualHostName: "virtualTestName",
        portalName: "TestName",
        availableLanguages: "nl_NL,en_GB",
        timeZone: "Europe/Paris"
)
create.vocabulary(
        name: ["en_US": "Bank products",
               "nl_NL": "Bankierproducten",
               "en_GB": "Banking products"]
)

create.category(
        name: ["en_US": "Savings account",
               "nl_NL": "Spaarrekening",
               "en_GB": "Savings account"],
        vocabularyName: "Bank products"
)
create.category(
        name: ["en_US": "Debit account",
               "nl_NL": "Betaalrekening",
               "en_GB": "Debit account"],
        vocabularyName: "Bank products"
)

create.site(
        nameMap: [
                "en_US": "Fictional Bank",
                "en_GB": "Fictional Bank",
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