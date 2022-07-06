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
class PersonCardModelTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String PERSON_CARD_COMPONENT = "/content/home/jcr:content/personcard";

	@BeforeEach
	public void setUp() {
		context.load().json("/personCardModel.json", "/content");
	}

	@Test
	void doTestPersonCard() {
		PersonCardModel person = getPersonCardModelModelUnderTest(PERSON_CARD_COMPONENT);
		assertEquals("Person Name", person.getName());
		assertEquals("Designation", person.getPosition());
		assertEquals("true", person.getUseOriginalImage());
		assertEquals("true", person.getUseTransparentImage());
		assertEquals("compact", person.getCategory());
		assertEquals("test@gmail.com", person.getMail());
		assertEquals("lightGreyColor", person.getBgColor());
		assertEquals("123-456-7890", person.getTelephone());
		assertEquals("https://www.linkedin.com", person.getLinkedin());
		assertEquals("/content/dam/we-retail/en/activities/hiking-camping/Hiking-Campaign-Male.jpg",
				person.getFileReference());
		assertNull(person.getDesc());
		assertNull(person.getColor());
	}

	private PersonCardModel getPersonCardModelModelUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(PersonCardModel.class);
	}

}
