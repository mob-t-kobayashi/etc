package mpz.admin.csv.impl;

import java.util.ArrayList;
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
 * CSVファイルインポート
 * @author tsutomu.kobayashi
 *
 */
public class ImportLogic extends ClientLogic{
	private final MpzAdminCsvClient client;
	public ImportLogic(ToolArgData argData, HttpCilentInput input, ConfProperties prop) {
		super(argData, input, prop);
		this.client = new MpzAdminCsvClient(input);
	}

	private static final Logger log = Logger.getLogger( ImportLogic.class );

	@Override
	protected void execute() throws Exception {
		final Result result = new Result();
		log.info("import start ******************");
		//CSVファイルディレクトリ取得
		String filePath = getArgData().getCsvFileDir();

		final List<CsvFileConfigBean> confCsvList = getProp().getConfCsvList(getArgData().getTargetFileNameSet());
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
		log.info("import result start.++++++++++++++++++++++++++++++++++++++++++++++++++++");
		if(result.isEmpty()){
			log.info("nothing.");
		} else {
			for(String logtext : result.getLogs()){
				log.info(logtext);
			}
		}
		log.info("import result end.++++++++++++++++++++++++++++++++++++++++++++++++++++");
	}

	@Override
	protected void login() throws Exception {
		client.login();
	}
}
