package pl.web.dsys.core.pojos;

import java.util.List;

/**
 * ChildrenEditorPojo class is a pojo for getting AuthTag Info to be used in tabs component.
 */
public class TabList {
    public List<TagInfo> tagList;

    public List<TagInfo> getTagList() {
        return tagList;
    }

    public void setTagList(List<TagInfo> tagList) {
        this.tagList = tagList;
    }

   
    
}
