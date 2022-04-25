package pl.web.dsys.core.models;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
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
public class SecondaryMultilinksModel {

	private static final Logger log = LoggerFactory.getLogger(SecondaryMultilinksModel.class);

	@SlingObject
	Resource resource;

	@ChildResource
	private List<SecondaryNavLinksModel> links;

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Secondary Nav Multilinks Type Model..");
	}

	public List<SecondaryNavLinksModel> getLinks() {
		return links;
	}

}
