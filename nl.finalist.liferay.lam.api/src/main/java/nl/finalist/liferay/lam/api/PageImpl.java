package nl.finalist.liferay.lam.api;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.LocaleUtil;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import nl.finalist.liferay.lam.api.model.PageModel;
import nl.finalist.liferay.lam.util.LocaleMapConverter;

@Component(immediate = true, service = Page.class)
public class PageImpl implements Page {

    private static final Log LOG = LogFactoryUtil.getLog(PageImpl.class);
    @Reference
    LayoutLocalService pageService;
    @Reference
    private CustomFields customFieldsService;

    @Override
    public void addPage(long userId, long groupId, long groupPrimaryKey, PageModel page) throws PortalException {

        if (page.getType() == null || page.getType().isEmpty()) {
            page.setType("portlet");
        }
        Layout layout = pageService.addLayout(userId, groupId, page.isPrivatePage(), determineParentId(groupId, page),
            LocaleMapConverter.convert(page.getNameMap()),
            page.getTitleMap(),
            page.getDescriptionMap(), null, null,
            page.getType(), page.getTypeSettings(), false,
            LocaleMapConverter.convert(page.getFriendlyUrlMap()),
            new ServiceContext());

        LOG.info(String.format("Page %s added", page.getNameMap().get(LocaleUtil.getSiteDefault().toString())));
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
    public void updatePage(long layoutId, long groupId, long groupPrimaryKey, PageModel page) throws PortalException {
        if (page.getType() == null || page.getType().isEmpty()) {
            page.setType("portlet");
        }

        byte[] b = new byte[0];
        pageService.updateLayout(groupId, page.isPrivatePage(), layoutId, determineParentId(groupId, page),
            LocaleMapConverter.convert(page.getNameMap()), page.getTitleMap(),
            page.getDescriptionMap(), null, null, page.getType(), false, LocaleMapConverter.convert(page.getFriendlyUrlMap()), false, b, new ServiceContext());
        LOG.info(String.format("Page %s updated", page.getNameMap().get(LocaleUtil.getSiteDefault())));
    }

    @Override
    public Layout fetchLayout(long groupId, boolean privateLayout, String friendlyURL) {
        return pageService.fetchLayoutByFriendlyURL(groupId, privateLayout, friendlyURL);
    }
}
