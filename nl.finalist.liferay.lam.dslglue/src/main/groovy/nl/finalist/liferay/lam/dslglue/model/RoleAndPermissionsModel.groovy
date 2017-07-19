package nl.finalist.liferay.lam.dslglue.model

import nl.finalist.liferay.lam.api.TypeOfRole

class RoleAndPermissionsModel {
    String roleName
    TypeOfRole roleType
    Map<Locale, String> titles
    Map<Locale, String> descriptions
    Map<String, ArrayList<String>> permissions
}
