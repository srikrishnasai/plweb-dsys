<%@include file="/libs/granite/ui/global.jsp" %>
<%@page session="false"
          import="com.adobe.granite.ui.components.AttrBuilder,
                  com.adobe.granite.ui.components.Config,
                  com.adobe.granite.ui.components.Field,
                  com.adobe.granite.ui.components.Tag" %>
 <%
	Config cfg = cmp.getConfig();

    ValueMap vm = (ValueMap) request.getAttribute(Field.class.getName());
    Field field = new Field(cfg);

    AttrBuilder attrsInput = new AttrBuilder(request, xssAPI);
    attrsInput.add("name", cfg.get("name", String.class));
    attrsInput.add("value", vm.get("value", String.class));
%>
<div class="custom-dialog-slider__wrapper">
    <input type="hidden" class="custom-dialog-slider__val" <%= attrsInput.build() %>>
    <div class="custom-dialog-slider__elem"></div>
</div>
