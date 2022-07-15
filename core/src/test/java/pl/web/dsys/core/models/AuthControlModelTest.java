package pl.web.dsys.core.models;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockRequestPathInfo;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.day.cq.wcm.api.WCMMode;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.base.DsysTestContext;

@ExtendWith(AemContextExtension.class)
class AuthControlModelTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String AUTH_COMPONENT = "/content/home/jcr:content/authModel";

	MockSlingHttpServletRequest request;
	MockRequestPathInfo requestPathInfo;
	@BeforeEach
	public void setUp() {
		context.load().json("/authModel.json", "/content");
		request = context.request();
		requestPathInfo = context.requestPathInfo();
	}

	@Test
	void doTest() {
		request.setAttribute("path", "/content/home");
		request.setAttribute("itemPath", "/content/home");
		AuthControlModel auth = getAuthControlModelUnderTest(AUTH_COMPONENT);
		assertNotNull(auth.isCompAuth());
		assertNotNull(auth.isDenyTags());
		assertNotNull(auth.isHasComponentAccess());
		assertNotNull(auth.isItemAuth());
		assertNotNull(auth.isItemAuthorized());
		assertNotNull(auth.isResourceAuthorized());
		assertNotNull(auth.getAuthtags());

	}
	
	@Test
	void doTestWcmDisabled() {
		request.setAttribute("path", "/content/home");
		request.setAttribute("itemPath", "/content/home");
		WCMMode.EDIT.toRequest(request);
		AuthControlModel auth = getAuthControlModelUnderTest(AUTH_COMPONENT);
		assertNotNull(auth.isCompAuth());
		assertNotNull(auth.isDenyTags());
		assertNotNull(auth.isHasComponentAccess());
		assertNotNull(auth.isItemAuth());
		assertNotNull(auth.isItemAuthorized());
		assertNotNull(auth.isResourceAuthorized());
		assertNotNull(auth.getAuthtags());

	}

	private AuthControlModel getAuthControlModelUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(AuthControlModel.class);
	}

}
