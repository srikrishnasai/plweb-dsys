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
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = CircularRatingModel.resourceType)
@Exporter(name = "jackson", extensions = "json")		
public class CircularRatingModel {

	private static final Logger log = LoggerFactory.getLogger(CircularRatingModel.class);
	public static final String resourceType = "plweb-dsys/components/circular-rating/v1/circular-rating";
	
	@SlingObject
	Resource resource;

	@SlingObject
	SlingHttpServletRequest request;

	@ValueMapValue
	private String ratingText;

	@ValueMapValue
	private String ratingTitle;

	@ValueMapValue
	private String circularRatingColor;

	@ValueMapValue
	private String circularRatingSize;


	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Circular-Rating Model..");
	}

	public String getRatingText() {
		return ratingText;
	}

	public String getRatingTitle() {
		return ratingTitle;
	}

	public String getCircularRatingColor() {
		return circularRatingColor;
	}

	public String getCircularRatingSize() {
		return circularRatingSize;
	}

}
