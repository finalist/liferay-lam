createOrUpdate.structure(
    file: "ICOStructure.json",
    descriptionMap: [
       "nl_NL": "Dit is een structure voor een Eerste Beursgang Raadselvaluta",
       "en_US": "This is a structure for an Initial Coin Offering"],
    nameMap: ["nl_NL": "EBRStructure", "en_US": "ICOStructure"],
    structureKey: "ICO-STRUCTURE"
)
createOrUpdate.template(
    file: "ICOTemplate.vm",
    forStructure: "ICO-STRUCTURE",
    templateKey: "ICO-TEMPLATE",
    descriptionMap: ["nl_NL": "Dit is een template voor een Eerste Beursgang Raadselvaluta", "en_US": "This is a template for a Initial Coin Offering"],
    nameMap: ["nl_NL": "EBRTemplate", "en_US": "ICOTemplate"]
)
createOrUpdate.webcontent(
    titleMap: ["en_US": "Initial Coin Offering", "nl_NL": "Eerste Beursgang Raadselvaluta"],
    urlTitle: "ico",
    file: "ICO1.xml",
    id: "ICO1",
    forSite:"/cryptoex",
    structure:"ICO-STRUCTURE",
    template:"ICO-TEMPLATE"
)