package mpz.admin.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 処理結果
 * @author tsutomu.kobayashi
 *
 */
public class Result {
	private List<String> okList = new ArrayList<String>();
	private List<String> ngList = new ArrayList<String>();

	/**
	 * 成功ファイル名
	 * @param name
	 */
	public void addOk(final String name){
		okList.add(name);
	}

	/**
	 * 失敗ファイル名
	 * @param name
	 */
	public void addNg(final String name){
		ngList.add(name);
	}

	/**
	 * 空判定
	 * @return
	 */
	public boolean isEmpty(){
		return okList.isEmpty() && ngList.isEmpty();
	}

	/**
	 * 結果
	 * @return
	 */
	public List<String> getLogs(){
		List<String> list = new ArrayList<String>();
		for(String s : okList)
			list.add(s +":OK");
		for(String s : ngList)
			list.add(s +":NG");
		StringBuffer sb = new StringBuffer();
		sb.append("TOTAL:");
		sb.append(okList.size() + ngList.size());
		sb.append(" OK:");
		sb.append(okList.size());
		sb.append(" NG:");
		sb.append(ngList.size());
		list.add(sb.toString());
		return list;
	}
}
