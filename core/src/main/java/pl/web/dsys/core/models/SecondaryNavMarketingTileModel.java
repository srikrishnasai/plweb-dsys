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
 * This Sling Model returns Secondary Nav Multilinks authored dialog values and
 * exports them as json.
 * 
 * @author Krishna
 *
 */
@Model(adaptables = { Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SecondaryNavMarketingTileModel {

	private static final Logger log = LoggerFactory.getLogger(SecondaryNavMarketingTileModel.class);

	@SlingObject
	Resource resource;

	@ValueMapValue
	private String fileReference;

	@ValueMapValue
	private String description;

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Secondary Nav MarketingTile Type Model..");
	}

	public String getFileReference() {
		return fileReference;
	}

	public String getDescription() {
		return description;
	}

}
