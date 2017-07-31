package nl.finalist.liferay.lam.api;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.util.LinkedList;
import java.util.List;

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
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.PropsUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ PropsUtil.class, CategoryImpl.class })
public class CategoryImplTest {

	@Mock
	private CompanyLocalService companyService;

	@Mock
	private Company company;
	@Mock
	private AssetVocabulary assetVocabulary;
	@Mock
	private AssetCategory assetCategroy;
	@Mock
	private AssetVocabularyLocalService assetVocabularyLocalService;
	@Mock
	private AssetCategoryLocalService assetCategoryLocalService;
	@Mock
	private User user;
	@Mock
	private ServiceContext mockServiceContext;
	@InjectMocks
	private CategoryImpl categoryImpl;

	@Before
	public void setUp() throws PortalException {
		categoryImpl = new CategoryImpl();
		PowerMockito.mockStatic(PropsUtil.class);
		PowerMockito.when(PropsUtil.get("company.default.web.id")).thenReturn("liferay.com");
		when(companyService.getCompanyByWebId("liferay.com")).thenReturn(company);
		
		
		initMocks(this);
	}

	@Test
	public void testAddNewCategory() throws Exception {
		when(company.getGroupId()).thenReturn(1L);
		when(company.getDefaultUser()).thenReturn(user);
		when(user.getUserId()).thenReturn(1L);
		when(assetVocabularyLocalService.getGroupVocabulary(1L, "test")).thenReturn(assetVocabulary);
		when(assetVocabulary.getVocabularyId()).thenReturn(123L);
		String[] names = new String[1];
		names[0] = "xxx";
		when(assetCategoryLocalService.getCategoryNames()).thenReturn(names);
		whenNew(ServiceContext.class).withNoArguments().thenReturn(mockServiceContext);
		when(assetCategoryLocalService.addCategory(1L, 1L, "Style", 123L, mockServiceContext))
				.thenReturn(assetCategroy);
		categoryImpl.addCategory("Style", "test", "testing it");
		verify(assetCategoryLocalService).addCategory(1L, 1L, "Style", 123L, mockServiceContext);
	}

	@Test
	public void testNotAddNewCategory() throws Exception {
		when(company.getGroupId()).thenReturn(1L);
		when(company.getDefaultUser()).thenReturn(user);
		when(user.getUserId()).thenReturn(1L);
		when(assetVocabularyLocalService.getGroupVocabulary(1L, "test")).thenReturn(assetVocabulary);
		when(assetVocabulary.getVocabularyId()).thenReturn(123L);
		String[] names = new String[1];
		names[0] = "xxx";
		when(assetCategoryLocalService.getCategoryNames()).thenReturn(names);
		whenNew(ServiceContext.class).withNoArguments().thenReturn(mockServiceContext);
		when(assetCategoryLocalService.addCategory(1L, 1L, "Style", 123L, mockServiceContext))
				.thenReturn(assetCategroy);
		categoryImpl.addCategory("xxx", "test", "testing it");
		verify(assetCategoryLocalService, never()).addCategory(1L, 1L, "Style", 123L, mockServiceContext);
	}

	@Test
	public void testUpdateCategory() throws PortalException {
		when(company.getGroupId()).thenReturn(1L);
		when(company.getDefaultUser()).thenReturn(user);
		when(user.getUserId()).thenReturn(1L);
		assetCategroy.setName("StyleUpdate");
		List<AssetCategory> assetCategories = new LinkedList<>();
		assetCategories.add(assetCategroy);
		assetVocabulary.getCategories().add(assetCategroy);

		when(assetVocabularyLocalService.getGroupVocabulary(1L, "vocabularyName")).thenReturn(assetVocabulary);
		when(assetVocabulary.getCategories()).thenReturn(assetCategories);
		when(assetCategroy.getName()).thenReturn("StyleUpdate");
		when(assetCategoryLocalService.updateAssetCategory(assetCategroy)).thenReturn(assetCategroy);
		categoryImpl.updateCategory("StyleUpdate", "vocabularyName", "updateName");
		verify(assetCategoryLocalService).updateAssetCategory(assetCategroy);
	}

	@Test
	public void testNotUpdateCategory() throws PortalException {
		when(company.getGroupId()).thenReturn(1L);
		when(company.getDefaultUser()).thenReturn(user);
		when(user.getUserId()).thenReturn(1L);
		assetCategroy.setName("StyleUpdate");
		List<AssetCategory> assetCategories = new LinkedList<>();
		assetCategories.add(assetCategroy);
		assetVocabulary.getCategories().add(assetCategroy);

		when(assetVocabularyLocalService.getGroupVocabulary(1L, "vocabularyName")).thenReturn(assetVocabulary);
		when(assetVocabulary.getCategories()).thenReturn(assetCategories);
		when(assetCategroy.getName()).thenReturn("StyleUpdate");
		when(assetCategoryLocalService.updateAssetCategory(assetCategroy)).thenReturn(assetCategroy);
		categoryImpl.updateCategory("StyleNotUpdate", "vocabularyName", "updateName");
		verify(assetCategoryLocalService, never()).updateAssetCategory(assetCategroy);
	}

	@Test
	public void testNotDeleteCategory() throws PortalException {
		when(company.getGroupId()).thenReturn(1L);
		when(company.getDefaultUser()).thenReturn(user);
		when(user.getUserId()).thenReturn(1L);
		assetCategroy.setName("StyleUpdate");
		List<AssetCategory> assetCategories = new LinkedList<>();
		assetCategories.add(assetCategroy);
		assetVocabulary.getCategories().add(assetCategroy);
		when(assetVocabularyLocalService.getGroupVocabulary(1L, "vocabularyName")).thenReturn(assetVocabulary);
		when(assetVocabulary.getCategories()).thenReturn(assetCategories);
		when(assetCategroy.getName()).thenReturn("StyleUpdate");
		categoryImpl.deleteCategory("StyleDelete", "vocabularyName");
		verify(assetCategoryLocalService, never()).deleteCategory(assetCategroy);
	}

	@Test
	public void testDeleteCategory() throws PortalException {
		when(company.getGroupId()).thenReturn(1L);
		when(company.getDefaultUser()).thenReturn(user);
		when(user.getUserId()).thenReturn(1L);
		assetCategroy.setName("StyleUpdate");
		List<AssetCategory> assetCategories = new LinkedList<>();
		assetCategories.add(assetCategroy);
		assetVocabulary.getCategories().add(assetCategroy);
		when(assetVocabularyLocalService.getGroupVocabulary(1L, "vocabularyName")).thenReturn(assetVocabulary);
		when(assetVocabulary.getCategories()).thenReturn(assetCategories);
		when(assetCategroy.getName()).thenReturn("StyleUpdate");
		categoryImpl.deleteCategory("StyleUpdate", "vocabularyName");
		verify(assetCategoryLocalService).deleteCategory(assetCategroy);
	}
}
