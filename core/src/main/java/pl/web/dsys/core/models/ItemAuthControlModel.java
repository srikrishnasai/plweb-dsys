package pl.web.dsys.core.models;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.web.dsys.core.utils.AuthUtil;
import pl.web.dsys.core.utils.SharedContants;

@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json")
public class ItemAuthControlModel {

	private static final Logger log = LoggerFactory.getLogger(ItemAuthControlModel.class);

	@SlingObject
	private Resource resource;

	@SlingObject
	private ResourceResolver resolver;

	@SlingObject
	private SlingHttpServletRequest request;

	@RequestAttribute
	private String path;

	@RequestAttribute
	private String selector;

	protected boolean isItemAuth;
	ValueMap vm;
	boolean access = Boolean.TRUE;
	String group = StringUtils.EMPTY;

	@PostConstruct
	protected void init() {
		log.debug("Group Tag in Item Auth Control :: ");
		group = AuthUtil.getGroupTag(selector);
		if (null != resource) {
			vm = resource.getValueMap();
			isItemAuth = vm.get(SharedContants.PN_AUTH_BY_ITEM, SharedContants.DO_COMPONENT_AUTH_DEFAULT);

			if (isItemAuth) {
				log.debug("Inside is Resource Authorized method ::{}", path);
				Resource itemResource = resolver.getResource(path);
				if (itemResource != null) {
					log.debug("Resource Item is not null");
					if (AuthUtil.isAuthorOrPreview(request)) {
						access = AuthUtil.isAuthorOrPreview(request);
					} else {
						ValueMap itemVm = AuthUtil.getVm(resolver, itemResource);
						if (null != itemVm) {
							String[] authTags = itemVm.get(SharedContants.PN_AUTH_TAGS, new String[0]);
							log.debug("Auth tags for the page ::{}", Arrays.asList(authTags));
							boolean isDenyAuth = itemVm.get(SharedContants.PN_DENY_TAGS,
									SharedContants.DO_DENY_AUTH_DEFAULT);
							log.debug("is Deny Tags check for the component ::{}", isDenyAuth);
							access = AuthUtil.checkAccess(authTags, isDenyAuth, group.trim());
						}
					}
					log.debug("Item has Access ::{}", access);
				}
			}

		}

	}

	public boolean isResourceAuthorized() {
		return access;
	}
}
