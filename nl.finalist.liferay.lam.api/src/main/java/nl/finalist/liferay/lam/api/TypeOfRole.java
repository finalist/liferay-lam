
package nl.finalist.liferay.lam.api;

/**
 * Type of Custom Role should be added.
 *
 */
public enum TypeOfRole {
	REGULARROLES(1), SITEROLES(2), ORGANISATIONROLES(3), STANDARD(0);

	TypeOfRole(int value) {
		this.value = value;
	}

	private int value;

	public int getValue() {
		return value;
	}

}
