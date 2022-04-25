package pl.web.dsys.core.models;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Model takes url as parameter and resolves and returns shortened url for
 * the same.
 * 
 * @author Krishna
 *
 */
@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json")
public class LinkHelperModel {

	private static final Logger log = LoggerFactory.getLogger(LinkHelperModel.class);

	@SlingObject
	private Resource resource;

	@SlingObject
	private ResourceResolver resolver;

	@SlingObject
	private SlingHttpServletRequest request;

	@RequestAttribute
	private String originalLink;

	String modifiedLink = StringUtils.EMPTY;

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of LinkHelper Model.. ::{}", originalLink);
		if (StringUtils.isNotBlank(originalLink)) {
			modifiedLink = resolver.map(request, originalLink);
			if (!StringUtils.endsWithIgnoreCase(originalLink, ".html")) {
				modifiedLink = modifiedLink + ".html";
			}
			log.debug("Modified Link ::{}", modifiedLink);
		}
	}

	public String getOriginalLink() {
		return originalLink;
	}

	public String getModifiedLink() {
		return modifiedLink;
	}

}
