package pl.web.dsys.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.base.DsysTestContext;

@ExtendWith(AemContextExtension.class)
class ChildrenEditorTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String CHILDREN_EDITOR_COMPONENT = "/content/home/jcr:content/childEditor";

	MockSlingHttpServletRequest request;

	Tag tag;
	ResourceResolver resolver;
	TagManager tagManager;

	@BeforeEach
	public void setUp() {
		context.load().json("/childEditor.json", "/content");
		request = context.request();
		request.setAttribute("nodePath", "/content/home/jcr:content/childEditor");
		resolver = context.resourceResolver();
		tagManager = Mockito.mock(TagManager.class);
		tag = Mockito.mock(Tag.class);
		Mockito.when(tagManager.resolve(Mockito.anyString())).thenReturn(tag);
	}

	@Test
	void doTestChildrenEditor() {
		ChildrenEditor child = getChildrenEditorUnderTest(CHILDREN_EDITOR_COMPONENT);
	}

	private ChildrenEditor getChildrenEditorUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(ChildrenEditor.class);
	}
}
