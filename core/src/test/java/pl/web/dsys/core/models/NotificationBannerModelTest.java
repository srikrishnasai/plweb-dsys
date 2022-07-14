package pl.web.dsys.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockRequestPathInfo;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.base.DsysTestContext;

@ExtendWith(AemContextExtension.class)
class NotificationBannerModelTest {

	private final AemContext context = DsysTestContext.newAemContext();
	public static final String NOTIFICATION_COMPONENT = "/content/home/jcr:content/notificationBanner";
	
	MockSlingHttpServletRequest request;

	MockSlingHttpServletResponse response;

	MockRequestPathInfo requestPathInfo;

	@BeforeEach
	public void setUp() {
		context.load().json("/notificationBanner.json", "/content");
		request = context.request();
		response = context.response();
		requestPathInfo = context.requestPathInfo();
	}

	@Test
	void doTestNotificationBanner() {
		NotificationBannerModel notification = getNotificationBannerModelUnderTest(NOTIFICATION_COMPONENT);
        assertNotNull(notification.getComponentProperties());
        assertEquals("normalnotification", notification.getBannerType());
	}
	
	@Test
	void doTestNotificationBannerForCookie() {
		requestPathInfo.setSelectorString("cookieBanner");
		NotificationBannerModel notification = getNotificationBannerModelUnderTest(NOTIFICATION_COMPONENT);
        assertNull(notification.getComponentProperties());
        assertEquals("cookienotification", notification.getBannerType());
	}

	private NotificationBannerModel getNotificationBannerModelUnderTest(String resourcePath) {
		Resource resource = context.resourceResolver().getResource(resourcePath);
		if (resource == null) {
			throw new IllegalStateException("Did you forget to define test resource " + resourcePath + "?");
		}
		context.currentResource(resource);
		return context.request().adaptTo(NotificationBannerModel.class);
	}
}
