package pl.web.dsys.core.utils;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;

import pl.web.dsys.core.pojos.AssetlistBean;

public  class ListSort implements Comparator<AssetlistBean>, Serializable {

    private static final long serialVersionUID = 204096578105548876L;
    private Enums.SortOrder sortOrder;
    private Enums.OrderBy orderBy;

    public ListSort(Enums.OrderBy orderBy, Enums.SortOrder sortOrder) {
        this.orderBy = orderBy;
        this.sortOrder = sortOrder;
    }

    @Override
    public int compare(AssetlistBean item1, AssetlistBean item2) {
        int i = 0;
         
        if (orderBy == Enums.OrderBy.PUBLISHED) {
            if(item1.getPublishedDate() !=null &&  item2.getPublishedDate() !=null)
                i = item1.getPublishedDate().compareTo(item2.getPublishedDate());
        } else if (orderBy == Enums.OrderBy.MODIFIED) {                 
            if(item1.getLastModified() !=null &&  item2.getLastModified() !=null)
                i = item1.getLastModified().compareTo(item2.getLastModified());
        } else if (orderBy == Enums.OrderBy.TITLE) {

            String title1 = StringUtils.stripToEmpty(item1.getTitle());


            String title2 = StringUtils.stripToEmpty(item2.getTitle());

            i = title1.compareTo(title2);
        }

        if (sortOrder == Enums.SortOrder.DESC) {
            i = i * -1;
        }
        return i;
    }
    
}
