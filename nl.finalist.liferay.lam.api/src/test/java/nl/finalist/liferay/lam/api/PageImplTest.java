package nl.finalist.liferay.lam.api;

import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.liferay.portal.kernel.exception.NoSuchLayoutException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.PortletPreferencesLocalService;
import com.liferay.portal.kernel.service.ServiceContext;


import nl.finalist.liferay.lam.api.model.PageModel;
import nl.finalist.liferay.lam.util.LocaleMapConverter;

import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DefaultValueImpl.class })
public class PageImplTest {

	private static final String TEST_SITEKEY = "Test";
	@Mock
	private LayoutLocalService mockLayoutService;
	@Mock
	private CustomFields mockCustomFields;
	@Mock
	private DefaultValue mockDefaultValue;
	@Mock
	private GroupLocalService mockGroupService;
	@Mock
	private PortletPreferencesLocalService mockPreferencesService;
	
	@Mock
	private Company company;
	@Mock
	private Group group;
	@Mock
	private Layout layout;
	
	@InjectMocks
	private PageImpl page;
	
	private static final long USER_ID = 10L;
	private static final long GROUP_ID = 1L;

	Map<String, String> friendlyUrlMap = new HashMap<>();
	Map<Locale, String> titleMap = new HashMap<>();
	Map<String, String> nameMap = new HashMap<>();
	Map<Locale, String> descriptionMap = new HashMap<>();
	PageModel pageModel = new PageModel(TEST_SITEKEY, false, nameMap, titleMap, descriptionMap, friendlyUrlMap, "", null, null, null, null);

	@Before
	public void setup() throws PortalException {
		friendlyUrlMap.put("en_US", "test");
		titleMap.put(Locale.US, "title");
		nameMap.put("en_US", "name");
		descriptionMap.put(Locale.US, "description");
		pageModel.setHiddenPage(false);
		
		when(mockDefaultValue.getDefaultCompany()).thenReturn(company);
		when(company.getCompanyId()).thenReturn(GROUP_ID);
		when(mockGroupService.fetchGroup(GROUP_ID, TEST_SITEKEY)).thenReturn(group);
		when(group.getGroupId()).thenReturn(GROUP_ID);
		when(mockDefaultValue.getDefaultUserId()).thenReturn(USER_ID);
		// determine parent layout id
		when(mockLayoutService.getFriendlyURLLayout(GROUP_ID, false, "test")).thenThrow(new NoSuchLayoutException());

		when(layout.getName(Locale.US)).thenReturn("name");
		when(layout.getFriendlyURL()).thenReturn("test");
		when(layout.getPrimaryKey()).thenReturn(1L);
	}
	
	@Test
	public void addPage_shouldAdd_whenPageDoesntExist() throws PortalException {
		when(mockLayoutService.fetchLayoutByFriendlyURL(GROUP_ID, false, "test")).thenReturn(null);
		when(mockLayoutService.addLayout(anyLong(), anyLong(), anyBoolean(), anyLong(), any(),
				any(), any(), any(), any(), anyString(), anyString(), anyBoolean(), any(), any())).thenReturn(layout);

		page.addPage(TEST_SITEKEY, pageModel);
		
		verify(mockLayoutService.addLayout(USER_ID, GROUP_ID, false, LayoutConstants.DEFAULT_PARENT_LAYOUT_ID, LocaleMapConverter.convert(nameMap),
				titleMap, descriptionMap, null, null, "portlet", "", false, LocaleMapConverter.convert(friendlyUrlMap), new ServiceContext()));
	}
	
	@Test
	public void addPage_shouldUpdate_whenPageExists() throws PortalException {
		when(mockLayoutService.fetchLayoutByFriendlyURL(GROUP_ID, false, "test")).thenReturn(layout);
		when(mockLayoutService.updateLayout(anyLong(), anyBoolean(), anyLong(), anyLong(), any(),
				any(), any(), any(), any(), anyString(), anyBoolean(), any(), anyBoolean(), any(), any())).thenReturn(layout);

		page.addPage(TEST_SITEKEY, pageModel);
		
		verify(mockLayoutService.updateLayout(GROUP_ID, false, 1L, LayoutConstants.DEFAULT_PARENT_LAYOUT_ID, LocaleMapConverter.convert(nameMap),
				titleMap, descriptionMap, null, null, "portlet", false, LocaleMapConverter.convert(friendlyUrlMap), false, null, new ServiceContext()));
	}
	
	@Test
	public void addPage_shouldAddCustomFields_whenCustomFieldsHaveBeenSpecified() throws PortalException {
		when(mockLayoutService.fetchLayoutByFriendlyURL(GROUP_ID, false, "test")).thenReturn(null);
		when(mockLayoutService.addLayout(anyLong(), anyLong(), anyBoolean(), anyLong(), any(),
				any(), any(), any(), any(), anyString(), anyString(), anyBoolean(), any(), any())).thenReturn(layout);
		
		Map<String, String> customFields = new HashMap<>();
		customFields.put("test", "test");
		pageModel.setCustomFields(customFields);
		
		page.addPage(TEST_SITEKEY, pageModel);
		
		verify(mockLayoutService).addLayout(anyLong(), anyLong(), anyBoolean(), anyLong(), any(),
				any(), any(), any(), any(), anyString(), anyString(), anyBoolean(), any(), any());
		verify(mockCustomFields).addCustomFieldValue(Layout.class.getName(), "test", 1L, "test");
	}
}
