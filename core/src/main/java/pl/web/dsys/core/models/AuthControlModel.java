package pl.web.dsys.core.models;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import pl.web.dsys.core.utils.AuthUtil;
import pl.web.dsys.core.utils.SharedContants;

@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json")
public class AuthControlModel {

	private static final Logger log = LoggerFactory.getLogger(AuthControlModel.class);

	@SlingObject
	private ResourceResolver resolver;

	@SlingObject
	private Resource resource;

	@SlingObject
	private SlingHttpServletRequest request;

	@RequestAttribute
	private String path;

	@ScriptVariable
	private Page currentPage;

	@ScriptVariable
	private PageManager pageManager;

	protected boolean compAuth;
	protected String[] authtags;
	protected boolean isDenyTags;
	protected boolean isItemAuth;
	protected boolean hasComponentAccess = true;
	ValueMap vm;

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Auth Control Model..");
		log.debug("PageManager is null ::{}", pageManager == null);
		if (null != resource) {
			vm = resource.getValueMap();
			compAuth = vm.get(SharedContants.PN_COMPONENT_AUTH, SharedContants.DO_COMPONENT_AUTH_DEFAULT);
			isItemAuth = vm.get(SharedContants.PN_AUTH_BY_ITEM, SharedContants.DO_COMPONENT_AUTH_DEFAULT);
			log.debug("Is Comp Auth for component ::{}", compAuth);
			if (compAuth) {
				authtags = vm.get(SharedContants.PN_AUTH_TAGS, new String[0]);
				log.debug("Auth tags for the component ::{}", Arrays.asList(authtags));
				isDenyTags = vm.get(SharedContants.PN_DENY_TAGS, SharedContants.DO_DENY_AUTH_DEFAULT);
				log.debug("is Deny Tags check for the component ::{}", isDenyTags);
				String group = AuthUtil.getAgencyTag(request);
				if (AuthUtil.isAuthorOrPreview(request)) {
					hasComponentAccess = AuthUtil.isAuthorOrPreview(request);
				} else {
					hasComponentAccess = AuthUtil.checkAccess(authtags, isDenyTags, group);
				}
				log.debug("Has component access ::{}", hasComponentAccess);
			}
		}

	}

	public boolean isCompAuth() {
		return compAuth;
	}

	public String[] getAuthtags() {
		return authtags;
	}

	public boolean isDenyTags() {
		return isDenyTags;
	}

	public boolean isItemAuth() {
		return isItemAuth;
	}

	public boolean isHasComponentAccess() {
		return hasComponentAccess;
	}

	public boolean isResourceAuthorized() {
		boolean access = Boolean.TRUE;
		if (isItemAuth) {
			log.debug("Inside is Resource Authorized method ::{}", path);
			Resource itemResource = resolver.getResource(path);
			if (itemResource != null) {
				log.debug("Resource Item is not null");
				if (AuthUtil.isAuthorOrPreview(request)) {
					access = AuthUtil.isAuthorOrPreview(request);
				} else {
					access = AuthUtil.checkAccess(request, itemResource);
				}
				log.debug("Item has Access ::{}", access);
			}
		}
		return access;
	}

}
