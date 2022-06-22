package pl.web.dsys.core.utils;

import org.apache.commons.lang3.StringUtils;

public class Enums {
    public enum SortOrder {
        ASC("asc"),
        DESC("desc");

        public String value;

        SortOrder(String value) {
            this.value = value;
        }

        public static SortOrder fromString(String value) {
            for (SortOrder s : values()) {
                if (StringUtils.equals(value, s.value)) {
                    return s;
                }
            }
            return ASC;
        }
    }

   public enum OrderBy {
        TITLE("title"),
        MODIFIED("modified"),
    	PUBLISHED("published");
    	

        private String value;

        OrderBy(String value) {
            this.value = value;
        }

        public static OrderBy fromString(String value) {
            for (OrderBy s : values()) {
                if (StringUtils.equals(value, s.value)) {
                    return s;
                }
            }
            return null;
        }
    }
    public enum Source {
        CHILDREN("children"),
        STATIC("static"),
        SEARCH("search"),
        TAGS("tags"),
		STATEFORMS("stateforms"),
		ADDFORMS("addforms"),
        EMPTY(StringUtils.EMPTY);

        private String value;

        Source(String value) {
            this.value = value;
        }

        public static Source fromString(String value) {
            for (Source s : values()) {
                if (StringUtils.equals(value, s.value)) {
                    return s;
                }
            }
            return null;
        }
    }
    
}
