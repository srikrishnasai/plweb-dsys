package pl.web.dsys.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.stream.Collectors;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPathsStrict;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import pl.web.dsys.core.utils.CloudManagerNotificationUtil;

/**
 * This Servlet acts as a webhook to post and get cloudmanager response.
 * 
 * @author Krishna
 *
 */
@Component(service = { Servlet.class }, immediate = true)
@SlingServletPathsStrict(paths = "/bin/pacificlife/cmnotification", methods = { HttpConstants.METHOD_GET,
		HttpConstants.METHOD_POST })
public class CloudManagerNotificationServlet extends SlingAllMethodsServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@ObjectClassDefinition(name = "CloudManager Notification Config", description = "Configuration needed to cloudmanager notification")
	public static @interface Config {

		@AttributeDefinition(name = "Organization ID")
		String organization_id() default "";

		@AttributeDefinition(name = "Technical Account Email")
		String technical_account_email() default "";

		@AttributeDefinition(name = "Technical Account ID")
		String technical_account_id() default "";

		@AttributeDefinition(name = "API Key")
		String api_key() default "";

		@AttributeDefinition(name = "Private Key Path")
		String private_key_path() default "";

		@AttributeDefinition(name = "Client Secret")
		String client_secret() default "";

		@AttributeDefinition(name = "Teams Webhook")
		String[] teams_webhook() default "";

		@AttributeDefinition(name = "Auth Server")
		String auth_server() default "";

	}

	private CloudManagerNotificationServlet.Config config;

	private static final String PRIVATE_KEY_FILE_NAME = "private.key";

	/**
	 * To verify the challenge from request.
	 */
	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {
		logger.debug("Inside doGet Method of Cloud Manager Servlet");
		resp.setContentType("application/text");
		PrintWriter writer = resp.getWriter();
		String challenge = "";
		try {
			challenge = CloudManagerNotificationUtil.getParamValue(req.getRequestURI() + "?" + req.getQueryString(),
					"challenge");
		} catch (URISyntaxException e) {
			logger.error("Error Occured while verifying Signature ::{}", e.getMessage());
		}
		if (!challenge.equals("")) {
			writer.print(challenge);
		} else {
			resp.sendError(400);
		}

	}

	/**
	 * Takes event data and sends notification if pipeline execution started/ended.
	 */
	@Override
	protected void doPost(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {
		logger.debug("Inside doPost Method of Cloud Manager Servlet");
		resp.setContentType("application/text");
		PrintWriter printWriter = resp.getWriter();
		printWriter.print("Request Received");
		logger.debug("Request Received from cloudmanager event handler.");
		String STARTED = "https://ns.adobe.com/experience/cloudmanager/event/started";
		String ENDED = "https://ns.adobe.com/experience/cloudmanager/event/ended";
		String EXECUTION = "https://ns.adobe.com/experience/cloudmanager/pipeline-execution";

		try {

			String requestData = req.getReader().lines().collect(Collectors.joining());
			CloudManagerNotificationUtil.verifySignature(requestData, req.getHeader("x-adobe-signature"), config);
			logger.debug("Signature Verified Successfully.");
			JsonElement jelement = new JsonParser().parse(requestData);
			JsonObject jobject = jelement.getAsJsonObject();

			String eventType = jobject.get("event").getAsJsonObject().get("@type").getAsString();
			String executionType = jobject.get("event").getAsJsonObject().get("xdmEventEnvelope:objectType")
					.getAsString();

			if ((ENDED.equals(eventType) || STARTED.equals(eventType)) && EXECUTION.equals(executionType)) {
				//
				String executionUrl = jobject.get("event").getAsJsonObject().get("activitystreams:object")
						.getAsJsonObject().get("@id").getAsString();
				logger.debug("Execution Url ::{}", executionUrl);
				String executionResponse = CloudManagerNotificationUtil.makeApiCall(
						CloudManagerNotificationUtil.getAccessToken(config, PRIVATE_KEY_FILE_NAME), executionUrl,
						config);

				JsonElement jelementer = new JsonParser().parse(executionResponse);
				JsonObject jobjecter = jelementer.getAsJsonObject();

				String pipelineurl = jobjecter.get("_links").getAsJsonObject()
						.get("http://ns.adobe.com/adobecloud/rel/pipeline").getAsJsonObject().get("href").getAsString();
				logger.debug("PipeLine Url ::{}", pipelineurl);
				URI uri = new URL(executionUrl).toURI();

				String pipelineResponse = CloudManagerNotificationUtil.makeApiCall(
						CloudManagerNotificationUtil.getAccessToken(config, PRIVATE_KEY_FILE_NAME),
						uri.resolve(pipelineurl).toURL().toString(), config);
				logger.debug("Pipeline Response ::{}", pipelineResponse);
				JsonElement jelementpipeline = new JsonParser().parse(pipelineResponse);
				JsonObject jobjectpipeline = jelementpipeline.getAsJsonObject();

				CloudManagerNotificationUtil
						.notifyTeams(jobjectpipeline.get("name").getAsString() + " Pipeline has been started", config);
				logger.debug("Notification Successfully sent to channel..");
			}

		} catch (Exception e) {
			logger.error("Error Occured ::{}", e.getMessage());
			resp.sendError(500);
		}
	}

	@Activate
	@Modified
	public void activate(Config config) {
		this.config = config;
	}

}
