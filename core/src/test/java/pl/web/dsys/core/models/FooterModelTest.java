package pl.web.dsys.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.base.DsysTestContext;

@ExtendWith(AemContextExtension.class)
class FooterModelTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String FOOTER_COMPONENT = "/content/home/jcr:content/footer";

	@BeforeEach
	public void setUp() {
		context.load().json("/footerModel.json", "/content");
	}

	@Test
	void doTestButtonGroup() {
		FooterModel footer = getFooterModelUnderTest(FOOTER_COMPONENT);
		assertEquals("Products and Education", footer.getSection1Title());
		assertEquals("Resources", footer.getSection2Title());
		assertEquals("Support", footer.getSection3Title());
		assertEquals("true", footer.getUseOriginalImage());
		assertEquals("true", footer.getUseTransparentImage());
		assertEquals("/content/dam/we-retail/en/activities/hiking-camping/Hiking-Campaign-Male.jpg",
				footer.getImagePath());
		assertEquals("Copyright 2022 © Pacific Life Insurance Company", footer.getCopyright());
	}

	private FooterModel getFooterModelUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(FooterModel.class);
	}
}
