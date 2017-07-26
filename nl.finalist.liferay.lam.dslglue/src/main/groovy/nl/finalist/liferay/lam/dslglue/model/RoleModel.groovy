package nl.finalist.liferay.lam.dslglue.model

import nl.finalist.liferay.lam.api.TypeOfRole

class RoleModel {
    String name
    TypeOfRole type
    Map<Locale, String> titles
    Map<Locale, String> descriptions
    Map<String, ArrayList<String>> permissions
}
