package pl.web.dsys.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import pl.web.dsys.core.pojos.CategoryList;
import pl.web.dsys.core.pojos.PageInfo;

/**
 * The NavigationBarModel class is a sling model for returning back a list of
 * child pages for NavigationBar component.
 */
@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NavigationBarModel {

	Logger logger = LoggerFactory.getLogger(NavigationBarModel.class);

	@Inject
	public Resource resource;

	@ValueMapValue
	public String linkTarget;

	@Inject
	private ResourceResolver resourceResolver;

	public Resource childResource;
	public List<PageInfo> pageInfoList;
	public List<CategoryList> categoryList;
	Resource requestResource;
	Iterator<Page> resourceIterator;
	PageManager pageManager;

	@PostConstruct
	protected void init() {
		if (linkTarget != null) {
			requestResource = resourceResolver.getResource(linkTarget);
			pageManager = resourceResolver.adaptTo(PageManager.class);
			Page page = pageManager.getContainingPage(requestResource);
			if (page != null) {
				resourceIterator = page.listChildren();
				// get child pages of current page path
				categoryList = new ArrayList<>();
				resourceIterator.forEachRemaining(resource -> {
					// get current page properties
					CategoryList categorylist = new CategoryList();
					final Resource contentResource = resource.getContentResource();
					final ValueMap pageValueMap = contentResource.getValueMap();
					final String pageTitle = pageValueMap.get(NameConstants.PN_PAGE_TITLE) != null
							? pageValueMap.get(NameConstants.PN_PAGE_TITLE).toString()
							: pageValueMap.get(NameConstants.PN_TITLE).toString();
					final String isHideInNavigation = pageValueMap.get(NameConstants.PN_HIDE_IN_NAV) != null
							? pageValueMap.get(NameConstants.PN_HIDE_IN_NAV).toString()
							: StringUtils.EMPTY;
					categorylist.setCategory(pageTitle);
					categorylist.setPath(resource.getPath());

					// get current page's list of child pages.
					if (null != resource.getPath()) {
						requestResource = resourceResolver.getResource(resource.getPath());
						pageManager = resourceResolver.adaptTo(PageManager.class);
						Page childPage = pageManager.getContainingPage(requestResource);
						if (null != childPage) {
							resourceIterator = childPage.listChildren();
							pageInfoList = new ArrayList<>();
							resourceIterator.forEachRemaining(childResource -> {
								pageManager = resourceResolver.adaptTo(PageManager.class);

								// get child page properties
								final Resource contentResourceChild = childResource.getContentResource();
								final ValueMap pageValueMapChild = contentResourceChild.getValueMap();
								final String pageTitleChild = pageValueMapChild.get(NameConstants.PN_PAGE_TITLE) != null
										? pageValueMapChild.get(NameConstants.PN_PAGE_TITLE).toString()
										: pageValueMapChild.get(NameConstants.PN_TITLE).toString();
								final String isHideInNavigationChild = pageValueMapChild
										.get(NameConstants.PN_HIDE_IN_NAV) != null
												? pageValueMapChild.get(NameConstants.PN_HIDE_IN_NAV).toString()
												: StringUtils.EMPTY;
								PageInfo pageInfo = new PageInfo();
								pageInfo.setPageTile(pageTitleChild);
								pageInfo.setPath(childResource.getPath());

								// add page into list only if its hide in navigation property is not set
								// to true
								if (isHideInNavigationChild.isEmpty()
										|| !isHideInNavigationChild.equalsIgnoreCase("true")) {
									pageInfoList.add(pageInfo);
								}

							});

						}

					}
					categorylist.setPageInfolist(pageInfoList);

					// add page into list only if its hide in navigation property is not set to true
					if (isHideInNavigation.isEmpty() || !isHideInNavigation.equalsIgnoreCase("true")) {
						categoryList.add(categorylist);
					}

				});
			}
		}

	}
}
