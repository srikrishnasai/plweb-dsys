package pl.web.dsys.core.helpers;

import java.util.Map;
import java.util.HashMap;

public class ImageTransformsMap {

	public static final String TRANFORM_LARGE_VERY_HIGH = "flexImageLargeVeryHigh";
    public static final String TRANFORM_LARGE_HIGH = "flexImageLargeHigh";
	public static final String TRANFORM_LARGE_MEDIUM = "flexImageLargeMedium";
    public static final String TRANFORM_LARGE_LOW = "flexImageLargeLow";
    public static final String TRANFORM_LARGE_VERY_LOW = "flexImageLargeVeryLow";
    public static final String TRANFORM_LARGE_THUMBNAIL_LARGE = "flexImageLargeThumbnailLarge";
    public static final String TRANFORM_LARGE_THUMBNAIL_SMALL = "flexImageLargeThumbnailSmall";

    public static final String TRANFORM_MEDIUM_HIGH = "flexImageMediumHigh";
	public static final String TRANFORM_MEDIUM_MEDIUM = "flexImageMediumMedium";
    public static final String TRANFORM_MEDIUM_LOW = "flexImageMediumLow";
    public static final String TRANFORM_MEDIUM_THUMBNAIL_LARGE = "flexImageMediumThumbnailLarge";
    public static final String TRANFORM_MEDIUM_THUMBNAIL_SMALL = "flexImageMediumThumbnailSmall";
    
	public static final String TRANFORM_SMALL_HIGH = "flexImageSmallHigh";
	public static final String TRANFORM_SMALL_MEDIUM = "flexImageSmallMedium";
    public static final String TRANFORM_SMALL_LOW = "flexImageSmallLow";
    public static final String TRANFORM_SMALL_THUMBNAIL_LARGE = "flexImageSmallThumbnailLarge";
    public static final String TRANFORM_SMALL_THUMBNAIL_SMALL = "flexImageSmallThumbnailSmall";

    private Map transformsMap;

    private void setTransformsMap(Map transformsMap) {
        this.transformsMap = transformsMap;
    }

    public ImageTransformsMap () {
        Map<String, String> tMap = new HashMap<>();

        tMap.put(TRANFORM_LARGE_VERY_HIGH, "dsys-flexImg1440");
        tMap.put(TRANFORM_LARGE_HIGH, "dsys-flexImg1200");
        tMap.put(TRANFORM_LARGE_MEDIUM, "dsys-flexImg992");
        tMap.put(TRANFORM_LARGE_LOW, "dsys-flexImg768");
        tMap.put(TRANFORM_LARGE_VERY_LOW, "dsys-flexImg576");
        tMap.put(TRANFORM_LARGE_THUMBNAIL_LARGE, "dsys-flexImg400");
        tMap.put(TRANFORM_LARGE_THUMBNAIL_SMALL, "dsys-flexImg200");

        tMap.put(TRANFORM_MEDIUM_HIGH, "dsys-flexImg992");
        tMap.put(TRANFORM_MEDIUM_MEDIUM, "dsys-flexImg768");
        tMap.put(TRANFORM_MEDIUM_LOW, "dsys-flexImg580");
        tMap.put(TRANFORM_MEDIUM_THUMBNAIL_LARGE, "dsys-flexImg400");
        tMap.put(TRANFORM_MEDIUM_THUMBNAIL_SMALL, "dsys-flexImg200");

        tMap.put(TRANFORM_SMALL_HIGH, "dsys-flexImg768");
        tMap.put(TRANFORM_SMALL_MEDIUM, "dsys-flexImg580");
        tMap.put(TRANFORM_SMALL_LOW, "dsys-flexImg440");
        tMap.put(TRANFORM_SMALL_THUMBNAIL_LARGE, "dsys-flexImg400");
        tMap.put(TRANFORM_SMALL_THUMBNAIL_SMALL, "dsys-flexImg200");

        this.setTransformsMap(tMap);
    }

    public Map getTransformsMap() {
        return this.transformsMap;
    }

    public String getTransformName(String key) {
        return this.transformsMap.get(key).toString();
    }
    
    public String getTransformNameOrDefault(String key, String defaultValue) {
        return this.transformsMap.getOrDefault(key, defaultValue).toString();
    }

}