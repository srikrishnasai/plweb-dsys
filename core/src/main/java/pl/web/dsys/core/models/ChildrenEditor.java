package pl.web.dsys.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.tagging.TagManager;

import pl.web.dsys.core.pojos.TabList;
import pl.web.dsys.core.pojos.TagInfo;

@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ChildrenEditor {

	Logger logger = LoggerFactory.getLogger(ChildrenEditor.class);

	@RequestAttribute
	private String nodePath = null;

	@SlingObject
	public Resource resource;

	@SlingObject
	public ResourceResolver resolver;

	public Resource childResource;
	int length = 0;
	private List<TabList> tabList;

	@PostConstruct
	protected void init() {
		if (StringUtils.isNotBlank(nodePath)) {
			childResource = resolver.getResource(nodePath);
			tabList = new ArrayList<>();
			setTabListData();

		}
	}

	public Resource getChildResource() {
		return childResource;
	}

	public List<TabList> getTabList() {
		return tabList;

	}

	public void setTabListData() {
		List<Resource> items = new ArrayList<>();
		if (null != childResource && childResource.hasChildren()) {
			Iterator<Resource> children = childResource.listChildren();
			while (children.hasNext()) {
				items.add(children.next());

			}
			//get taginfo using TagManager API
			TagManager tagManager = resolver.adaptTo(TagManager.class);

			for (int i = 0; i < items.size(); i++) {
				Resource item = items.get(i);
				ValueMap vm = item.getValueMap();
				String panelTitle = vm.get("cq:panelTitle", String.class);
				String[] authTag = vm.get("cq:authtags", new String[] {});
				TabList tab = new TabList();
				logger.info("panelTitle::{}", panelTitle);
				if (null != authTag) {
					List<TagInfo> tagList = new ArrayList<TagInfo>();

					for (String j : authTag) {
						//set TagInfo data
						TagInfo tag = new TagInfo();
						tag.setTagID(tagManager.resolve(j).getTagID());
						tag.setTagTitle(tagManager.resolve(j).getTitlePath());
						logger.info("authTag::{}", j);
						logger.info("tagValue::{}", tagManager.resolve(j));
						logger.info("tagID::{}", tagManager.resolve(j).getTagID());
						logger.info("tagTitle::{}", tagManager.resolve(j).getTitle());
						logger.info("tagTitlePath::{}", tagManager.resolve(j).getTitlePath());
						logger.info("tagNameSpace::{}", tagManager.resolve(j).getNamespace().toString());
						logger.info("tagLocalID:{}", tagManager.resolve(j).getLocalTagID());
						logger.info("tagName::{}", tagManager.resolve(j).getName());
						logger.info("tagValueTitle::{}", tagManager.resolveByTitle(j));
						tagList.add(tag);

					}
					//set TagList data
					tab.setTagList(tagList);

				}
				//set TabList
				tabList.add(tab);

			}

		}
	}
}
