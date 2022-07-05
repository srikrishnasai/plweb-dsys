package pl.web.dsys.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = FooterModel.resourceType)
public class FooterModel {

	private static final Logger log = LoggerFactory.getLogger(FooterModel.class);
	public static final String resourceType = "plweb-dsys/components/footer/v1/footer";

	@SlingObject
	Resource resource;

	@SlingObject
	SlingHttpServletRequest request;

	@ValueMapValue
	private String imagePath;

	@ValueMapValue
	private String useTransparentImage;

	@ValueMapValue
	private String useOriginalImage;

	@ValueMapValue
	private String section1Title;

	@ValueMapValue
	private String section2Title;

	@ValueMapValue
	private String section3Title;

	@ValueMapValue
	private String copyright;

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of footer Model..");
	}

	public String getImagePath() {
		return imagePath;
	}

	public String getUseTransparentImage() {
		return useTransparentImage;
	}

	public String getUseOriginalImage() {
		return useOriginalImage;
	}

	public String getSection1Title() {
		return section1Title;
	}

	public String getSection2Title() {
		return section2Title;
	}

	public String getSection3Title() {
		return section3Title;
	}

	public String getCopyright() {
		return copyright;
	}

}
