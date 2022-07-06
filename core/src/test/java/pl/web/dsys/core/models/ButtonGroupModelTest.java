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
class ButtonGroupModelTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String BUTTON_GROUP_COMPONENT = "/content/home/jcr:content/button_group";

	@BeforeEach
	public void setUp() {
		context.load().json("/buttonGroupModel.json", "/content");
	}

	@Test
	void doTestButtonGroup() {
		ButtonGroupModel buttonGroup = getButtonGroupModelUnderTest(BUTTON_GROUP_COMPONENT);
		assertEquals("2", buttonGroup.getNumberOfButtons());
	}

	private ButtonGroupModel getButtonGroupModelUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(ButtonGroupModel.class);
	}

}
