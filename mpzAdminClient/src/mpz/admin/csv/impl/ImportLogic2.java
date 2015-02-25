package mpz.admin.csv.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mpz.admin.csv.ClientLogic;
import mpz.admin.csv.CsvFile;
import mpz.admin.csv.HttpCilentInput;
import mpz.admin.csv.ToolArgData;
import mpz.admin.util.ConfProperties;
import mpz.admin.util.CsvFileConfigBean;
import mpz.admin.util.Result;

import org.apache.log4j.Logger;

/**
 * CSVファイルインポート(差分確認用エクスポート実施)
 * @author tsutomu.kobayashi
 *
 */
public class ImportLogic2 extends ClientLogic{
	private final static String dateString;
	static {
		dateString = (new SimpleDateFormat("yyyyMMddHHmm")).format(new Date());
	}

	private final MpzAdminCsvClient client;
	public ImportLogic2(ToolArgData argData, HttpCilentInput input, ConfProperties prop) {
		super(argData, input, prop);
		this.client = new MpzAdminCsvClient(input);
	}

	private static final Logger log = Logger.getLogger( ImportLogic2.class );

	@Override
	protected void execute() throws Exception {
		final List<CsvFileConfigBean> confCsvList = getProp().getConfCsvList(getArgData().getTargetFileNameSet());

		String path = getArgData().getCheckDir();
		String commonCheckPath = path +
			(path.endsWith(File.separator) ? "" : File.separator) +
			getArgData().getToolType() +"_" + dateString;
		String chkOrgPath = commonCheckPath +"_org";
		String chkUpdPath = commonCheckPath +"_upd";
		log.info("origin export start ******************");
		export(chkOrgPath,confCsvList);
		log.info("origin export end ******************");
		log.info("import start ******************");
		final Result result = new Result();
		log.info("import start ******************");
		//CSVファイルディレクトリ取得
		String filePath = getArgData().getCsvFileDir();

		final List<CsvFile> fileList = new ArrayList<CsvFile>();
		for(CsvFileConfigBean c: confCsvList){
			fileList.add(new CsvFile(c,filePath));
		}
		for(CsvFile f : fileList){
			try{
				if(!f.isImportTarget()){
					log.debug(f.getName()+": skip.");
					continue;
				}
				if(!f.exists()){
					log.debug(f.getFile().getAbsolutePath()+": file not found.");
					continue;
				} else{
					log.info(f.getFile().getAbsolutePath()+": import.");
				}
				if(client.upload(f)){
					result.addOk(f.getName());
				} else {
					result.addNg(f.getName());
				}
			}catch(Exception e){
				result.addNg(f.getName());
				log.error(e);
			}
		}
		log.info("import end ******************");
		if(getArgData().getExecuteType().isExecuteClear()){
			//キャッシュクリア実施
			log.info("");
			log.info("cache clear start ******************");
			client.clear();
			log.info("cache clear end ******************");
		}
		log.info("");
		log.info("update export start ******************");
		export(chkUpdPath,confCsvList);
		log.info("update export end ******************");

		log.info("import result start.++++++++++++++++++++++++++++++++++++++++++++++++++++");
		if(result.isEmpty()){
			log.info("nothing.");
		} else {
			for(String logtext : result.getLogs()){
				log.info(logtext);
			}
		}
		log.info("import result end.++++++++++++++++++++++++++++++++++++++++++++++++++++");
		log.info("check diff. see:"+chkOrgPath + "\r\n" + chkUpdPath);
	}

	private void export(String path,final List<CsvFileConfigBean> confCsvList) throws Exception {
		final Result result = new Result();
		//CSVファイルディレクトリ取得
		final File exportDir = new File(path);
		exportDir.mkdir();
		final String filePath = exportDir.getAbsolutePath();

		final List<CsvFile> fileList = new ArrayList<CsvFile>();
		for(CsvFileConfigBean c: confCsvList){
			fileList.add(new CsvFile(c,filePath));
		}

		for(CsvFile f : fileList){
			try{
				if(f.exists()){
					log.info(f.getFile().getAbsolutePath()+": delete.");
					f.getFile().delete();
				}
				if( !client.export(f.getFile(),f.getReqExport()) )
					result.addNg(f.getName());
			}catch(Exception e){
				result.addNg(f.getName());
				log.error(e);
			}
		}
		log.info("export result ******************");
		if(result.isEmpty()){
			log.info("check export OK.");
		} else {
			log.info("check export NG.");
			for(String logtext : result.getLogs()){
				log.info(logtext);
			}
		}
	}

	@Override
	protected void login() throws Exception {
		client.login();
	}
}
