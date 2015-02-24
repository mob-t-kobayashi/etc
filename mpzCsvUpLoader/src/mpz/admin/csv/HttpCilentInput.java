package mpz.admin.csv;

/**
 * クライアント入力
 * @author tsutomu.kobayashi
 *
 */
public class HttpCilentInput {
	private final String user;
	private final String password;
	private final String domain;

	private final int timeout;
	/**
	 * コンストラクタ
	 * @param user
	 * @param password
	 * @param domain
	 * @param timeout
	 */
	public HttpCilentInput(final String user,final String password,final String domain,final int timeout){
		this.user = user;
		this.password = password;
		this.domain = domain + (domain.endsWith("/") ? "" : "/");
		this.timeout = timeout;
	}
	public String getUser() {
		return user;
	}
	public String getPassword() {
		return password;
	}
	public String getDomain() {
		return domain;
	}

	public int getTimeout() {
		return timeout;
	}
	/**
	 * URI編集
	 * @param context
	 * @return
	 */
	public String getUri(final String context) {
		return domain + context;
	}

}
