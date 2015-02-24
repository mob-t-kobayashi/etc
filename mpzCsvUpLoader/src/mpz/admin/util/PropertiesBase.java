package mpz.admin.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Propertyファイル基底
 * @author tsutomu.kobayashi
 *
 */
abstract class PropertiesBase {
	private static final Logger log = Logger.getLogger( PropertiesBase.class );

	private final Properties prop = new Properties();

	protected void init(final String fileName) throws Exception  {
		String filePath = fileName;
		InputStream inStream = null;
		File f = new File(filePath);
		log.info("property file path.(" + filePath + ")=" + f.getAbsolutePath());
		if(!f.exists() || !f.isFile()){
			filePath = "..\\" + filePath;
			File f2 = new File(filePath);
			log.info("property file path.(" + filePath + ")=" + f2.getAbsolutePath());
			if(!f2.exists() || !f2.isFile()){
				throw new Exception(filePath + ":not found.");
			}
		}
		try {
			inStream = new BufferedInputStream(
				new FileInputStream(filePath));
			prop.load(inStream);
		} finally {
			if (inStream != null) {
				inStream.close();
			}
		}
    }

	protected boolean hasProperty(final String key) throws MissingResourceException {
		String val = prop.getProperty(key,null);
		return val != null;
    }

	protected String getString(final String key) throws MissingResourceException {
		String val = prop.getProperty(key,null);
		log.debug("load "+key+"="+val);
		return val;
    }

	protected int getInt(final String key) throws MissingResourceException,NumberFormatException {
		return Integer.valueOf(getString(key));
    }

}

