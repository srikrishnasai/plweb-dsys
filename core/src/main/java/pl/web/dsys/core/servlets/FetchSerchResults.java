package pl.web.dsys.core.servlets;

//apache sling imports
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.web.dsys.core.search.SearchResult;
import pl.web.dsys.core.search.SearchResultsPagination;
import pl.web.dsys.core.search.predicates.PredicateResolver;
import pl.web.dsys.core.search.predicates.impl.FullltextPredicateFactoryImpl;
import pl.web.dsys.core.search.providers.SearchProvider;

//OSGI Annotations
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

//json imports
import org.json.JSONException;
import org.json.JSONObject;

//java util imports
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;

import com.day.cq.commons.inherit.ComponentInheritanceValueMap;
import com.day.cq.search.result.Hit;
import com.day.cq.wcm.api.NameConstants;

import org.apache.commons.lang3.StringUtils;



//OSGI Annotation Declaration R7 Format
@Component(name = "Search Results GET Servlet", service={Servlet.class},
				property = {"sling.servlet.methods= " + HttpConstants.METHOD_GET,
								"sling.servlet.resourceTypes="+ "plweb-dsys/components/global-search/v1/global-search",
								"sling.servlet.extensions=" + "json"})
public class FetchSerchResults extends SlingAllMethodsServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(FetchSerchResults.class);

	@Reference
	private PredicateResolver predicateResolver;

	@Reference
	private SearchProvider searchProvider;

	private List<SearchResult> searchResults;

	private List<SearchResultsPagination> pagination;

	private long timeTaken = -1;

	private String totalResults = StringUtils.EMPTY;

	private com.day.cq.search.result.SearchResult result;

	private long offsetValue = 0L;
	private long rangeStart = 0L;
	private long rangeEnd = 0L;
	private long pageNumber = 0L;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		String searchTerm = request.getParameter("q") != null ? request.getParameter("q") : StringUtils.EMPTY;
		String offset = request.getParameter("offset") != null ? request.getParameter("offset") : "0";
		String noOfResPerPage = request.getParameter("noOfResPerPage") != null ? request.getParameter("noOfResPerPage")
				: "10";
		Resource res = request.getResource();
		log.debug("Resource is registered via resourceType and its resource path is ::{}", res.getPath());
		Map<String, String> searchPredicates = predicateResolver.getRequestPredicates(request);
		log.debug("Search Map ::{} for searchTerm ::{} and offset ::{} and noOfResPerPage ::{}", searchPredicates,
				searchTerm, offset, noOfResPerPage);
		boolean isMultiWord = isMultiWord(searchTerm);
		String modifiedSearchTerm = isMultiWord ? "\"" + searchTerm + "\"" : searchTerm;
		JSONObject jObj = new JSONObject();
		try {
			if (isSearchable(request)) {
				if (isMultiWord) {
					searchPredicates.put("fulltext", modifiedSearchTerm);
					result = searchProvider.search(request.getResourceResolver(), searchPredicates);
					List<Hit> hits = result.getHits();
					if (null != hits && hits.size() == 0) {
						searchPredicates.put("fulltext", searchTerm);
						result = searchProvider.search(request.getResourceResolver(), searchPredicates);
					}
				} else {
					result = searchProvider.search(request.getResourceResolver(), searchPredicates);
				}
				pagination = searchProvider.buildPagination(result, "Previous", "Next");
				searchResults = searchProvider.buildSearchResults(result, searchTerm);
				totalResults = computeTotalMatches(result);
				timeTaken = result.getExecutionTimeMillis();
				if (searchResults != null && !searchResults.isEmpty()) {
					searchResults.stream().forEach(e -> {
						log.info("Path in item ::{}", e.getPath());
						e.setFixedUrl(getFixedUrl(e.getPath(), request));
					});
				}
				offsetValue = result.getStartIndex();
				rangeStart = offsetValue + 1;
				long hitsPerPage = result.getHitsPerPage();
				rangeEnd = Math.min(offsetValue + hitsPerPage, result.getTotalMatches());
				pageNumber = 1 + offsetValue / hitsPerPage;
				jObj.put("searchTerm", searchTerm);
				jObj.put("pagination", pagination);
				jObj.put("results", searchResults);
				jObj.put("resultTotal", totalResults);
				jObj.put("timeTake", timeTaken);
				jObj.put("rangeStart", rangeStart);
				jObj.put("rangeEnd", rangeEnd);
				jObj.put("pageNumber", pageNumber);
				jObj.put("offsetValue", offsetValue);
			} else {
				jObj.put("searchTerm", searchTerm);
				jObj.put("pagination", new ArrayList<>());
				jObj.put("results", new ArrayList<>());
				jObj.put("resultTotal", "");
				jObj.put("timeTake", -1);
				jObj.put("rangeStart", 0L);
				jObj.put("rangeEnd", 0L);
				jObj.put("pageNumber", 0L);
				jObj.put("offsetValue", 0L);
			}

		} catch (JSONException e) {
			log.error("Error Occured while parsing Json Object ::{}", e.getMessage());
		}
		// response.getWriter().write(jObj.toString());
		response.getWriter().print(jObj);
	}

	private boolean isMultiWord(String searchTerm) {
		String[] words = searchTerm.trim().split(" ");
		log.info("is Multi Word ::{}", words.toString());
		if (words.length >= 2) {
			return true;
		}
		return false;
	}

	private String getFixedUrl(String path, SlingHttpServletRequest request) {
		String originalLink = path;
		String fixedLink = StringUtils.EMPTY;
		if (StringUtils.isBlank(originalLink)) {
			return StringUtils.EMPTY;
		}
		ResourceResolver resourceResolver = request.getResourceResolver();
		Resource page = resourceResolver.resolve(originalLink);

		// Code Scan Remediation
		if (page != null) {
			if (page.getResourceType().equals(NameConstants.NT_PAGE)) {
				// page link
				String newLink = resourceResolver.map(request, originalLink);
				if (newLink != null) {
					if (newLink.contains(".html")) {
						fixedLink = newLink;
					} else {
						fixedLink = newLink + ".html";
					}
				}
			} else {
				// DAM asset
				fixedLink = resourceResolver.map(request, originalLink);
			}
		} else {
			// this will be the case for external links
			fixedLink = originalLink;
		}

		return fixedLink;
	}

	private boolean isSearchable(SlingHttpServletRequest request) {
		return StringUtils.isNotBlank(getSearchTerm(request))
				|| new ComponentInheritanceValueMap(request.getResource()).getInherited("allowBlankFulltext", false);
	}

	private String getSearchTerm(SlingHttpServletRequest request) {
		return request.getParameter(FullltextPredicateFactoryImpl.REQUEST_PARAM);
	}

	private String computeTotalMatches(com.day.cq.search.result.SearchResult result) {
		String totalResult = String.valueOf(result.getTotalMatches());

		// Returning a String with '+' character in the case guessTotal is being used
		if (result.hasMore()) {
			totalResult += "+";
		}
		return totalResult;
	}
    
}
