package pl.web.dsys.core.helpers;

import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.web.dsys.core.utils.CommonUtils;

@Model(adaptables = { Resource.class,
        SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LinkHelpers {

    Logger logger = LoggerFactory.getLogger(LinkHelpers.class);

    @SlingObject
    private Resource resource;

    @SlingObject
    private ResourceResolver resolver;

    @SlingObject
    private SlingHttpServletRequest request;

    @RequestAttribute
    private String url;
    private String linkURL = StringUtils.EMPTY;

    @PostConstruct
    protected void init() {
        if (url != null && !url.isEmpty()) {
            linkURL = checkLinkFormat(url);
        }

    }

    /**
     * 
     * @param link is a received url from component/resource.
     * @return formattedLink returns formatted url link after validating.
     */
    private String checkLinkFormat(String link) {
        if (CommonUtils.isExternal(link.toLowerCase())) {
            return link;
        }
        return CommonUtils.resolveUrl(link, resolver, request);

    }

    public String getLinkURL() {
        return linkURL;
    }
}