package pl.web.dsys.core.helpers;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.regex.*;
import org.apache.commons.io.FilenameUtils;



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
     * @param link is a received url from component/resource.
     * @return formattedLink returns formatted url link after validating.
     */
    private String checkLinkFormat(String link) {
        String formattedLink = "";
        if (null != link && !link.isEmpty()) {
            if(isExternalURL(link.toLowerCase())){
                return formattedLink = link;                
            }
            else{
             if (null != FilenameUtils.getExtension(link) && !FilenameUtils.getExtension(link).isEmpty()){
               return formattedLink = link;
            } else {
               return formattedLink = link + ".html";
            }

            }

           

        }
        return formattedLink;

    }

     // Function to validate external URL using regular expression
    public static boolean
    isExternalURL(String url)
    {
        // Regex to check valid URL
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        // Compile the ReGex
        Pattern p = Pattern.compile(regex);
        // Find match between given string
        // and regular expression
        // using Pattern.matcher()
        Matcher m = p.matcher(url);
 
        // Return if the string
        // matched the ReGex
        return m.matches();
    }


    public String getLinkURL() {
        return linkURL;
    }
}