package pl.web.dsys.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = PersonCardModel.resourceType)
@Exporter(name = "jackson", extensions = "json")		
public class PersonCardModel {

	private static final Logger log = LoggerFactory.getLogger(PersonCardModel.class);
	public static final String resourceType = "plweb-dsys/components/person-card/v1/person-card";
	
	@SlingObject
	Resource resource;

	@SlingObject
	SlingHttpServletRequest request;

	@ValueMapValue
	private String category;

	@ValueMapValue
	private String color;

	@ValueMapValue
	private String useOriginalImage;

	@ValueMapValue
	private String useTransparentImage;

	@ValueMapValue
	private String name;

	@ValueMapValue
	private String position;

	@ValueMapValue
	private String desc;

	@ValueMapValue
	private String bgColor;

	@ValueMapValue
	private String mail;

	@ValueMapValue
	private String telephone;

	@ValueMapValue
	private String linkedin;


	Resource child;
	ValueMap valueMap;
	private String fileReference;

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Person Card Model..");	
		child = resource;
		child = child.getChild("image");
		valueMap = child.getValueMap();
		fileReference = valueMap.get("fileReference", String.class);
	}

	public String getCategory() {
		return category;
	}

	public String getColor() {
		return color;
	}

	public String getFileReference() {
		return fileReference;
	}

	public String getUseOriginalImage() {
		return useOriginalImage;
	}

	public String getUseTransparentImage() {
		return useTransparentImage;
	}

	public String getName() {
		return name;
	}

	public String getPosition() {
		return position;
	}

	public String getDesc() {
		return desc;
	}

	public String getBgColor() {
		return bgColor;
	}

	public String getMail() {
		return mail;
	}

	public String getTelephone() {
		return telephone;
	}

	public String getLinkedin() {
		return linkedin;
	}

}
