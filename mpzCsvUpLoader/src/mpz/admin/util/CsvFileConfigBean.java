package mpz.admin.util;

/**
 * CSVファイル設定情報
 * ※Jsonでシリアライズ
 * @author tsutomu.kobayashi
 *
 */
public class CsvFileConfigBean {
	private String csvName;
	private String reqImp;
	private String reqExp;
	private boolean execImp;
	/**
	 * コンストラクタ
	 * @param name
	 * @param reqPrm
	 * @param exec
	 */
	public CsvFileConfigBean(String csvName,String reqImp,String reqExp,boolean execImp){
		this.csvName = csvName;
		this.reqImp = reqImp;
		this.reqExp = reqExp;
		this.execImp = execImp;
	}
	public CsvFileConfigBean(){}
	public String getCsvName() {
		return csvName;
	}
	public void setCsvName(String csvName) {
		this.csvName = csvName;
	}
	public String getReqImp() {
		return reqImp;
	}
	public void setReqImp(String reqImp) {
		this.reqImp = reqImp;
	}
	public String getReqExp() {
		return reqExp;
	}
	public void setReqExp(String reqExp) {
		this.reqExp = reqExp;
	}
	public boolean isExecImp() {
		return execImp;
	}
	public void setExecImp(boolean execImp) {
		this.execImp = execImp;
	}
}
