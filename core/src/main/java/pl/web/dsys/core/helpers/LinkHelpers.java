package pl.web.dsys.core.helpers;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = { Resource.class,
        SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LinkHelpers {

    Logger logger = LoggerFactory.getLogger(LinkHelpers.class);

    @Inject
    private String url;
    private String linkURL;

    @PostConstruct
    protected void init() {
        if (url != null && !url.isEmpty()) {
            linkURL = checkLinkFormat(url);
        }
    }

    /**
     * 
     * @param link is a received url
     * @return formattedLink returns formatted url link.
     */
    private String checkLinkFormat(String link) {
        String formattedLink = "";
        if (null != link && !link.isEmpty()) {
            if (link.contains(".com") || link.contains("www") || link.contains(".html")) {
                formattedLink = link;
            } else {
                formattedLink = link + ".html";
            }

        }
        return formattedLink;

    }

    public String getLinkURL() {
        return linkURL;
    }
}