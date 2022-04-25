package pl.web.dsys.core.models;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Sling Model returns Secondary Nav Overview Type authored dialog values
 * and exports them as json.
 * 
 * @author Krishna
 *
 */
@Model(adaptables = { Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SecondaryNavOverviewModel {

	private static final Logger log = LoggerFactory.getLogger(SecondaryNavOverviewModel.class);

	@SlingObject
	Resource resource;

	@ValueMapValue
	private String header;

	@ValueMapValue
	private String primaryLinkText;

	@ValueMapValue
	private String primaryLinkTargetPath;

	@ValueMapValue
	private String privateLinkTarget;

	@ValueMapValue
	private String overviewDescription;

	@ValueMapValue
	private String telephone;

	@ChildResource
	private List<SecondaryNavLinksModel> secondaryLinks;

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Secondary Nav Overview Type Model..");
	}

	public String getHeader() {
		return header;
	}

	public String getPrimaryLinkText() {
		return primaryLinkText;
	}

	public String getPrimaryLinkTargetPath() {
		return primaryLinkTargetPath;
	}

	public String getPrivateLinkTarget() {
		return privateLinkTarget;
	}

	public String getOverviewDescription() {
		return overviewDescription;
	}

	public String getTelephone() {
		return telephone;
	}

	public List<SecondaryNavLinksModel> getSecondaryLinks() {
		return secondaryLinks;
	}

}
