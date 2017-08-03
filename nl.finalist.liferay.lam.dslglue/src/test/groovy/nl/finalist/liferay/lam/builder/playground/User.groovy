package nl.finalist.liferay.lam.builder.playground

import groovy.transform.ToString

@ToString
class User {
    String screenName
    String firstName
    String emailAddress

    List<CustomFieldInstance> customFields = new ArrayList<>()

    void addCustomFieldInstance(CustomFieldInstance customFieldInstance) {
        customFields += customFieldInstance
    }
}