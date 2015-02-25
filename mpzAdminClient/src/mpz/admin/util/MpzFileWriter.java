package mpz.admin.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * ファイル書き込み
 * @author tsutomu.kobayashi
 *
 */
public class MpzFileWriter {
	private final File file;
	/**
	 * コンストラクタ
	 * @param file
	 */
	public MpzFileWriter(final File file){
		this.file = file;
	}
	/**
	 * 書き込み
	 * @param text
	 * @throws Exception
	 */
	public void write (final String text) throws Exception {
		FileOutputStream outStream= null;
		OutputStreamWriter outStreamWriter = null;
		try{
			outStream = new FileOutputStream(this.file.getAbsolutePath());
			outStreamWriter = new OutputStreamWriter(outStream, "UTF-8");
			outStreamWriter.write(text);
		}finally{
			if(outStreamWriter != null)
				outStreamWriter.close();
			if(outStream != null)
				outStream.close();
		}
	}
}
