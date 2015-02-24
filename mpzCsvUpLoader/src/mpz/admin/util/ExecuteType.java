package mpz.admin.util;

/**
 * 処理種別判定定数
 * @author tsutomu.kobayashi
 *
 */
public enum ExecuteType {
	IMPORT(0,"import"),
	IMPORT_CLEAR(1,"import-clear"),
	IMPORT_EXPORT_CLEAR(2,"import-export-clear"),
//	CLEAR(3,"clear"),
	EXPORT(4,"export"),
	INVALID(-1,"invalid"),
	;
	private final int code;
	private final String name;

	ExecuteType(final int code,final String name){
		this.code = code;
		this.name = name;
	}

	public static ExecuteType getCode(String strCode){
		for(ExecuteType c : ExecuteType.values()){
			if(c.getName().equals(strCode))
				return c;
		}
		return INVALID;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public boolean isInValid(){
		return this == INVALID;
	}
//	public boolean isExecuteImport(){
//		return this == IMPORT_CLEAR || this == IMPORT;
////		return this == IMPORT_CLEAR || this == IMPORT || this == IMPORT_CLEAR2;
//	}
//	public boolean isExecuteExport(){
//		return this == EXPORT;
//	}
	public boolean isExecuteClear(){
		return this == IMPORT_CLEAR  || this == IMPORT_EXPORT_CLEAR;
	}
//
//	public boolean isImportClear2(){
//		return this == IMPORT_CLEAR2;
//	}
}
