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
 * CSVファイルエクスポート
 * @author tsutomu.kobayashi
 *
 */
public class ExportLogic extends ClientLogic{

	private final static String dateString;
	static {
		dateString = (new SimpleDateFormat("yyyyMMddHHmm")).format(new Date());
	}

	private final MpzAdminCsvClient client;
	public ExportLogic(ToolArgData argData, HttpCilentInput input, ConfProperties prop) {
		super(argData, input, prop);
		this.client = new MpzAdminCsvClient(input);
	}

	private static final Logger log = Logger.getLogger( ExportLogic.class );

	@Override
	protected void execute() throws Exception {
		final Result result = new Result();
		log.info("export start ******************");
		//CSVファイルディレクトリ取得
		String path = getArgData().getCsvFileDir();
		final File exportDir = new File(path +
				(path.endsWith(File.separator) ? "" : File.separator) +
				getArgData().getToolType() +"_" + dateString );
		exportDir.mkdir();
		final String filePath = exportDir.getAbsolutePath();
		log.info("output directory =" + exportDir.getAbsolutePath());
		final List<CsvFileConfigBean> confCsvList = getProp().getConfCsvList(getArgData().getTargetFileNameSet());
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
				log.info(f.getFile().getAbsolutePath()+": export.");
				if( client.export(f.getFile(),f.getReqExport()) ){
					result.addOk(f.getName());
				} else {
					result.addNg(f.getName());
				}
			}catch(Exception e){
				result.addNg(f.getName());
				log.error(e);
			}
		}
		log.info("export end ******************");
		log.info("export result start.++++++++++++++++++++++++++++++++++++++++++++++++++++");
		if(result.isEmpty()){
			log.info("nothing.");
		} else {
			for(String logtext : result.getLogs()){
				log.info(logtext);
			}
		}
		log.info("export result end.++++++++++++++++++++++++++++++++++++++++++++++++++++");
		log.info("export directory:" + exportDir.getAbsolutePath());
	}

	@Override
	protected void login() throws Exception {
		if(!client.login()){
			throw new Exception("login error");
		}
	}
}
