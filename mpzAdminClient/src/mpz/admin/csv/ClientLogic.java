package mpz.admin.csv;

import mpz.admin.csv.impl.ExportLogic;
import mpz.admin.csv.impl.ImportLogic;
import mpz.admin.csv.impl.ImportLogic2;
import mpz.admin.util.ConfProperties;
import mpz.admin.util.ExecuteType;

/**
 * クライアント処理
 * @author tsutomu.kobayashi
 *
 */
public abstract class ClientLogic {
	private final ToolArgData argData;
	private final HttpCilentInput input;
	private final ConfProperties prop;
	/**
	 * コンストラクタ
	 * @param argData
	 * @param input
	 * @param prop
	 */
	protected ClientLogic(final ToolArgData argData,final HttpCilentInput input,ConfProperties prop){
		this.argData = argData;
		this.input = input;
		this.prop = prop;
	}
	public ToolArgData getArgData() {
		return argData;
	}
	public HttpCilentInput getInput() {
		return input;
	}
	public ConfProperties getProp() {
		return prop;
	}
	/**
	 * ログイン
	 * @throws Exception
	 */
	protected abstract void login() throws Exception;
	/**
	 * 処理
	 * @throws Exception
	 */
	protected abstract void execute() throws Exception;

	/**
	 * インスタンス取得
	 * @param argData
	 * @param input
	 * @param prop
	 * @return
	 */
	static ClientLogic getInstance(final ToolArgData argData,final HttpCilentInput input,ConfProperties prop){
		final ExecuteType type = argData.getExecuteType();

		if( type == ExecuteType.IMPORT || type == ExecuteType.IMPORT_CLEAR )
			return new ImportLogic(argData,input,prop);
		if( type == ExecuteType.IMPORT_EXPORT_CLEAR )
			return new ImportLogic2(argData,input,prop);
		if( type == ExecuteType.EXPORT )
			return new ExportLogic(argData,input,prop);
		return null;
	}

}
