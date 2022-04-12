package pl.web.dsys.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import pl.web.dsys.core.servlets.CloudManagerNotificationServlet;

/**
 * This Util has methods required for connecting cloudmanager and teams channel
 * to send notifications.
 * 
 * @author Krishna
 *
 */
public class CloudManagerNotificationUtil {
	static long EXPIRATION = 60 * 60; // 1 hour
	private static final Logger log = LoggerFactory.getLogger(CloudManagerNotificationUtil.class);

	/**
	 * 
	 * @param link
	 * @param paramName
	 * @return
	 * @throws URISyntaxException
	 */
	public static String getParamValue(String link, String paramName) throws URISyntaxException {
		List<NameValuePair> queryParams = new URIBuilder(link).getQueryParams();
		return queryParams.stream().filter(param -> param.getName().equalsIgnoreCase(paramName))
				.map(NameValuePair::getValue).findFirst().orElse("");
	}

	/**
	 * Verify Signature
	 * 
	 * @param requestData
	 * @param signature
	 * @param config
	 * @throws Exception
	 */
	public static void verifySignature(String requestData, String signature,
			CloudManagerNotificationServlet.Config config) throws Exception {

		if (signature != null && !signature.equals("")) {

			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKeySpec = new SecretKeySpec(config.client_secret().getBytes(), "HmacSHA256");
			mac.init(secretKeySpec);
			String hmacSha256 = Base64.encodeBase64String(mac.doFinal(requestData.getBytes()));
            log.debug("HmacSha String ::{}", hmacSha256);
			if (!signature.equals(hmacSha256)) {
				throw new Exception("x-adobe-signature HMAC check failed");
			}
		} else {
			throw new Exception("x-adobe-signature required");
		}

	}

	/**
	 * Get Access Token
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static String getAccessToken(CloudManagerNotificationServlet.Config config, String fileName)
			throws Exception {
		String jwtToken = getJWTToken(config, fileName);

		String accessToken = "";
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

			HttpPost authPostRequest = new HttpPost("https://" + config.auth_server() + "/ims/exchange/jwt");
			authPostRequest.addHeader("Cache-Control", "no-cache");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("client_id", config.api_key()));
			params.add(new BasicNameValuePair("client_secret", config.client_secret()));
			params.add(new BasicNameValuePair("jwt_token", jwtToken));
			log.debug("AccessToken Params ::{}", params);
			authPostRequest.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
			HttpResponse response = httpclient.execute(authPostRequest);
			if (200 != response.getStatusLine().getStatusCode()) {
				throw new IOException("Server returned error: " + response.getStatusLine().getReasonPhrase());
			}
			HttpEntity entity = response.getEntity();

			JsonElement jelement = new JsonParser().parse(EntityUtils.toString(entity));
			JsonObject jobject = jelement.getAsJsonObject();

			accessToken = jobject.get("access_token").getAsString();

		}
		return accessToken;
	}

	/**
	 * Get JWT Token
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	private static String getJWTToken(CloudManagerNotificationServlet.Config config, String fileName) throws Exception {
		Long expirationTime = Math.round(new Date().getTime() / 1000) + EXPIRATION;

		HashMap<String, Object> jwtClaims = new HashMap<String, Object>();
		jwtClaims.put("iss", config.organization_id());
		jwtClaims.put("sub", config.technical_account_id());
		jwtClaims.put("exp", expirationTime);
		jwtClaims.put("aud", "https://" + config.auth_server() + "/c/" + config.api_key());
		jwtClaims.put("https://" + config.auth_server() + "/s/ent_cloudmgr_sdk", true);
		log.debug("JwtClaims Map ::{}", jwtClaims);
		String privateKeyContent;
		String privateKeyPath = config.private_key_path();
		try (InputStream keyStoreStream = new FileInputStream(
				new File(privateKeyPath, FilenameUtils.getName(fileName)))) {
			privateKeyContent = new BufferedReader(new InputStreamReader(keyStoreStream, StandardCharsets.UTF_8))
					.lines().collect(Collectors.joining("\n"));
		}
		privateKeyContent = privateKeyContent.replace("-----BEGIN PRIVATE KEY-----", "");
		privateKeyContent = privateKeyContent.replace("-----END PRIVATE KEY-----", "");
		privateKeyContent = privateKeyContent.replaceAll("\\s+", "");

		byte[] pkcs8EncodedBytes = DatatypeConverter.parseBase64Binary(privateKeyContent);

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		RSAPrivateKey privateKey = (RSAPrivateKey) kf.generatePrivate(keySpec);

		String jwtToken = Jwts.builder().setClaims(jwtClaims).signWith(SignatureAlgorithm.RS256, privateKey).compact();

		return jwtToken;
	}

	/**
	 * Cloud Manager API Calls
	 * 
	 * @param accessToken
	 * @param url
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static String makeApiCall(Object accessToken, String url, CloudManagerNotificationServlet.Config config)
			throws Exception {
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

			HttpGet apiRequest = new HttpGet(url);
			apiRequest.addHeader("x-gw-ims-org-id", config.organization_id());
			apiRequest.addHeader("x-api-key", config.api_key());
			apiRequest.addHeader("Authorization", "Bearer " + accessToken);

			HttpResponse response = httpclient.execute(apiRequest);
			if (200 != response.getStatusLine().getStatusCode()) {
				throw new IOException("Server returned error: " + response.getStatusLine().getReasonPhrase());
			}
			HttpEntity entity = response.getEntity();

			return EntityUtils.toString(entity);

		}
	}

	/**
	 * Post Message to Teams
	 * 
	 * @param message
	 * @param config
	 * @throws IOException
	 */
	public static void notifyTeams(String message, CloudManagerNotificationServlet.Config config) throws Exception {
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

			for (String webhook : config.teams_webhook()) {
				log.debug("Webhook Channel Url ::{}", webhook);
				HttpPost notifyPostRequest = new HttpPost(webhook);

				Map<String, String> postMessage = new HashMap<>();
				/*
				 * postMessage.put("@context", "blue"); postMessage.put("@type", "yellow");
				 * postMessage.put("themeColor", "green"); postMessage.put("title",
				 * "Notification From Cloud Manager");
				 */
				postMessage.put("text", message);
				StringEntity postingString = new StringEntity(new Gson().toJson(postMessage));
				notifyPostRequest.setEntity(postingString);
				notifyPostRequest.addHeader("Content-Type", "application/json");

				HttpResponse response = httpclient.execute(notifyPostRequest);

				if (200 != response.getStatusLine().getStatusCode()) {
					throw new IOException("Server returned error: " + response.getStatusLine().getReasonPhrase());
				}

			}
		}
	}
}
