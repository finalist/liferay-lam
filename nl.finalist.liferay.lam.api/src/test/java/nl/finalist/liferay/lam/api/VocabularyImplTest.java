package nl.finalist.liferay.lam.api;

import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PropsUtil;

import java.util.HashMap;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ LocaleUtil.class, VocabularyImpl.class, PropsUtil.class, Locale.class})
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
	@Mock
	private DefaultValue defaultValue;
    @InjectMocks
    private VocabularyImpl vocabularyImpl;

    @Before
    public void setUp() {
        vocabularyImpl = new VocabularyImpl();
        PowerMockito.mockStatic(LocaleUtil.class);
        PowerMockito.mockStatic(PropsUtil.class);
        PowerMockito.mockStatic(DefaultValueImpl.class);
        PowerMockito.when(PropsUtil.get("company.default.web.id")).thenReturn("liferay.com");
        initMocks(this);
    }

    @Test
    public void testAddVocabulary() throws Exception {
        String vocabularyName = "testName";
     
        Locale mockLocale = new Locale("nl_NL");
        PowerMockito.when(LocaleUtil.getSiteDefault()).thenReturn(mockLocale);
        when(defaultValue.getDefaultCompany()).thenReturn(mockCompany);
       when(defaultValue.getDefaultUserId()).thenReturn(10L); 
       
        whenNew(HashMap.class).withAnyArguments().thenReturn(mockTitleMap);
        whenNew(ServiceContext.class).withNoArguments().thenReturn(mockServiceContext);

        vocabularyImpl.addVocabulary(vocabularyName, 1L);

        verify(vocabularyService).addVocabulary(10L, 1L, vocabularyName, mockTitleMap, mockTitleMap, "", mockServiceContext);
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
