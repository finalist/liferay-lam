package nl.finalist.liferay.lam.api;

import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.util.PropsUtil;

public class DeafultCompanyUtil {
	private static final Log LOG = LogFactoryUtil.getLog(DeafultCompanyUtil.class);
	@Reference
	private static CompanyLocalService companyService;
	private static Company defaultCompany;

	public static long getDefaultUserId() {
		 defaultCompany = getDefaultCompany();
		long userId = 0;
		try {
			userId = defaultCompany.getDefaultUser().getUserId();
		} catch (PortalException e) {
			LOG.error(String.format("Error while retrieving default userId, error is %s", e.getMessage()));
		}
		return userId;
	}

	public static Company getDefaultCompany() {
		 defaultCompany = null;
		String webId = PropsUtil.get("company.default.web.id");
		try {
			defaultCompany = companyService.getCompanyByWebId(webId);
		} catch (PortalException e) {
			LOG.error(String.format("Error while retrieving default company, error is %s", e.getMessage()));
		}
		return defaultCompany;
	}
	public static  long getGlobalGroupId() {
		 defaultCompany = getDefaultCompany();
		long groupId = 0;
		try {
			groupId = defaultCompany.getGroupId();
		} catch (PortalException e) {
			LOG.error("Error while retrieving global groupId", e);
		}
		return groupId;
	}
}
