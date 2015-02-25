package mpz.admin.util;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * conf.properties情報管理
 * @author tsutomu.kobayashi
 *
 */
public class ConfProperties extends PropertiesBase {
	private static final String CONF_FILE_PATH = "conf.properties";

	private static String KEY_ADMIN_URL_PREFIX 	= "mpz.admin.url.";
	private static String KEY_CSV_FILE_JSON 	= "csv.file.json";
	private static String KEY_TIME_OUT 			= "mpz.admin.timeout";
	public void init() throws Exception  {
		super.init(CONF_FILE_PATH);
    }

	public boolean hasToolConfig(final String key) throws MissingResourceException {
		return hasProperty(KEY_ADMIN_URL_PREFIX + key);
    }

	public String getToolUrl(final String key) throws MissingResourceException {
		return getString(KEY_ADMIN_URL_PREFIX + key);
    }

	public List<CsvFileConfigBean> getConfCsvList(final Set<String> filterSet) throws Exception {
		final Gson gson = new Gson();
		final String json = getString(KEY_CSV_FILE_JSON);
		List<CsvFileConfigBean> list = gson.fromJson(json, new TypeToken<List<CsvFileConfigBean>>(){}.getType());
		if(filterSet == null || filterSet.isEmpty())
			return list;
		List<CsvFileConfigBean> rtnList = new ArrayList<CsvFileConfigBean>();
		for(CsvFileConfigBean csvFileConfig : list) {
			if(filterSet.contains(csvFileConfig.getCsvName()))
				rtnList.add(csvFileConfig);
		}
		return rtnList;
	}


	public int getTimeOut() throws MissingResourceException,NumberFormatException {
		return getInt(KEY_TIME_OUT);
    }

}

