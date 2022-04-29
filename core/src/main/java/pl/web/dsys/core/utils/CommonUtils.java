package pl.web.dsys.core.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class holds all the common methods used across the project.
 * 
 * @author Krishna
 *
 */
public class CommonUtils {

	private static final Logger log = LoggerFactory.getLogger(CommonUtils.class);
	private static final String DEFAULT_SUBSERVICE_USERNAME = "plWebDsysServiceUser";

	public static ResourceResolver getResourceResolver(ResourceResolverFactory resolverFactory, String userName) {
		log.debug("Fetching ResourceResolver for the user ::{}", userName);
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isBlank(userName)) {
			userName = DEFAULT_SUBSERVICE_USERNAME;
		}
		params.put(ResourceResolverFactory.SUBSERVICE, userName);
		try {
			if (null != resolverFactory) {
				return resolverFactory.getServiceResourceResolver(params);
			}
		} catch (LoginException e) {
			log.error("Unable to fetch ResoureResolver ::{}", e.getMessage());
		}
		return null;
	}

	public static boolean isExternal(String url) {
		String regex = "^((https?|ftp|file):)?//[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(url);
		return m.matches();
	}

	public static String resolveUrl(String url, ResourceResolver resolver, SlingHttpServletRequest request) {
		log.debug("CommonUtils resolver is null ::{}", resolver == null);
		log.debug("CommonUtils request is null ::{}", request == null);
		if (StringUtils.isNotBlank(url) && resolver != null && request != null) {
			String modifiedUrl = resolver.map(request, url);
			if (null != FilenameUtils.getExtension(modifiedUrl) && !FilenameUtils.getExtension(modifiedUrl).isEmpty()) {
				return modifiedUrl;
			} else {
				return modifiedUrl + ".html";
			}
		}
		return StringUtils.EMPTY;
	}

}
