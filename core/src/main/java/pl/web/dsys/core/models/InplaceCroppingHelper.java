package pl.web.dsys.core.models;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;

import javax.annotation.PostConstruct;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;

import com.day.cq.wcm.foundation.Image;
import pl.web.dsys.core.utils.SharedContants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper to get the image source path for Image inplace editor
 *
 */

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class},
defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class InplaceCroppingHelper {

	private static final Logger LOG = LoggerFactory.getLogger(InplaceCroppingHelper.class);

	@SlingObject
	Resource res;

	@RequestAttribute
	String imageName;
	
	@RequestAttribute
	String nodeName;


	@PostConstruct 
	public void activate() {
	LOG.error("InplaceCroppingHelperSling");
		/*
		 * if node name is provided, get the child resource with that name of the
		 * current resource, if not, use the current resource
		 */

		if (StringUtils.isNotEmpty(nodeName)) {
			res = res.getChild(nodeName);
		}
				
	}

	private Image getImage(String name) {
		Image img = null;
		if (res != null) {
			if (StringUtils.isNotBlank(name)) {
				img = new Image(res, name);
			} else {
				img = new Image(res);
			}

		}
		return img;
	}
	
	private boolean hasImg(String name) {
		Image img = getImage(name);
		return img != null && img.hasContent();
	}

	public boolean isNamedImage() {
		return StringUtils.isNotBlank(imageName) ? hasImg(imageName) : false;
	}
	
	public boolean isSmallImage() {
		return hasImg(SharedContants.PROPERTY_ADAPTIVE_IMAGE_SMALL);
	}

	public boolean isLargeImage() {
		return hasImg(SharedContants.PROPERTY_ADAPTIVE_IMAGE_LARGE);
	}

	public boolean isMediumImage() {
		return hasImg(SharedContants.PROPERTY_ADAPTIVE_IMAGE_MEDIUM);
	}

}
