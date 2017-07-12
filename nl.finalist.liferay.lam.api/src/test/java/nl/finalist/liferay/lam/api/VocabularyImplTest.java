package nl.finalist.liferay.lam.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.PropsUtil;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ LanguageUtil.class, VocabularyImpl.class, PropsUtil.class, Locale.class })
public class VocabularyImplTest {

    @Mock
    private CompanyLocalService companyService;
    @Mock
    private AssetVocabularyLocalService vocabularyService;
    @Mock
    private UserLocalService userService;
    @Mock
    private CounterLocalService counterService;
    @Mock
    private Company mockCompany;
    @Mock
    private AssetVocabulary mockAssetVocabulary;
    @Mock
    private User mockDefaultUser;
    @Mock
    private HashMap<Locale, String> mockTitleMap;

    @Mock
    private ServiceContext mockServiceContext;
    @InjectMocks
    private VocabularyImpl vocabularyImpl;

    @Before
    public void setUp() {
        vocabularyImpl = new VocabularyImpl();
        PowerMockito.mockStatic(LanguageUtil.class);
        PowerMockito.mockStatic(PropsUtil.class);
        PowerMockito.mockStatic(Locale.class);

        PowerMockito.when(PropsUtil.get("company.default.web.id")).thenReturn("liferay.com");
        initMocks(this);
    }

    @Test
    public void testAddVocabulary() throws Exception {
        Set<Locale> mockLocales = new HashSet<>();
        PowerMockito.when(LanguageUtil.getAvailableLocales(1L)).thenReturn(mockLocales);
        String vocabularyName = "testName";
        when(companyService.getCompanyByWebId("liferay.com")).thenReturn(mockCompany);
        when(mockCompany.getCompanyId()).thenReturn(1L);
        when(userService.getDefaultUser(1L)).thenReturn(mockDefaultUser);
        when(mockDefaultUser.getUserId()).thenReturn(10L);
        whenNew(HashMap.class).withAnyArguments().thenReturn(mockTitleMap);
        whenNew(ServiceContext.class).withNoArguments().thenReturn(mockServiceContext);

        vocabularyImpl.addVocabulary(vocabularyName, 1L);

        verify(vocabularyService).addVocabulary(10L, 1L, null, mockTitleMap, mockTitleMap, "", mockServiceContext);
    }

    @Test
    public void testDeleteExistingVocabulary() throws PortalException {
        String vocabularyName = "testName";
        when(vocabularyService.getGroupVocabulary(1L, vocabularyName)).thenReturn(mockAssetVocabulary);
        when(mockAssetVocabulary.getVocabularyId()).thenReturn(123L);

        vocabularyImpl.deleteVocabulary(vocabularyName, 1L);

        verify(vocabularyService).deleteAssetVocabulary(123L);
    }

    @Test
    public void testDeleteNonExistingVocabulary() throws PortalException {
        String vocabularyName = "testNonexistingName";
        when(vocabularyService.getGroupVocabulary(1L, vocabularyName)).thenReturn(null);

        vocabularyImpl.deleteVocabulary(vocabularyName, 1L);

        verifyNoMoreInteractions(mockAssetVocabulary);
    }

    @Test
    public void testUpdateVocabularyTranslation() throws PortalException {
        String vocabularyName = "testName";
        when(vocabularyService.getGroupVocabulary(1L, vocabularyName)).thenReturn(mockAssetVocabulary);
        when(mockAssetVocabulary.getTitleMap()).thenReturn(mockTitleMap);

        vocabularyImpl.updateVocabularyTranslation("tst_TST", "translatedName", vocabularyName, 1L);

        verify(vocabularyService).updateAssetVocabulary(mockAssetVocabulary);
    }

    @Test
    public void testUpdateNonExistingVocabularyTranslation() throws PortalException {
        String vocabularyName = "testNonexistingName";
        when(vocabularyService.getGroupVocabulary(1L, vocabularyName)).thenReturn(null);

        vocabularyImpl.updateVocabularyTranslation("tst_TST", "translatedName", vocabularyName, 1L);

        verifyNoMoreInteractions(mockAssetVocabulary);
    }
}
