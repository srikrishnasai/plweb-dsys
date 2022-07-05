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
class CircularRatingModelTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String CIRCULAR_RATING_COMPONENT = "/content/home/jcr:content/circular_rating";

	@BeforeEach
	public void setUp() {
		context.load().json("/circularRatingModel.json", "/content");
	}

	@Test
	void doTestSpacerModel() {
		CircularRatingModel circularRating = getCircularRatingModelUnderTest(CIRCULAR_RATING_COMPONENT);
		assertEquals("30", circularRating.getRatingText());
		assertEquals("Circular Rating", circularRating.getRatingTitle());
		assertEquals("turquoise", circularRating.getCircularRatingColor());
		assertEquals("small", circularRating.getCircularRatingSize());
	}

	private CircularRatingModel getCircularRatingModelUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(CircularRatingModel.class);
	}

}
