package nl.finalist.liferay.lam.api;

import com.liferay.expando.kernel.service.ExpandoValueLocalService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true)
public class Lam {

    @Reference
    private ExpandoValueLocalService evls;

	// TODO: proper method name, proper arguments, javadoc, etc....
	public void createCustomField() {
        evls.addExpandoValue(null);
		throw new RuntimeException("Not implemented yet");
	}
}