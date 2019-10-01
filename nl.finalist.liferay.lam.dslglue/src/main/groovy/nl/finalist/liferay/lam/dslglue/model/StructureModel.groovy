package nl.finalist.liferay.lam.dslglue.model

//Updated Structure's groovy model to have webIds attribute
class StructureModel {
    String[] webIds
    String file
    Map<String, String> nameMap
    Map<String, String> descriptionMap
    String structureKey
    String siteKey
}