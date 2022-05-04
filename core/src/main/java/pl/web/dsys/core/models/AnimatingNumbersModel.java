package pl.web.dsys.core.models;


import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = AnimatingNumbersModel.resourceType)
@Exporter(name = "jackson", extensions = "json")
public class AnimatingNumbersModel {

	private static final Logger log = LoggerFactory.getLogger(AnimatingNumbersModel.class);
	public static final String resourceType = "plweb-dsys/components/animating-numbers/v1/animating-numbers";
	
	@SlingObject
	Resource resource;

	@SlingObject
	SlingHttpServletRequest request;

	@ValueMapValue
	private String animatingNumbersStartValue;

	@ValueMapValue
	private String animatingNumbersEndValue;

	@ValueMapValue
	private String animatingNumbersIncrementValue;

	@ValueMapValue
	private String animatingNumbersAnimationInterval;

	@ValueMapValue
	private String animatingNumbersPrefix;

	@ValueMapValue
	private String animatingNumbersSuffix;

	@ValueMapValue
	private String animatingNumbersLabel;

	@ValueMapValue
	private String animatingNumbersColor;


	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Animating Numbers Model..");
	}

	public String getAnimatingNumbersStartValue() {
		return animatingNumbersStartValue;
	}

	public String getAnimatingNumbersEndValue() {
		return animatingNumbersEndValue;
	}

	public String getAnimatingNumbersIncrementValue() {
		return animatingNumbersIncrementValue;
	}

	public String getAnimatingNumbersAnimationInterval() {
		return animatingNumbersAnimationInterval;
	}

	public String getAnimatingNumbersPrefix() {
		return animatingNumbersPrefix;
	}

	public String getAnimatingNumbersSuffix() {
		return animatingNumbersSuffix;
	}

	public String getAnimatingNumbersLabel() {
		return animatingNumbersLabel;
	}

	public String getAnimatingNumbersColor() {
		return animatingNumbersColor;
	}

}
