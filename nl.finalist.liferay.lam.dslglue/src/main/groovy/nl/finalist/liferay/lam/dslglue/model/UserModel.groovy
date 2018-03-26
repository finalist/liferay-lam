package nl.finalist.liferay.lam.dslglue.model


class UserModel {
	String screenName
	String firstName
	String lastName
	String newScreenName
	String emailAddress
	String[] roles
	String[] sites
	String[] userGroups
	Map<String, String> customFields
}