package mpz.admin.csv;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import mpz.admin.util.ConfProperties;
import mpz.admin.util.CsvFileConfigBean;
import mpz.admin.util.ExecuteType;

import org.apache.log4j.Logger;

/**
 * 実行パラメータ情報
 * @author tsutomu.kobayashi
 *
 */
public class ToolArgData {

	enum Index{
		ADMIN_TOOL(0,"admin tool","(exp. dev/sand/stag/expo)"),
		TYPE(1,"execute type","(exp. import/import-clear/import-export-clear/export)"),
		USER_ID(2,"login user id",""),
		USER_PWD(3,"login user password",""),
		CSV_FILES_DIR(4,"csv files directory",""),
		TARGET_CSV_FILES_CSV(5,"target csv files csv","default all"),
//		CSV_FILES_CHECK_DIR(5,"check directory"," only \"import-export-clear\"")
		;
		private final int index;
		private final String name;
		private final String comment;
		Index(final int index,final String name, final String comment){
			this.index = index;
			this.name = name;
			this.comment = comment;
		}
		int getIndex() {
			return index;
		}
		String getName() {
			return name;
		}
		String getComment() {
			return comment;
		}
		static String getInfo(){
			StringBuilder sb = new StringBuilder();
			sb.append("arg");
			for(Index i : values()){
				sb.append("\r\n");
				sb.append("arg["+i.getIndex() + "] : " + i.getName() + " " + i.getComment());
			}
			return sb.toString();
		}
		static String getInfo(final String[] args){
			StringBuilder sb = new StringBuilder();
			sb.append("arg");
			for(Index i : values()){
				String val = args.length > i.getIndex() ? args[i.getIndex()] : "";
				sb.append("\r\n");
				sb.append("arg["+i.getIndex() + "](" + i.getName() + ") = " + val);
			}
			return sb.toString();
		}
	}

	private static final Logger log = Logger.getLogger( ToolArgData.class );
	private final String[] args;
	private final ConfProperties prop;
	private final Set<String> targetFileSet;

	/**
	 * コンストラクタ
	 * @param args
	 * @param prop
	 */
	ToolArgData(final String[] args,final ConfProperties prop){
		this.args = args;
		this.prop = prop;

		final Set<String> set = new LinkedHashSet<String>();
		if(this.args.length > 5) {
			final String targetFiles = this.args[Index.TARGET_CSV_FILES_CSV.getIndex()];
			if(targetFiles != null && targetFiles.length() > 0){
				String[] tgtFileNames = targetFiles.split(",");
				for (String s : tgtFileNames){
					set.add(s);
				}
			}
		}
		this.targetFileSet = set;
	}

	/**
	 * 入力チェック
	 * @param prop
	 * @return
	 * @throws Exception
	 */
	boolean validate() throws Exception {
		for(String a: args)
			log.debug("arg"+a);
		if(this.args == null || this.args.length < 4) {
			log.error("parameter is invalid.");
			log.error(Index.getInfo());
			return false;
		}
//		//export先の指定チェック
//		if(ExecuteType.getCode(this.args[Index.TYPE.getIndex()]) == ExecuteType.IMPORT_EXPORT_CLEAR
//				&& this.args.length < 5) {
//			log.error("parameter is invalid(need export directory).");
//			log.error(Index.getInfo());
//			return false;
//		}

		if(!this.prop.hasToolConfig(this.args[Index.ADMIN_TOOL.getIndex()])){
			log.error("admin tool target is invalid. (dev/sand/stag/expo)");
			return false;
		}

		final ExecuteType executeType = getExecuteType();
		if(executeType.isInValid()){
			log.error("type is invalid=" + this.args[Index.TYPE.getIndex()]);
			return false;
		}
//		String propKey = this.args[Index.ADMIN_TOOL.getIndex()];
//		log.debug(propKey);

		final File f = new File(getCsvFileDir());
		if(!f.exists()){
			log.error("csv files directory is not found. path=" + f.getAbsolutePath());
			return false;
		}

		Set<String> tgtFileSet = getTargetFileNameSet();
		List<CsvFileConfigBean> confFileList = this.prop.getConfCsvList(null);
		List<String> errorList = new ArrayList<String>();
		if(!tgtFileSet.isEmpty()){
			for(String s : tgtFileSet){
				boolean match = false;
				for(CsvFileConfigBean csvFileConfig : confFileList){
					match = csvFileConfig.getCsvName().equals(s);
					if(match){
						break;
					}
				}
				if(!match)
					errorList.add(s);
			}
		}

		if(!errorList.isEmpty()){
			StringBuffer sb = new StringBuffer();
			for(String s : errorList){
				if(sb.length() > 0)
					sb.append(",");
				sb.append(s);
			}
			log.error("target csv file is invalid. file =" + sb.toString());
			return false;
		}

		return true;
	}

	/**
	 * CSVファイルディレクトリ
	 * @return
	 */
	public String getCsvFileDir(){
		return this.args[Index.CSV_FILES_DIR.getIndex()];
	}

	public String getCheckDir(){
		return getCsvFileDir();
//		return this.args[Index.CSV_FILES_CHECK_DIR.getIndex()];
	}

	public String getUserId(){
		return this.args[Index.USER_ID.getIndex()];
	}

	public String getUserPassword(){
		return this.args[Index.USER_PWD.getIndex()];
	}

	public String getToolType(){
		return this.args[Index.ADMIN_TOOL.getIndex()];
	}

	public String getToolUrl(){
		return this.prop.getToolUrl(this.args[Index.ADMIN_TOOL.getIndex()]);
	}
	public ExecuteType getExecuteType(){
		return ExecuteType.getCode(this.args[Index.TYPE.getIndex()]);
	}

	public Set<String> getTargetFileNameSet(){
		return this.targetFileSet;
	}
	void showParamInfo(){
		log.info(Index.getInfo(this.args));
		Set<String> tgtFiles = getTargetFileNameSet();
		StringBuffer sb = new StringBuffer();
		sb.append("target files:");
		if(tgtFiles.isEmpty()){
			sb.append("default(all files.)");
		} else {

			for(String s : tgtFiles){
				sb.append("\r\n");
				sb.append(s);
			}
		}
		log.info(sb.toString());
	}
}
