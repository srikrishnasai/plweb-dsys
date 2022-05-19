package pl.web.dsys.core.utils;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.api.WCMMode;

public class AuthUtil {

	private static final Logger log = LoggerFactory.getLogger(AuthUtil.class);

	/**
	 * Fetch the Agency Tag from request.
	 * 
	 * @param request
	 * @return
	 */
	public static String getAgencyTag(SlingHttpServletRequest request) {
		String group = SharedContants.DEFAULT_PUBLIC_AGENCY;
		String[] selectors = request.getRequestPathInfo().getSelectors();
		if (selectors != null && selectors.length > 0) {
			log.debug("Selectors from Request are ::{}", Arrays.asList(selectors));
			for (String selector : selectors) {
				log.debug("Selector ::{}", selector);
				if (StringUtils.startsWithIgnoreCase(selector, SharedContants.AGENCY_SELECTOR_PREFIX)) {
					group = StringUtils.substringAfter(selector, SharedContants.AGENCY_SELECTOR_PREFIX);
					log.debug("Group ::{}", group);
					break;
				}
			}
		}
		return SharedContants.AGENCY_PREFIX + group.toUpperCase();
	}

	public static boolean checkAccess(SlingHttpServletRequest request, Resource res) {
		String group = null;
		String[] authTags = null;
		boolean isDenyAuth = SharedContants.DO_DENY_AUTH_DEFAULT;

		group = getAgencyTag(request);
		ResourceResolver resourceResolver = request.getResourceResolver();
		ValueMap vm = getVm(resourceResolver, res);
		if (null != vm) {
			authTags = vm.get(SharedContants.PN_AUTH_TAGS, new String[0]);
			log.debug("Auth Tags of the resource from Auth Util ::{}", Arrays.asList(authTags));
			isDenyAuth = vm.get(SharedContants.PN_DENY_TAGS, SharedContants.DO_DENY_AUTH_DEFAULT);
			log.debug("Deny Tag for resource from Auth Util ::{}", isDenyAuth);
		}
		return checkAccess(authTags, isDenyAuth, group);
	}

	/**
	 * 
	 * @param authTags
	 * @param denyTags
	 * @param group
	 * @return
	 */
	public static boolean checkAccess(String[] authTags, boolean isDenyTags, String group) {
		boolean access = false;
		if (authTags != null && authTags.length > 0) {
			for (String authTag : authTags) {
				log.debug("AuthTag :: {} and Group ::{}", authTag, group);
				if (authTag.equals(group)) {
					access = true;
					break;
				}
			}
		} else {
			log.debug("authTags & denyTags null or empty");
			access = true;
		}
		log.debug("Access to the resource ::{}", (isDenyTags ? !access : access));
		return isDenyTags ? !access : access;
	}

	/**
	 * To check whether wcmmode is edit or preview.
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isAuthorOrPreview(SlingHttpServletRequest request) {
		WCMMode wcmMode = WCMMode.fromRequest(request);
		if (wcmMode == WCMMode.EDIT || wcmMode == WCMMode.PREVIEW || wcmMode == WCMMode.READ_ONLY) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	/**
	 * 
	 * @param resourceResolver
	 * @param res
	 * @return
	 */
	public static ValueMap getVm(ResourceResolver resourceResolver, Resource res) {
		ValueMap vm = null;
		if (resourceResolver.isResourceType(res, DamConstants.NT_DAM_ASSET)) {
			log.debug("Asset Resource ::{}", res.getPath());
			Resource assetRes = resourceResolver
					.getResource(res.getPath() + "/" + JcrConstants.JCR_CONTENT + "/" + DamConstants.METADATA_FOLDER);
			if (null != assetRes) {
				vm = assetRes.getValueMap();
			}
		} else {
			Resource pageRes = resourceResolver.getResource(res.getPath() + "/" + JcrConstants.JCR_CONTENT);
			if (null != pageRes) {
				vm = pageRes.getValueMap();
			}
		}
		return vm;
	}
}
