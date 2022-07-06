package pl.web.dsys.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.base.DsysTestContext;

@ExtendWith(AemContextExtension.class)
public class GlobalEnablerModelTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String GLOBAL_ENABLER_HEADER_COMPONENT = "/content/home/jcr:content/global_enabler_header";
	public static final String GLOBAL_ENABLER_FOOTER_COMPONENT = "/content/home/jcr:content/global_enabler_footer";
	public static final String GLOBAL_ENABLER_COMPONENT = "/content/home/jcr:content/global_enabler";

	@BeforeEach
	public void setUp() {
		context.load().json("/globalEnablerModel.json", "/content");
	}

	@Test
	void doTestGlobaleEnablerHeader() {
		GlobalEnablerModel header = getGlobalEnablerModelUnderTest(GLOBAL_ENABLER_HEADER_COMPONENT);
		assertEquals("header", header.getComponentType());
		assertEquals("/content/experience-fragments/plweb-dsys/us/en/site/header/master/jcr:content",
				header.getExperienceFragmentPath());
	}

	@Test
	void doTestGlobalEnablerFooter() {
		GlobalEnablerModel footer = getGlobalEnablerModelUnderTest(GLOBAL_ENABLER_FOOTER_COMPONENT);
		assertEquals("footer", footer.getComponentType());
		assertEquals("/content/experience-fragments/plweb-dsys/us/en/site/footer/master/jcr:content",
				footer.getExperienceFragmentPath());
	}

	@Test
	void doTestGlobalEnabler() {
		GlobalEnablerModel enabler = getGlobalEnablerModelUnderTest(GLOBAL_ENABLER_COMPONENT);
		assertNull(enabler.getComponentType());
		assertEquals(StringUtils.EMPTY, enabler.getExperienceFragmentPath());
	}

	private GlobalEnablerModel getGlobalEnablerModelUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(GlobalEnablerModel.class);
	}

}
