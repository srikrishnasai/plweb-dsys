package pl.web.dsys.core.models;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

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

	@ValueMapValue
	private String secondaryNavXfPath;

	@SlingObject
	private ResourceResolver resolver;

	String pageTitle = StringUtils.EMPTY;

	String pageDescription = StringUtils.EMPTY;

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Secondary Nav Links Model..");
		log.debug("Resolver is null ::{}", resolver == null);
		if (StringUtils.isNotBlank(targetPath) && resolver != null) {
			Page page = resolver.getResource(targetPath).adaptTo(Page.class);
			if (page != null) {
				pageTitle = page.getTitle();
				pageDescription = page.getDescription();
			}
		}
	}

	public String getLinkText() {
		// falling back to pagetitle if link text is not authored.
		return linkText != null ? linkText : pageTitle;
	}

	public String getTargetPath() {
		return targetPath;
	}

	public String getLinkTarget() {
		return linkTarget != null ? "_blank" : "_self";
	}

	public String getDescription() {
		// falling back to page description if description in dialog is not authored.
		return description != null ? description : pageDescription;
	}

	public String getSecondaryNavXfPath() {
		if (StringUtils.isNotBlank(secondaryNavXfPath)) {
			return secondaryNavXfPath + "/" + JcrConstants.JCR_CONTENT + "/root";
		}
		return secondaryNavXfPath;
	}

}
