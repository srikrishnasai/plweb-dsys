package pl.web.dsys.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.Arrays;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class})
public class NotificationBannerModel {

    Logger logger = LoggerFactory.getLogger(NotificationBannerModel.class);

    @Inject
    @Optional
    private ValueMap valueMap;
    
    @Inject
	private Resource resource;

    @SlingObject
    private SlingHttpServletRequest slingHttpServletRequest;

    private ValueMap componentProperties;
    private String bannerType;

    @PostConstruct
    public void activate() {
      
      String[] selectors = slingHttpServletRequest.getRequestPathInfo().getSelectors();
      
      if( null != selectors && Arrays.asList(selectors).contains("cookieBanner") ) {
          bannerType = "cookienotification";
      } else {
          valueMap = resource.adaptTo(ValueMap.class);
          componentProperties = valueMap;
          bannerType = "normalnotification";
      }
    }

    public ValueMap getComponentProperties() {
        return this.componentProperties;
    }

    public String getBannerType() {
        return this.bannerType;
    }
}
