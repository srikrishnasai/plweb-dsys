package pl.web.dsys.core.models;

import java.util.Enumeration;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = IframeModel.resourceType)
@Exporter(name = "jackson", extensions = "json")
public class IframeModel {

	private static final Logger log = LoggerFactory.getLogger(IframeModel.class);
	public static final String resourceType = "plweb-dsys/components/Iframe/v1/Iframe";
	public static final String IFRM_EXTERNAPP = "IFRM_EXTERNAPP";
	public static final String PROP_OVERRIDE_ID = "overrideId";

	private String width = "100%";
	private String height = "100%";
	private String iFrameId = null;

	@SlingObject
	Resource resource;

	@SlingObject
	SlingHttpServletRequest request;

	@SlingObject
	private ResourceResolver resolver;

	@ValueMapValue
	private String html;

	@ValueMapValue
	private String appUrl;

	@ValueMapValue
	private String removeWH;

	@ValueMapValue
	private String scrollbars;

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Iframe Model..::{}");
		ValueMap vm = resource.getValueMap();
		iFrameId = vm.get(PROP_OVERRIDE_ID, IFRM_EXTERNAPP);
		String oRegExp = "[<>,<,>,(),),(,)]";
		StringBuffer buffer = new StringBuffer();
		int count = 0;
		for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
			if (count == 0)
				buffer.append("&");
			String key = (String) e.nextElement();
			String value = request.getParameter(key);
			value = value.replaceAll(oRegExp, "");
			buffer.append(key).append("=").append(value).append("&");
			count++;
		}

		if (buffer.length() > 0) {
			if (StringUtils.contains(appUrl, "?"))
				appUrl = appUrl + "&" + buffer.toString();
			else
				appUrl = appUrl + "?" + buffer.toString();
		}
		appUrl = StringUtils.removeEnd(appUrl, "&");

		log.debug("App Url ::{}", appUrl);
		log.debug("Html ::{}", html);

	}

	public String getHtml() {
		return html;
	}

	public boolean isShowHtml() {
		return StringUtils.isNotBlank(StringUtils.stripToNull(html));
	}

	/**
	 * 
	 * @return
	 */
	public boolean showFrame() {
		return StringUtils.isBlank(StringUtils.stripToNull(html));
	}

	public String getAppUrl() {
		if (StringUtils.isBlank(appUrl)) {
			return "#";
		}
		return appUrl;
	}

	public String getRemoveWH() {
		if (StringUtils.isBlank(removeWH)) {
			return "width=\"" + width + "\" height=\"" + height + "\"";
		}
		return null;
	}

	public String getScrollbars() {
		if (StringUtils.isBlank(scrollbars)) {
			return "scrolling=\"no\"";
		}
		return null;
	}

	public String getIFrameId() {
		return iFrameId;
	}

}
