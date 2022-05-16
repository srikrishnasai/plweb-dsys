package pl.web.dsys.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;

import pl.web.dsys.core.pojos.LinkPojo;
import pl.web.dsys.core.utils.AuthUtil;
import pl.web.dsys.core.utils.CommonUtils;
import pl.web.dsys.core.utils.SharedContants;

/**
 * This Sling Model returns Header Component's authored dialog values and
 * exports them as json.
 * 
 * @author Krishna
 *
 */
@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = HeaderModel.resourceType)
@Exporter(name = "jackson", extensions = "json")
public class HeaderModel {

	private static final Logger log = LoggerFactory.getLogger(HeaderModel.class);
	public static final String resourceType = "plweb-dsys/components/header/v1/header";
	public static final String ALL_SITES_PATH = "/misc/all-sites";

	@SlingObject
	Resource resource;

	@SlingObject
	SlingHttpServletRequest request;

	@SlingObject
	ResourceResolver resolver;

	@ScriptVariable
	Page currentPage;

	@ValueMapValue
	private String fileReference;

	@ValueMapValue
	private String searchTargetPath;

	@ValueMapValue
	private String allSitesPath;

	@ValueMapValue
	private String topNavHeaderText;

	@ValueMapValue
	private String hideSearch;

	@ValueMapValue
	private String hideTopNav;

	@RequestAttribute
	private String selector;

	@ChildResource
	private List<SecondaryNavLinksModel> primarylinks;
	String group = StringUtils.EMPTY;

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Header Model..");
		allSitesPath = StringUtils.isBlank(allSitesPath) ? ALL_SITES_PATH : allSitesPath;
		group = AuthUtil.getGroupTag(selector);
	}

	public String getFileReference() {
		return fileReference;
	}

	public String getSearchTargetPath() {
		return CommonUtils.resolveUrl(searchTargetPath, resolver, request);
	}

	public List<SecondaryNavLinksModel> getPrimarylinks() {
		return primarylinks;
	}

	public String getAllSitesPath() {
		return allSitesPath;
	}

	public String getTopNavHeaderText() {
		return topNavHeaderText;
	}

	public List<LinkPojo> getAllSites() {
		List<LinkPojo> allSitesList = new ArrayList<LinkPojo>();
		Resource allSitesResource = resolver.getResource(allSitesPath);
		if (allSitesResource != null) {
			Page allSitesRootPage = allSitesResource.adaptTo(Page.class);
			if (allSitesRootPage != null) {
				Iterator<Page> it = allSitesRootPage.listChildren(new PageFilter(request));
				while (it.hasNext()) {
					Page childPage = it.next();
					ValueMap vm = childPage.getContentResource().adaptTo(ValueMap.class);
					if (vm.containsKey("hideInNav") && vm.get("hideInNav", Boolean.class)) {
						log.debug("No Access as its hide in Nav ::{}", childPage.getPath());
						continue;
					}
					String[] authTags = vm.get(SharedContants.PN_AUTH_TAGS, new String[0]);
					boolean isDenyAuth = vm.get(SharedContants.PN_DENY_TAGS, SharedContants.DO_DENY_AUTH_DEFAULT);
					if (!AuthUtil.checkAccess(authTags, isDenyAuth, group.trim())) {
						log.debug("No Access listItem -->::{}", childPage.getPath());
						continue;
					}
					LinkPojo lp = new LinkPojo();
					lp.setLinkText(childPage.getTitle());
					lp.setLinkUrl(getUrl(vm, childPage));
					allSitesList.add(lp);
				}
			}

		}
		return allSitesList;
	}

	private String getUrl(ValueMap vm, Page childPage) {
		String redirectUrl = vm.get("cq:redirectTarget", String.class);
		if (StringUtils.isNotBlank(redirectUrl)) {
			if (CommonUtils.isExternal(redirectUrl)) {
				return redirectUrl;
			} else {
				return CommonUtils.resolveUrl(redirectUrl, resolver, request);
			}
		} else {
			return CommonUtils.resolveUrl(childPage.getPath(), resolver, request);
		}
	}

	public String getHomePagePath() {
		return currentPage.getAbsoluteParent(1).getPath() + "/home.html";
	}

	public String getHideSearch() {
		return hideSearch;
	}

	public String getHideTopNav() {
		return hideTopNav;
	}

}
