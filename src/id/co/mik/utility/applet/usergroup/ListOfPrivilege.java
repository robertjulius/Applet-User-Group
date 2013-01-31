package id.co.mik.utility.applet.usergroup;

import java.util.HashMap;


public class ListOfPrivilege {
	
	private static final HashMap<String, Privilege> hashMap = new HashMap<String, Privilege>();
	
	static {		
		hashMap.put("000", new Privilege("000", "Nol Nol Nol", ""));
		hashMap.put("010", new Privilege("010", "Nol Satu Nol", "000"));
		hashMap.put("020", new Privilege("020", "Nol Dua Nol", "000"));
		hashMap.put("011", new Privilege("011", "Nol Satu Satu", "010"));
		hashMap.put("012", new Privilege("012", "Nol Satu Dua", "010"));
		hashMap.put("013", new Privilege("013", "Nol Satu Tiga", "010"));
		hashMap.put("021", new Privilege("021", "Nol Dua Satu", "020"));
		hashMap.put("022", new Privilege("022", "Nol Dua Dua", "020"));
	}
	
	public static HashMap<String, Privilege> getHashMap() {
		return hashMap;
	}
}
