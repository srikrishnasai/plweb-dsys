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
class AnimatingNumbersModelTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String ANIMATING_NUMBERS_COMPONENT = "/content/home/jcr:content/animating_numbers";

	@BeforeEach
	public void setUp() {
		context.load().json("/animatingNumbersModel.json", "/content");
	}
	
	@Test
	void doTestAnimatingNumbers() {
		AnimatingNumbersModel animating = getAnimatingNumbersModelUnderTest(ANIMATING_NUMBERS_COMPONENT);
		assertEquals("1", animating.getAnimatingNumbersStartValue());
		assertEquals("100", animating.getAnimatingNumbersEndValue());
		assertEquals("5", animating.getAnimatingNumbersIncrementValue());
		assertEquals("black", animating.getAnimatingNumbersColor());
		assertEquals("Prefix", animating.getAnimatingNumbersPrefix());
		assertEquals("Suffix", animating.getAnimatingNumbersSuffix());
		assertEquals("Animating Statics", animating.getAnimatingNumbersLabel());
		assertEquals("60", animating.getAnimatingNumbersAnimationInterval());
	}

	private AnimatingNumbersModel getAnimatingNumbersModelUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(AnimatingNumbersModel.class);
	}

}
