package mpz.admin.csv;

import java.io.File;

import mpz.admin.util.CsvFileConfigBean;

/**
 * CSVファイル情報
 * @author tsutomu.kobayashi
 *
 */
public class CsvFile {
	private final CsvFileConfigBean config;
	private final File file;
	/**
	 * コンストラクタ
	 * @param config
	 * @param filePath
	 */
	public CsvFile (final CsvFileConfigBean config,final String filePath){
		this.config =  config;
		this.file =  new File(filePath + (filePath.endsWith("\\") ? "":"\\") + config.getCsvName());
	}
	/**
	 * resuestパラメータ
	 * @return
	 */
	public String getReqImport() {
		return config.getReqImp();
	}
	public String getReqExport() {
		return config.getReqExp();
	}

	/**
	 * Fileオブジェクト取得
	 * @return
	 */
	public File getFile() {
		return file;
	}

	/**
	 * ファイル存在チェック
	 * @return
	 */
	public boolean exists() {
		return file.exists();
	}

	/**
	 * ファイル名取得
	 * @return
	 */
	public String getName(){
		return file.getName();
	}

	/**
	 * 実行対象
	 * @return
	 */
	public boolean isImportTarget(){
		return this.config.isExecImp();
	}
}
