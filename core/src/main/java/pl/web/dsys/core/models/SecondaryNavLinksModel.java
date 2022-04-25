package pl.web.dsys.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
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
public class SecondaryNavLinksModel {

	private static final Logger log = LoggerFactory.getLogger(SecondaryNavLinksModel.class);

	@SlingObject
	Resource resource;

	@ValueMapValue
	private String linkText;

	@ValueMapValue
	private String targetPath;

	@ValueMapValue
	private String linkTarget;

	@ValueMapValue
	private String description;

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Secondary Nav Links Model..");
	}

	public String getLinkText() {
		return linkText;
	}

	public String getTargetPath() {
		return targetPath;
	}

	public String getLinkTarget() {
		return linkTarget;
	}

	public String getDescription() {
		return description;
	}

}
