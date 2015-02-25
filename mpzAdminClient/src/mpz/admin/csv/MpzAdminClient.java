package mpz.admin.csv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * HTTP Client共通ラッパー
 * @author tsutomu.kobayashi
 *
 */
public abstract class MpzAdminClient {

	private static final Logger log = Logger.getLogger( MpzAdminClient.class );

	public static final int HTTP_STATUS_OK 				= 200;

	protected final static String MPZ_ADMIN_URL_TOP 		= "login";
	protected final static String MPZ_ADMIN_URL_LOGIN 	= "loginProcess";

	protected final static String MPZ_ADMIN_ENCODE 			= "UTF-8";

	protected final static String USER_AGENT = "MPZ Http Client";

	protected final String CI_COOKIE_ARU_CSRF_CODE = "ARU_csrf_code";
	protected final String CI_REQUEST_ARU_CSRF_CODE = "ARU_CSRF_R2D2";


	private final HttpCilentInput input;
	private final CloseableHttpClient httpClient;


	private String csrfToken = null;

	/**
	 * コンストラクタ
	 * @param config
	 * @param input
	 */
	protected MpzAdminClient(final HttpCilentInput input){
		this.input = input;
		// request configuration
		RequestConfig requestConfig = RequestConfig.custom()
		.setConnectTimeout(this.input.getTimeout())
		.setSocketTimeout(this.input.getTimeout())
		.build();
		// headers
		List<Header> headers = new ArrayList<Header>();
		headers.add(new BasicHeader("Accept-Charset",MPZ_ADMIN_ENCODE));
		headers.add(new BasicHeader("Accept-Language","ja, en;q=0.8"));
		headers.add(new BasicHeader("User-Agent",USER_AGENT));
		// create client
		this.httpClient = HttpClientBuilder.create()
			.setDefaultRequestConfig(requestConfig)
			.setDefaultHeaders(headers).build();
	}

	/**
	 * ログイン
	 * @return
	 * @throws Exception
	 */
	public boolean login() throws Exception{
		//トップページ表示+トークン発行
		final HttpGet httpGet = new HttpGet(this.input.getUri(MPZ_ADMIN_URL_TOP));
		final HttpResponse response = httpClient.execute(httpGet);
		final int responseStatus = response.getStatusLine().getStatusCode();
		this.csrfToken = getCookie(response,CI_COOKIE_ARU_CSRF_CODE);
		showHttpStatusLog(MPZ_ADMIN_URL_TOP,responseStatus);
		if( responseStatus != HTTP_STATUS_OK ){
			log.error("login error");
			return false;
		}
		EntityUtils.toString(response.getEntity(), MPZ_ADMIN_ENCODE);

		//ログイン実施
		//String url ="http://local.admin.www.mpz.mobcast.io:8082/loginProcess";
	  	final HttpPost httpPost = new HttpPost(this.input.getUri(MPZ_ADMIN_URL_LOGIN));
	  	final List<NameValuePair> requestParams = new ArrayList<NameValuePair>();
		requestParams.add(new BasicNameValuePair("txtLoginId", this.input.getUser()));
		requestParams.add(new BasicNameValuePair("txtLoginPasswd", this.input.getPassword()));
		requestParams.add(new BasicNameValuePair(CI_REQUEST_ARU_CSRF_CODE,this.csrfToken));

		httpPost.setEntity(new UrlEncodedFormEntity(requestParams));
		final HttpResponse response2 = httpClient.execute(httpPost);
		final int responseStatus2 = response2.getStatusLine().getStatusCode();
		this.csrfToken = getCookie(response2,CI_COOKIE_ARU_CSRF_CODE);
		log.debug("cookie token ="+this.csrfToken);
		showHttpStatusLog(MPZ_ADMIN_URL_LOGIN,responseStatus2);
		String body = EntityUtils.toString(response2.getEntity(), MPZ_ADMIN_ENCODE);
		if(!checkResponse(MPZ_ADMIN_URL_LOGIN,body)){
			log.error("login error");
			return false;
		}
		log.info("login success");
		return true;
	}


	protected String getCsrfToken() {
		return csrfToken;
	}

	protected void setCsrfToken(String csrfToken) {
		this.csrfToken = csrfToken;
	}

	protected HttpCilentInput getInput() {
		return input;
	}

	protected CloseableHttpClient getHttpClient() {
		return httpClient;
	}

	protected void setCsrfToken(final HttpResponse response){
		this.csrfToken = getCookie(response,CI_COOKIE_ARU_CSRF_CODE);
	}

	/**
	 * レスポンスチェック
	 * @param text
	 * @param body
	 * @return
	 */
	protected boolean checkResponse(final String text,final String body){
		final Map<String,String> resultMap = (new Gson()).fromJson(body, new TypeToken<Map<String,String>>(){}.getType());
		boolean check = "1".equals(resultMap.get("RESULT"));
		if(check){
			log.debug(text + "response:" + body);
		} else {
			log.error(text + "response:" + body);
		}
		return check;
	}

	/**
	 * Coolie取得
	 * @param response
	 * @param key
	 * @return
	 */
	protected String getCookie(final  HttpResponse response,String key){
		  String cookie = response.getFirstHeader("Set-Cookie")
            .getValue();
		 // System.out.println(cookie);
		  if(cookie.indexOf(key) < 0)
			  return "";
		  return  cookie.substring((key + "=").length(),
				  cookie.indexOf(";"));
	}

	protected void showHttpStatusLog(String url,final int httpStatusCode){
		if(httpStatusCode == HTTP_STATUS_OK){
			log.debug(url + ":http_status_code ="+httpStatusCode);
		} else {
			log.error(url + ":http_status_code ="+httpStatusCode);
		}
	}
}
