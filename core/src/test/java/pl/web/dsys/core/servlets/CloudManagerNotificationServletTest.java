package pl.web.dsys.core.servlets;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.sling.testing.mock.sling.servlet.MockRequestPathInfo;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.base.DsysTestContext;

@ExtendWith(AemContextExtension.class)
class CloudManagerNotificationServletTest {

	private final AemContext context = DsysTestContext.newAemContext();
	// private final AemContext context = new
	// AemContext(ResourceResolverType.JCR_MOCK);
	MockSlingHttpServletRequest request;

	MockSlingHttpServletResponse response;

	MockRequestPathInfo requestPathInfo;

	CloudManagerNotificationServlet servlet = new CloudManagerNotificationServlet();
	
	CloudManagerNotificationServlet.Config config;

	@BeforeEach
	void setup() {
		request = context.request();
		response = context.response();
		request.setQueryString("challenge=krishna");
		request.setHeader("x-adobe-signature", "abcd");
		config = Mockito.mock(CloudManagerNotificationServlet.Config.class);
		Map<String, Object> params = new HashedMap<String, Object>();
		params.put("organization.id", "AB38333E55F2BDE17F000101@AdobeOrg");
		context.registerInjectActivateService(CloudManagerNotificationServlet.Config.class, params);
		Mockito.when(config.organization_id()).thenReturn("AB38333E55F2BDE17F000101@AdobeOrg");
		Mockito.when(config.technical_account_email()).thenReturn("21db785a-93f8-42e8-a6ee-8d75f3589a53@techacct.adobe.com");
		Mockito.when(config.technical_account_id()).thenReturn("7DB881DC62505EE60A495FBC@techacct.adobe.com");
		Mockito.when(config.api_key()).thenReturn("61bd153e5adb4e0c8b50f7e4fdb61911");
		Mockito.when(config.auth_server()).thenReturn("ims-na1.adobelogin.com");
		Mockito.when(config.client_secret()).thenReturn("p8e-a58HNE61t2-YXvFnfl41_Skq3P0qYe_f");
		Mockito.when(config.teams_webhook()).thenReturn(new String[] {"https://hooks.slack.com/services/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"});
		Mockito.when(config.private_key_path()).thenReturn("/mnt/crx/");
	}
	
	@Test
	void doTestGet() {
		try {
			servlet.doGet(request, response);
			assertEquals("krishna", response.getOutputAsString());
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void doTestPost() {
		try {
			servlet.doPost(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
