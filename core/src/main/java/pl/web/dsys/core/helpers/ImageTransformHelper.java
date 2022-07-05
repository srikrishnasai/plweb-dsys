package pl.web.dsys.core.helpers;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ImageTransformHelper {

	private static final Logger log = LoggerFactory.getLogger(ImageTransformHelper.class);

	@RequestAttribute
	String transform;

	@RequestAttribute
	String imagePath;

	@RequestAttribute
	String nodeName;

	@RequestAttribute
	String transparent;

	@Inject
	Resource resource;

	@Inject
	ResourceResolver resourceResolver;

	private String transformedImageUrl;
	private static final String IMAGE_PATH_FORMAT = "%s.img.transform/%s/image.jpg";
	private static final String IMAGE_PATH_FORMAT_TRANSPARENT = "%s.img.transform/%s/image.png";
	private static final String IMAGE_NODE_PATH_FORMAT = "%s.%s";

	@PostConstruct
	public void activate() throws Exception {
		String path = imagePath;

		if (null != nodeName) {
			String imageNodePath = resource.getPath() + "/" + nodeName;
			Resource imageResource = resourceResolver.getResource(imageNodePath);
			if (null != imageResource) {
				String fileReference = imageResource.getValueMap().get("fileReference", StringUtils.EMPTY);
				if (fileReference.toLowerCase().contains(".svg")) {
					this.transformedImageUrl = fileReference; // Returns origin image path if it is svg file
				} else {
					String imageName = fileReference.substring(fileReference.lastIndexOf("/") + 1);
					path = String.format(IMAGE_NODE_PATH_FORMAT, imageNodePath, imageName);
				}
			}
		}

		// ------------ Returns path based on the image is transparent or not
		if (null != transform && null != path) {
			if (null != transparent && transparent.equalsIgnoreCase("true")) {
				this.transformedImageUrl = String.format(IMAGE_PATH_FORMAT_TRANSPARENT, path, transform);
			} else {
				this.transformedImageUrl = String.format(IMAGE_PATH_FORMAT, path, transform);
			}
		}
	}

	public String getTransformedImageUrl() {
		return transformedImageUrl;
	}

	public void setTransformedImageUrl(String transformedImageUrl) {
		this.transformedImageUrl = transformedImageUrl;
	}
}
