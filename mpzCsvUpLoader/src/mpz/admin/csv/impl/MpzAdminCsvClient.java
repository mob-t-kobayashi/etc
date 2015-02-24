package mpz.admin.csv.impl;

import java.io.File;

import mpz.admin.csv.CsvFile;
import mpz.admin.csv.HttpCilentInput;
import mpz.admin.csv.MpzAdminClient;
import mpz.admin.util.MpzFileWriter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * HTTP Clientデータ操作
 * @author tsutomu.kobayashi
 *
 */
public class MpzAdminCsvClient extends MpzAdminClient{

	private static final Logger log = Logger.getLogger( MpzAdminCsvClient.class );

	protected final static String MPZ_ADMIN_URL_IMPORT 				= "common/gameDataCSVImportProcess";
	protected final static String MPZ_ADMIN_URL_EXPORT 				= "common/gameDataCSVExportProcess?hidDataType=";
	protected final static String MPZ_ADMIN_URL_CACHE_CLEAR_ALL 	= "common/gameCacheDataInitProcess?hidDataCategory=&hidMode=all";


	/**
	 * コンストラクタ
	 * @param input
	 */
	protected MpzAdminCsvClient(final HttpCilentInput input){
		super(input);
	}

	/**
	 * インポート実施
	 * @param args
	 */
	public boolean upload(final CsvFile csvFile) throws Exception {
		final HttpPost httpPost = new HttpPost(getInput().getUri(MPZ_ADMIN_URL_IMPORT));
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();

		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addPart("userfile", new FileBody(csvFile.getFile()));
		builder.addTextBody(CI_REQUEST_ARU_CSRF_CODE, getCsrfToken());
		builder.addTextBody("hidDataType", csvFile.getReqImport());

		httpPost.setEntity(builder.build());
		HttpResponse response = getHttpClient().execute(httpPost);
		final int responseStatus =  response.getStatusLine().getStatusCode();
		showHttpStatusLog(MPZ_ADMIN_URL_IMPORT +"("+csvFile.getFile()+")",responseStatus);
		if(responseStatus != HTTP_STATUS_OK)
			return false;

		String b = EntityUtils.toString(response.getEntity(), MPZ_ADMIN_ENCODE);
		if(!checkResponse(MPZ_ADMIN_URL_IMPORT,b))
				return false;
		setCsrfToken(response);
		return responseStatus == HTTP_STATUS_OK;
	}

	/**
	 * エクスポート実施
	 * @param args
	 */
	public boolean export(final File file, final String param) throws Exception {
		HttpEntity entity = null;
		final String url = MPZ_ADMIN_URL_EXPORT + param;
		final HttpGet httpGet = new HttpGet(getInput().getUri(url));
		final HttpResponse response = getHttpClient().execute(httpGet);
		final int responseStatus = response.getStatusLine().getStatusCode();
		showHttpStatusLog(url,responseStatus);
		if( responseStatus != HTTP_STATUS_OK )
		  		return false;
		setCsrfToken(response);
		entity = response.getEntity();
		if (entity != null) {
			MpzFileWriter fileWriter = new MpzFileWriter(file);
			fileWriter.write(EntityUtils.toString(response.getEntity(), MPZ_ADMIN_ENCODE));
			EntityUtils.consume(entity);
		} else{
			log.info(file.getName() + ":response file is empty.");
			return false;
		}
		return true;
	}

	/**
	 * キャッシュクリア
	 * @return
	 * @throws Exception
	 */
	public int clear() throws Exception{

		final HttpGet httpGet = new HttpGet(getInput().getUri(MPZ_ADMIN_URL_CACHE_CLEAR_ALL));
		final HttpResponse response = getHttpClient().execute(httpGet);
		final int responseStatus = response.getStatusLine().getStatusCode();
		showHttpStatusLog(MPZ_ADMIN_URL_CACHE_CLEAR_ALL,responseStatus);
		String body = EntityUtils.toString(response.getEntity(), MPZ_ADMIN_ENCODE);
		if( responseStatus != HTTP_STATUS_OK || !checkResponse(MPZ_ADMIN_URL_CACHE_CLEAR_ALL,body) ){
			log.error("cache clear faild!!!");
			return responseStatus;
		} else{
			log.info("cache clear success.");
		}

		 setCsrfToken(response);
		 return responseStatus;
	}


}
