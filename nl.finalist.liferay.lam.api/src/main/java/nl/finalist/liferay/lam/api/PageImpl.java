package nl.finalist.liferay.lam.api;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.*;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ServiceContext;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import nl.finalist.liferay.lam.api.model.PageModel;
import nl.finalist.liferay.lam.util.LocaleMapConverter;

@Component(immediate = true, service = Page.class)
public class PageImpl implements Page {

	@Reference
	LayoutLocalService pageService;
	@Reference
	private CustomFields customFieldsService;
	
	private static final Log LOG = LogFactoryUtil.getLog(PageImpl.class);
	@Override
	public void addPage(long userId, long groupId, long groupPrimaryKey, PageModel page) throws PortalException {
		Layout layout = pageService.addLayout(userId, groupId, page.isPrivatePage(), determineParentId(groupId, page),
				LocaleMapConverter.convert( page.getNameMap()), 
				page.getTitleMap(), 
				page.getDescriptionMap(), null, null,
				LayoutConstants.TYPE_PORTLET, page.getTypeSettings(), false, 
				page.getFriendlyUrlMap(),
				new ServiceContext());
		Map<String, String> customFields = page.getCustomFields();
		if (customFields != null) {
			for (String fieldName : customFields.keySet()) {
				LOG.debug(String.format("Page Custom field %s now has value %s", fieldName, customFields.get(fieldName)));

				customFieldsService.addCustomFieldValue(Layout.class.getName(), fieldName, layout.getPrimaryKey(),
						customFields.get(fieldName));
			}
		}

	}

	private long determineParentId(long groupId, PageModel page) throws PortalException {
		long parentId = LayoutConstants.DEFAULT_PARENT_LAYOUT_ID;
		if (page.getParentUrl() != null) {
			Layout parent = pageService.getFriendlyURLLayout(groupId, page.isPrivatePage(), page.getParentUrl());
			if (parent != null) {
				parentId = parent.getLayoutId();
			} else {
				LOG.error("The parent page could not be found, creating page at top level instead.");
			}
		}
		return parentId;
	}

	@Override
	public void updatePage(long layoutId, long groupId, long groupPrimaryKey, PageModel page) throws PortalException{
		byte[] b = new byte[0];
		pageService.updateLayout(groupId, page.isPrivatePage(), layoutId, determineParentId(groupId, page), 
				LocaleMapConverter.convert( page.getNameMap()), page.getTitleMap(),
				page.getDescriptionMap(), null, null, LayoutConstants.TYPE_PORTLET, false, page.getFriendlyUrlMap(), false, b, new ServiceContext());
	}
	
	@Override
	public Layout fetchLayout(long groupId, boolean privateLayout, String friendlyURL) {
		 return pageService.fetchLayoutByFriendlyURL(groupId, privateLayout, friendlyURL);
	}
}
