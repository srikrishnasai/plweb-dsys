package pl.web.dsys.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
class AuthControlledExperienceFragmentTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String AUTHCONTROLLED_EXPERIENCE_COMPONENT = "/content/home/jcr:content/authControlled-experience-fragment";
	public static final String AUTHCONTROLLED_EXPERIENCE_COMPONENT_NO_VARIATION = "/content/home/jcr:content/authControlled-experience-fragment-no-variations";
	public static final String AUTHCONTROLLED_EXPERIENCE_COMPONENT_EMPTY = "/content/home/jcr:content/authControlled-experience-fragment-empty";

	@BeforeEach
	public void setUp() {
		context.load().json("/authControlledExperienceFragmentModel.json", "/content");
	}
	
	@Test
	void doTestAuthControlledExperienceFragment() {
		AuthControlledExperienceFragment auth = getAuthControlledExperienceFragmentModelUnderTest(AUTHCONTROLLED_EXPERIENCE_COMPONENT);
		assertEquals("/content/experience-fragments/plweb-dsys/us/en/site/header/master", auth.getDefaultXfPath());
		assertNotNull(auth.getVariations());
		assertEquals("amfauth:agency/PUBLIC", auth.getGroup());
		assertEquals("/content/experience-fragments/plweb-dsys/us/en/site/header/master/jcr:content", auth.getExperienceFragmentPath());
	}
	
	@Test
	void doTestAuthControlledExperienceFragmentNoVariations() {
		AuthControlledExperienceFragment auth = getAuthControlledExperienceFragmentModelUnderTest(AUTHCONTROLLED_EXPERIENCE_COMPONENT_NO_VARIATION);
		assertEquals("/content/experience-fragments/plweb-dsys/us/en/site/header/master", auth.getDefaultXfPath());
		assertNull(auth.getVariations());
		assertEquals("amfauth:agency/PUBLIC", auth.getGroup());
		assertEquals("/content/experience-fragments/plweb-dsys/us/en/site/header/master/jcr:content", auth.getExperienceFragmentPath());
	}
	
	@Test
	void doTestAuthControlledExperienceFragmentEmpty() {
		AuthControlledExperienceFragment auth = getAuthControlledExperienceFragmentModelUnderTest(AUTHCONTROLLED_EXPERIENCE_COMPONENT_EMPTY);
		assertEquals(StringUtils.EMPTY, auth.getDefaultXfPath());
		assertNull(auth.getVariations());
		assertEquals("amfauth:agency/PUBLIC", auth.getGroup());
		assertEquals(StringUtils.EMPTY, auth.getExperienceFragmentPath());
	}

	private AuthControlledExperienceFragment getAuthControlledExperienceFragmentModelUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(AuthControlledExperienceFragment.class);
	}
}
