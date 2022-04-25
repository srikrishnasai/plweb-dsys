package pl.web.dsys.core.models;

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
 * This Sling Model returns Secondary Nav Column's authored dialog values and
 * exports them as json.
 * 
 * @author Krishna
 *
 */
@Model(adaptables = { Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SecondaryNavColumnModel {

	private static final Logger log = LoggerFactory.getLogger(SecondaryNavColumnModel.class);

	@SlingObject
	Resource resource;

	@ValueMapValue
	private String navType;
	
	@ChildResource
	private SecondaryNavOverviewModel overview;
	
	@ChildResource
	private SecondaryMultilinksModel multilinks;
	
	@ChildResource
	private SecondaryNavMarketingTileModel marketingtile;

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Secondary Nav Column Model..");
	}

	public String getNavType() {
		return navType;
	}

	public SecondaryNavOverviewModel getOverview() {
		return overview;
	}

	public SecondaryMultilinksModel getMultilinks() {
		return multilinks;
	}

	public SecondaryNavMarketingTileModel getMarketingtile() {
		return marketingtile;
	}

}
