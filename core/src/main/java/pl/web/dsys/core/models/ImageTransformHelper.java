package pl.web.dsys.core.models;

import javax.annotation.PostConstruct;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;


@Model(adaptables = {Resource.class, SlingHttpServletRequest.class},
defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ImageTransformHelper {
    
    @RequestAttribute
    String transform;
    @RequestAttribute
    String imagePath;
    @RequestAttribute
    String transparent;

    private String transformedImageUrl;
    private static final String IMAGE_PATH_FORMAT = "%s.img.transform/%s/image.jpg";
    private static final String IMAGE_PATH_FORMAT_TRANSPARENT = "%s.img.transform/%s/image.png";
    
    @PostConstruct 
    public void activate() throws Exception {
        
        // ------------ Returns path based on the image is transparent or not
        if( null != transform && null != imagePath) {
            if(null != transparent && transparent.equalsIgnoreCase("true")){
                this.transformedImageUrl = String.format(IMAGE_PATH_FORMAT_TRANSPARENT, imagePath, transform);
            }    
            else{
                this.transformedImageUrl = String.format(IMAGE_PATH_FORMAT, imagePath, transform);
            }     
        }
    }

    public String getTransformedImageUrl() {
        return transformedImageUrl;
    }

    public void setTransformedImageUrl(String transformedImageUrl) {
        this.transformedImageUrl = transformedImageUrl;
    }
}
