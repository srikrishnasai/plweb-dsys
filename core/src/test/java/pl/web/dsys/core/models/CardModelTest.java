package pl.web.dsys.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.base.DsysTestContext;

@ExtendWith(AemContextExtension.class)
class CardModelTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String CARD_COMPONENT = "/content/home/jcr:content/card";

	@BeforeEach
	public void setUp() {
		context.load().json("/cardModel.json", "/content");
	}

	@Test
	void doTestHeaderModel() {
		CardModel cardModel = getCardModelUnderTest(CARD_COMPONENT);
		assertEquals("Title", cardModel.getCardTitle());
		assertEquals("Description", cardModel.getCardDescription());
		assertEquals("true", cardModel.getUseOriginalImage());
		assertEquals("true", cardModel.getUseTransparentImage());
		assertEquals("/content/dam/we-retail/en/activities/hiking-camping/Hiking-Campaign-Male.jpg",
				cardModel.getFileReference());
		assertNull(cardModel.getCardWebpageTitle());
		assertNull(cardModel.getCardWebpageTarget());
		assertNull(cardModel.getCardWebpageTabTarget());
		assertNull(cardModel.getAlignments());
		assertNull(cardModel.getDisableImage());
	}

	private CardModel getCardModelUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(CardModel.class);
	}
}
