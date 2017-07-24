package nl.finalist.liferay.lam.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.PropsUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ PropsUtil.class, CategoryImpl.class, DynamicQueryFactoryUtil.class })
public class CategoryImplTest {

	@Mock
	CompanyLocalService companyService;
	@Mock
	Company company;
	@Mock
	AssetVocabulary assetVocabulary;
	@Mock
	AssetCategory assetCategroy;
	@Mock
	AssetVocabularyLocalService assetVocabularyLocalService;
	@Mock
	AssetCategoryLocalService assetCategoryLocalService;
	@Mock
	private User user;
	 @Mock
	    private ServiceContext mockServiceContext;
	@InjectMocks
	CategoryImpl categoryImpl;

	@Before
	public void setUp() {
		categoryImpl = new CategoryImpl();
		PowerMockito.mockStatic(PropsUtil.class);
		PowerMockito.when(PropsUtil.get("company.default.web.id")).thenReturn("liferay.com");
		PowerMockito.mockStatic(DynamicQueryFactoryUtil.class);
		initMocks(this);
	}

	@Test
	public void testAddNewCategory() throws Exception {
		when(companyService.getCompanyByWebId("liferay.com")).thenReturn(company);
		when(company.getGroupId()).thenReturn(1L);
		when(company.getDefaultUser()).thenReturn(user);
        when(user.getUserId()).thenReturn(1L);

		when(assetVocabularyLocalService.getGroupVocabulary(1L, "test")).thenReturn(assetVocabulary);
		when(assetVocabulary.getVocabularyId()).thenReturn(123L);
		String[] names = new String[1];
		names[0] = "xxx";
		when(assetCategoryLocalService.getCategoryNames()).thenReturn(names);
		 whenNew(ServiceContext.class).withNoArguments().thenReturn(mockServiceContext);
		when(assetCategoryLocalService.addCategory(1L, 1L, "Style", 123L, mockServiceContext)).thenReturn(assetCategroy);
		categoryImpl.addCategory("Style", "test", "testing it");
		verify(assetCategoryLocalService).addCategory(1L, 1L, "Style", 123L, mockServiceContext);
	}
}
