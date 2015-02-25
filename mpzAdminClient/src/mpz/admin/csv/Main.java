package mpz.admin.csv;

import mpz.admin.util.ConfProperties;

import org.apache.log4j.Logger;

/**
 * 起動
 * @author tsutomu.kobayashi
 *
 */
public class Main {
	private static final Logger log = Logger.getLogger( Main.class );

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		log.info("ver." + Constant.VERSION);

		try{
			log.info("load properties start ******************");
			//設定ファイル読み込み
			ConfProperties prop = new ConfProperties();
			prop.init();
			log.info("load properties end ******************");

			//パラメータチェック
			ToolArgData argData = new ToolArgData(args,prop);
			if(!argData.validate())
				return;
			argData.showParamInfo();

			final HttpCilentInput input = new HttpCilentInput(
					argData.getUserId(),
					argData.getUserPassword(),
					argData.getToolUrl(),
					prop.getTimeOut());
			final ClientLogic clientLogic = ClientLogic.getInstance(argData, input, prop);
			log.info("login start ******************");
			clientLogic.login();
			log.info("login end ******************");
			log.info("logic start ******************");
			clientLogic.execute();
			log.info("logic end ******************");
			log.info("finish.");
		}catch(Exception e){
			log.error(e);
		}
	}
}
