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
class SpacerModelTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String SPACER_COMPONENT = "/content/home/jcr:content/spacer";
    public static final String SPACER_DEFAULT_HEIGHT_COMPONENT = "/content/home/jcr:content/spacerDefaultHeight";
    public static final String SPACER_EMPTY_FONTSIZE = "/content/home/jcr:content/spacerEmptyFontSize";
	@BeforeEach
	public void setUp() {
		context.load().json("/spacerModel.json", "/content");
	}

	@Test
	void doTestSpacerModel() {
		Spacer spacer = getSpacerModelUnderTest(SPACER_COMPONENT);
        assertEquals("20px", spacer.getHeight());
	}
	
	@Test
	void doTestSpacerModelWithDefaultHeight() {
		Spacer spacer = getSpacerModelUnderTest(SPACER_DEFAULT_HEIGHT_COMPONENT);
		assertEquals("10px", spacer.getHeight());
	}
	
	@Test
	void doTestSpacerModelEmptyFontSize() {
		Spacer spacer = getSpacerModelUnderTest(SPACER_EMPTY_FONTSIZE);
		assertEquals("30px", spacer.getHeight());
	}

	private Spacer getSpacerModelUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(Spacer.class);
	}
}
