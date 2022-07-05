package pl.web.dsys.core.helpers;

import javax.annotation.PostConstruct;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ImageTransformHelperUse {
	private static final Logger LOG = LoggerFactory.getLogger(ImageTransformHelperUse.class);
	private static final String DEFAULT_SUFFIX = "w";
	private static final String VERTICAL_SUFFIX = "h";

	private final ImageTransformsMap transformsMapHelper = new ImageTransformsMap();

	private Node currNode;
	private Node imgNode;

	private String largeTransform;
	private String mediumTransform;
	private String smallTransform;

	private void setLargeTransform(String largeTransform) {
		this.largeTransform = largeTransform;
	}

	private void setMediumTransform(String mediumTransform) {
		this.mediumTransform = mediumTransform;
	}

	private void setSmallTransform(String smallTransform) {
		this.smallTransform = smallTransform;
	}

	private void setCurrNode(Node currNode) {
		this.currNode = currNode;
	}

	private void setImgNode(Node imgNode) {
		this.imgNode = imgNode;
	}

	@SlingObject
	SlingHttpServletRequest request;

	@SlingObject
	Resource resource;

	ValueMap valueMap;

	@PostConstruct
	public void activate() {
		try {
			valueMap = resource.getValueMap();
			ImageTransformsMap tmHelper = this.transformsMapHelper;

			String largeTransform = valueMap.get("largeImageTransform", tmHelper.TRANFORM_LARGE_HIGH);
			String mediumTransform = valueMap.get("mediumImageTransform", tmHelper.TRANFORM_MEDIUM_HIGH);
			String smallTransform = valueMap.get("smallImageTransform", tmHelper.TRANFORM_SMALL_HIGH);

			largeTransform = tmHelper.getTransformName(largeTransform);
			mediumTransform = tmHelper.getTransformName(mediumTransform);
			smallTransform = tmHelper.getTransformName(smallTransform);

			String largeSuffix = DEFAULT_SUFFIX;
			String mediumSuffix = DEFAULT_SUFFIX;
			String smallSuffix = DEFAULT_SUFFIX;

			this.setCurrNode(resource.adaptTo(Node.class));
			if (this.currNode.hasNode("image")) {
				this.setImgNode(this.currNode.getNode("image"));
				if (this.imgNode != null) {
					if (this.checkVerticalByNode("large")) {
						largeSuffix = VERTICAL_SUFFIX;
					}
					if (this.checkVerticalByNode("medium")) {
						mediumSuffix = VERTICAL_SUFFIX;
					}
					if (this.checkVerticalByNode("small")) {
						smallSuffix = VERTICAL_SUFFIX;
					}
				}
			}

			this.setLargeTransform(largeTransform + largeSuffix);
			this.setMediumTransform(mediumTransform + mediumSuffix);
			this.setSmallTransform(smallTransform + smallSuffix);
		} catch (RepositoryException e) {
			LOG.error("Error while activation", e);
		}
	}

	private Integer getInt(String strNum) {
		return Integer.parseInt(strNum);
	}

	private boolean checkIsVertical(String imgCrop, String imgRotate) {
		try {
			if (imgCrop != null) {
				String[] cropArr = imgCrop.split(",");
				long ratio = (this.getInt(cropArr[2]) - this.getInt(cropArr[0]))
						/ (this.getInt(cropArr[3]) - this.getInt(cropArr[1]));
				return (imgRotate == "270" || imgRotate == "90") ? ratio >= 1 : ratio < 1;
			}
			return false;
		} catch (Exception e) {
			LOG.error("Error in checkIsVertical method", e);
			return false;
		}
	}

	private boolean checkVerticalByNode(String nodeName) {
		try {
			if (this.imgNode.hasNode(nodeName)) {
				Node node = this.imgNode.getNode(nodeName);
				if (node.hasProperty("imageCrop")) {
					String imgRotate = node.hasProperty("imageRotate") ? node.getProperty("imageRotate").getString()
							: "0";
					String imgCrop = node.getProperty("imageCrop").getString();
					return this.checkIsVertical(imgCrop, imgRotate);
				}
			}
			return false;
		} catch (RepositoryException e) {
			LOG.error("Error in checkVerticalByNode method", e);
			return false;
		}
	}

	public String getLargeTransform() {
		return this.largeTransform;
	}

	public String getMediumTransform() {
		return this.mediumTransform;
	}

	public String getSmallTransform() {
		return this.smallTransform;
	}

}