package pl.web.dsys.core.models;

import java.util.Arrays;

import javax.jcr.RepositoryException;

import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;

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
import org.apache.sling.models.annotations.DefaultInjectionStrategy;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.foundation.Image;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class},
defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AdaptiveImage {

	private static final Logger LOG = LoggerFactory.getLogger(AdaptiveImage.class);

	private static final String IMAGE_PATH_FORMAT = "%s.%s.img.transform/%s/image.jpg";
	private static final String IMAGE_PNG_PATH_FORMAT = "%s.%s.img.transform/%s/image.png";
	private static final String PROPERTY_ALT_TAG = "altTag";
	private static final String PROPERTY_ALT_FLEXIBLE_TAG = "alt";
	private static final String PROPERTY_DAM_ALT_TAG = "altValueFromDAM";
	private static final String PROPERTY_TITLE_TAG = "jcr:title";
	private static final String PROPERTY_DAM_TITLE_TAG = "titleValueFromDAM";
	private static final String PROPERTY_URL_LINK_TAG = "linkURL";
	private static final String PROPERTY_ISDECORATIVE_TAG = "isDecorative";
	private static final String PROPERTY_DISPLAY_POPUP_TITLE_TAG = "displayPopupTitle";
	private static final String PROPERTY_LARGE_IMAGE_DAM = "largeImageDAM";
	private static final String PROPERTY_MEDIUM_IMAGE_DAM = "mediumImageDAM";
	private static final String PROPERTY_SMALL_IMAGE_DAM = "smallImageDAM";
	private static final String PROPERTY_LARGE_IMAGE_TRANSPARENT = "largeTransparentImage"; 
	private static final String PROPERTY_MEDIUM_IMAGE_TRANSPARENT = "mediumTransparentImage";
	private static final String PROPERTY_SMALL_IMAGE_TRANSPARENT = "smallTransparentImage";
	private static final String PROPERTY_IMAGE_POSITION_TAG = "imagePos";
	private static final String PROPERTY_ADAPTIVE_IMAGE_SMALL = "small";
	private static final String PROPERTY_ADAPTIVE_IMAGE_MEDIUM = "medium";
	private static final String PROPERTY_ADAPTIVE_IMAGE_LARGE = "large";

	private String smallTransformName;
	private String mediumTransformName;
	private String largeTransformName;
	private String imagePosition;
	private boolean lazyLoad = false;
	private boolean transparent = false;
	private String title;

	@SlingObject
	SlingHttpServletRequest request;

	@SlingObject
	Resource resource;

	ValueMap valueMap;

	@PostConstruct 
	public void activate() throws Exception {
		LOG.debug("Activating AdaptiveImage...");
		LOG.debug("... request: {}", request.getRequestPathInfo());
		String[] selectors = request.getRequestPathInfo().getSelectors();
		valueMap = resource.getValueMap();

		if (selectors != null && selectors.length > 0) {
			LOG.debug("in selectors not null");
			LOG.debug("... selectors: {}", Arrays.toString(selectors));

			this.setLargeTransformName(selectors[0]);
			if (selectors.length > 1) {
				this.setMediumTransformName(selectors[1]);
			}

			if (selectors.length > 2) {
				this.setSmallTransformName(selectors[2]);
			}

			if (selectors.length > 3) {
				this.setLazyLoad(true);
			}
		} 

		/*If for some reason, the transforms are not set, setting default transforms*/
		if(null == this.getLargeTransformName() || StringUtils.isBlank(this.getLargeTransformName())
				|| !this.getLargeTransformName().contains("dsys-flexImg")) {
			this.setLargeTransformName("dsys-flexImg1440w");
		}
		if(null == this.getMediumTransformName() || StringUtils.isBlank(this.getMediumTransformName())
				|| !this.getMediumTransformName().contains("dsys-flexImg")) {
			this.setMediumTransformName("dsys-flexImg992w");
		}
		if(null == this.getSmallTransformName() || StringUtils.isBlank(this.getSmallTransformName())
				|| !this.getSmallTransformName().contains("dsys-flexImg")) {
			this.setSmallTransformName("dsys-flexImg768w");
		}
	}

	private Image getImage(String imageType) {
		return new Image(resource, imageType);
	}

    private String getFileName(String imagefileReference) {
        return imagefileReference.substring(imagefileReference.lastIndexOf("/")+1);
    }

	// ------------ Get local DAM AdaptiveImage paths for various sizes
	public String getSmallImagePath() {
		Image image;

		image = getImage(PROPERTY_ADAPTIVE_IMAGE_SMALL);
		if (checkContent(image)) {
			if(valueMap.containsKey(PROPERTY_SMALL_IMAGE_TRANSPARENT)) {
				this.transparent = true;
			}
			if(valueMap.containsKey(PROPERTY_SMALL_IMAGE_DAM)){
				return getAdaptiveImagePath("dsys-flexImg", image);
			}
			return getAdaptiveImagePath(getSmallTransformName(), image);
		}
	// ------------ Fallback to the Medium image when Samll image not present
		image = getImage(PROPERTY_ADAPTIVE_IMAGE_MEDIUM);
		if (checkContent(image)) {
			if(valueMap.containsKey(PROPERTY_MEDIUM_IMAGE_TRANSPARENT)) {
				this.transparent = true;
			}
			if(valueMap.containsKey(PROPERTY_MEDIUM_IMAGE_DAM)){
				return getAdaptiveImagePath("dsys-flexImg", image);
			}
			return getAdaptiveImagePath(getMediumTransformName(), image);
		}
	// ------------ Fallback to the Large image when Medium image not present
		image = getImage(PROPERTY_ADAPTIVE_IMAGE_LARGE);
		if (checkContent(image)) {
			if(valueMap.containsKey(PROPERTY_LARGE_IMAGE_TRANSPARENT)) {
				this.transparent = true;
			}
			if(valueMap.containsKey(PROPERTY_LARGE_IMAGE_DAM)){
				return getAdaptiveImagePath("dsys-flexImg", image);
			}
			return getAdaptiveImagePath(getLargeTransformName(), image);
		}

		return null;
	}

	public String getMediumImagePath() {
		Image image;

	   image = getImage(PROPERTY_ADAPTIVE_IMAGE_MEDIUM);
		if (checkContent(image)) {
			if(valueMap.containsKey(PROPERTY_MEDIUM_IMAGE_TRANSPARENT)) {
				this.transparent = true;
			}
			if(valueMap.containsKey(PROPERTY_MEDIUM_IMAGE_DAM)){
				return getAdaptiveImagePath("dsys-flexImg", image);
			}
			return getAdaptiveImagePath(getMediumTransformName(), image);
		}
	// ------------ Fallback to the Large image when Medium image not present
		image = getImage(PROPERTY_ADAPTIVE_IMAGE_LARGE);
		if (checkContent(image)) {
			if(valueMap.containsKey(PROPERTY_LARGE_IMAGE_TRANSPARENT)) {
				this.transparent = true;
			}
			if(valueMap.containsKey(PROPERTY_LARGE_IMAGE_DAM)){
				return getAdaptiveImagePath("dsys-flexImg", image);
			}
			return getAdaptiveImagePath(getLargeTransformName(), image);
		}

		return null;
	}

	public String getLargeImagePath() {
		Image image;
		image = getImage(PROPERTY_ADAPTIVE_IMAGE_LARGE);
		if(valueMap.containsKey(PROPERTY_LARGE_IMAGE_DAM)) {
			if(valueMap.containsKey(PROPERTY_LARGE_IMAGE_TRANSPARENT)) {
				this.transparent = true;
			}
			return getAdaptiveImagePath("dsys-flexImg", image);
		} 
		if (checkContent(image)) {
			if(valueMap.containsKey(PROPERTY_LARGE_IMAGE_TRANSPARENT)) {
				this.transparent = true;
			}
			return getAdaptiveImagePath(getLargeTransformName(), image);
		}
		return null;
	}

	/* Set image source path format */
	private String getAdaptiveImagePath(String transformName, Image image) {
		LOG.debug("in getAdaptiveImagePath");
		if (transformName != null) {
			if(this.transparent) {
				this.transparent = false;
                return String.format(IMAGE_PNG_PATH_FORMAT, image.getPath(), getFileName(image.getFileReference()), transformName);
            }
			return String.format(IMAGE_PATH_FORMAT, image.getPath(), getFileName(image.getFileReference()), transformName);
		}
		return null;
	}

	/* Get image title */
	public String getDisplayPopupTitle(){
		LOG.debug("in getDisplayPopupTitle");
		Resource imageParent = resource.getParent();
		ValueMap vm = imageParent.getValueMap();
		return vm.get(PROPERTY_DISPLAY_POPUP_TITLE_TAG, String.class);
	}

	/* Get image alt value from the dialog and if not present then fetch from the dam properies */
	public String getAlt() {
		LOG.debug("in getAlt");
		if (valueMap.containsKey(PROPERTY_ALT_TAG)) {
			return valueMap.get(PROPERTY_ALT_TAG, String.class);
		} else {
			Resource imageParent = resource.getParent();
			ValueMap vm = imageParent.getValueMap();

			if(vm.get(PROPERTY_ISDECORATIVE_TAG, String.class).equalsIgnoreCase("false")){
				if(vm.get(PROPERTY_DAM_ALT_TAG, String.class).equalsIgnoreCase("true")){
					Image largeImage = this.getImage(PROPERTY_ADAPTIVE_IMAGE_LARGE);
					if (largeImage != null && StringUtils.isNotBlank(largeImage.getFileReference())) {
						Resource imageResource = request.getResourceResolver().getResource(largeImage.getFileReference());
						if (imageResource != null) {
							Asset asset = imageResource.adaptTo(Asset.class);
							if (asset != null) {
								String description = asset.getMetadataValue(DamConstants.DC_DESCRIPTION);
								if (StringUtils.isNotBlank(description)) {
									return description;
								}
								else {
									return "";
								}
							}
						}
					}
				}
				else {
					return vm.get(PROPERTY_ALT_FLEXIBLE_TAG, String.class);
				}
			} 
		}
		return "";
	}	
	/* Get image title from the dialog and if not present then fetch from the dam properies */
	public String getTitle() {
		LOG.debug("in getTitle");
		Resource imageParent = resource.getParent();
		ValueMap vm = imageParent.getValueMap();
		String test = vm.get(PROPERTY_DAM_TITLE_TAG, String.class);
		if(vm.get(PROPERTY_DAM_TITLE_TAG, String.class).equalsIgnoreCase("true")){
			Image largeImage = this.getImage(PROPERTY_ADAPTIVE_IMAGE_LARGE);
			if (largeImage != null && StringUtils.isNotBlank(largeImage.getFileReference())) {
				Resource imageResource = request.getResourceResolver().getResource(largeImage.getFileReference());
				if (imageResource != null) {
					Asset asset = imageResource.adaptTo(Asset.class);
					if (asset != null) {
						String title = asset.getMetadataValue(DamConstants.DC_TITLE);
						if (StringUtils.isNotBlank(title)) {
							return title;
						}
						else {
							return asset.getName().split("\\.")[0];
						}
					}
				}
			}
		}
		else {
			return vm.get(PROPERTY_TITLE_TAG, String.class);
		}
		return "";
	}

	/* Get image alt title from dialog and if not present then fetch from the dam properies */
	public String getLink() {
		LOG.debug("in getLink method of Adaptive Image");
		Resource imageParent = resource.getParent();
		ValueMap vm = imageParent.getValueMap();
		String linkURL = vm.get(PROPERTY_URL_LINK_TAG, String.class);
		if(vm.get(PROPERTY_ISDECORATIVE_TAG, String.class).equalsIgnoreCase("false")){
			if (vm.containsKey(PROPERTY_URL_LINK_TAG)) {
				return linkURL + ".html";
			}
		}
		return "";
	}

	public String getImagePos() {
		LOG.debug("in getImagePos method of Adaptive Image");
		Resource imageParent = resource.getParent();
		ValueMap vm = imageParent.getValueMap();
		if (vm.containsKey(PROPERTY_IMAGE_POSITION_TAG)){
			String imagePosition = vm.get(PROPERTY_IMAGE_POSITION_TAG, String.class);
			this.imagePosition = imagePosition;
			if (this.imagePosition.equalsIgnoreCase("d-block mx-auto")){
				return "d-block-mx-auto";

			}else{
				return imagePosition;
			}
			
		}
	return "";
	}

	/*
	 * This method checks if the image has content or not
	 */
	private boolean checkContent(Image image) {
		LOG.debug("in check Content method of Adaptive Image");
		if (image == null) {
			return false;
		}

		try {
			if (image != null && image.hasContent()) {
				return true;
			}
			return false;
		} catch (Exception ex) {
			LOG.error("Exception occured while checking the Image content", ex);
			return false;
		}
	}

	public String getSmall() throws RepositoryException {
		return getSmallImagePath();
	}

	public String getMedium() throws RepositoryException {
		return getMediumImagePath();
	}

	public String getLarge() throws RepositoryException {
		LOG.debug("in get large method method of Adaptive Image");
		return getLargeImagePath();
	}

	public String getSmallTransformName() {
		return smallTransformName;
	}

	public void setSmallTransformName(String smallTransformName) {
		this.smallTransformName = smallTransformName;
	}

	public String getMediumTransformName() {
		return mediumTransformName;
	}

	public void setMediumTransformName(String mediumTransformName) {
		this.mediumTransformName = mediumTransformName;
	}

	public String getLargeTransformName() {
		return largeTransformName;
	}

	public void setLargeTransformName(String largeTransformName) {
		this.largeTransformName = largeTransformName;
	}

	public boolean getLazyLoad() {
		return lazyLoad;
	}

	public void setLazyLoad(Boolean lazyLoad) {
		this.lazyLoad = lazyLoad;
	}

}