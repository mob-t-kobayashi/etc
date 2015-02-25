package mpz.admin.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Json作成用
 * @author tsutomu.kobayashi
 *
 */
public class CreatJson {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		createJson(0);
	}

	private static void createJson(final int type){
		if(type == 0){
			//conf.properties#csv.file.json
			List<CsvFileConfigBean> list = new ArrayList<CsvFileConfigBean>();
			list.add(new CsvFileConfigBean("G_GODDESS.csv","GoddessData","G_GODDESS",true));
			list.add(new CsvFileConfigBean("G_DIVER_SKILL.csv","GameSkillData","G_DIVER_SKILL",true));
			Gson gson = new Gson();
			Type t = new TypeToken<List<CsvFileConfigBean>>(){}.getType();
			String s=gson.toJson(list, t);
			System.out.println(s);
			List<CsvFileConfigBean> l=gson.fromJson(s, t);
			for(CsvFileConfigBean e :l){
				System.out.println(e.getCsvName()+":"+e.getReqImp());
			}
		} else {
			//パラメータ
			Map<String,List<String>> ml = new HashMap<String,List<String>>();

			List<String> csvList = new ArrayList<String>();
			ml.put("game", csvList);
			csvList.add("G_DIVER.csv");
			csvList.add("G_DIVER_EXP_REINFORCE_TABLE.csv");
			csvList.add("G_DIVER_EXP_TABLE.csv");
			csvList.add("G_DIVER_SKILL.csv");
			csvList.add("G_DIVER_SKILL_LEADER.csv");
			csvList.add("G_DIVER_SKILL_RATE_REINFORCE_TABLE.csv");

			Gson gson = new Gson();
			Type t = new TypeToken<Map<String,List<String>>>(){}.getType();
			String s=gson.toJson(ml, t);
			System.out.println(s);
		}
	}

}
