package pl.web.dsys.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

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
    Iterator<Resource> resourceIterator;
    PageManager pageManager;

    @PostConstruct
    protected void init() {
        if (linkTarget != null) {
            requestResource = resourceResolver.getResource(linkTarget);
            if (requestResource != null) {
                resourceIterator = requestResource.listChildren();

                // get child pages of current page path
                categoryList = new ArrayList<>();
                resourceIterator.forEachRemaining(resource -> {
                    pageManager = resourceResolver.adaptTo(PageManager.class);
                    if (pageManager != null) {
                        Page page = pageManager.getContainingPage(resource);
                        if (page != null) {

                            // get current page properties
                            CategoryList categorylist = new CategoryList();
                            final Resource contentResource = page.getContentResource();
                            final ValueMap pageValueMap = contentResource.getValueMap();
                            final String pageTitle = pageValueMap.get(NameConstants.PN_PAGE_TITLE) != null
                                    ? pageValueMap.get(NameConstants.PN_PAGE_TITLE).toString()
                                    : pageValueMap.get(NameConstants.PN_TITLE).toString();
                            final String isHideInNavigation = pageValueMap.get(NameConstants.PN_HIDE_IN_NAV) != null
                                    ? pageValueMap.get(NameConstants.PN_HIDE_IN_NAV).toString()
                                    : StringUtils.EMPTY;
                            categorylist.setCategory(pageTitle);
                            categorylist.setPath(page.getPath());

                            // get current page's list of child pages.
                            if (null != page.getPath()) {
                                requestResource = resourceResolver.getResource(page.getPath());
                                if (null != requestResource) {
                                    resourceIterator = requestResource.listChildren();
                                    pageInfoList = new ArrayList<>();
                                    resourceIterator.forEachRemaining(childResource -> {
                                        pageManager = resourceResolver.adaptTo(PageManager.class);
                                        if (null != pageManager) {
                                            Page childPage = pageManager.getContainingPage(childResource);
                                            if (null != childPage) {

                                                // get child page properties
                                                final Resource contentResourceChild = childPage.getContentResource();
                                                final ValueMap pageValueMapChild = contentResourceChild.getValueMap();
                                                final String pageTitleChild = pageValueMapChild
                                                        .get(NameConstants.PN_PAGE_TITLE) != null
                                                                ? pageValueMapChild.get(NameConstants.PN_PAGE_TITLE)
                                                                        .toString()
                                                                : pageValueMapChild.get(NameConstants.PN_TITLE)
                                                                        .toString();
                                                final String isHideInNavigationChild = pageValueMapChild
                                                        .get(NameConstants.PN_HIDE_IN_NAV) != null
                                                                ? pageValueMapChild.get(NameConstants.PN_HIDE_IN_NAV)
                                                                        .toString()
                                                                : StringUtils.EMPTY;
                                                PageInfo pageInfo = new PageInfo();
                                                pageInfo.setPageTile(pageTitleChild);
                                                pageInfo.setPath(childPage.getPath());

                                                // add page into list only if its hide in navigation property is not set
                                                // to true
                                                if (isHideInNavigationChild.isEmpty()
                                                        || !isHideInNavigationChild.equalsIgnoreCase("true")) {
                                                    pageInfoList.add(pageInfo);
                                                }

                                            }

                                        }

                                    });

                                }

                            }
                            categorylist.setPageInfolist(pageInfoList);

                            // add page into list only if its hide in navigation property is not set to true
                            if (isHideInNavigation.isEmpty() || !isHideInNavigation.equalsIgnoreCase("true")) {
                                categoryList.add(categorylist);
                            }

                        }
                    }
                });
            }
        }

    }
}
