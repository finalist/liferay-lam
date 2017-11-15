package nl.finalist.liferay.lam.api;

import com.liferay.portal.kernel.model.Company;

public interface DefaultValue {

	long getDefaultUserId();

	Company getDefaultCompany();

	long getGlobalGroupId();

}