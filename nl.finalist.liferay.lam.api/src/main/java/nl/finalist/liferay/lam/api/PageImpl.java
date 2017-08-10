package nl.finalist.liferay.lam.api;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ServiceContext;

import nl.finalist.liferay.lam.api.model.PageModel;

@Component(immediate = true, service = Page.class)
public class PageImpl implements Page {

	@Reference
	LayoutLocalService pageService;

	@Override
	public void addPage(long userId, long groupId, PageModel page) throws PortalException {
		pageService.addLayout(userId, groupId, page.isPrivatePage(), LayoutConstants.DEFAULT_PARENT_LAYOUT_ID,
				page.getNameMap(), page.getTitleMap(), page.getDescriptionMap(), null, null,
				LayoutConstants.TYPE_PORTLET, page.getTypeSettings(), false, page.getFriendlyUrlMap(),
				new ServiceContext());
	}

	@Override
	public void updatePage(long layoutId, long groupId, PageModel page) throws PortalException{
		byte[] b = new byte[0];
		pageService.updateLayout(groupId, page.isPrivatePage(), layoutId, LayoutConstants.DEFAULT_PARENT_LAYOUT_ID, page.getNameMap(), page.getTitleMap(),
				page.getDescriptionMap(), null, null, LayoutConstants.TYPE_PORTLET, false, page.getFriendlyUrlMap(), false, b, new ServiceContext());
	}
	
	@Override
	public Layout fetchLayout(long groupId, boolean privateLayout, String friendlyURL) {
		 return pageService.fetchLayoutByFriendlyURL(groupId, privateLayout, friendlyURL);
	}
}
