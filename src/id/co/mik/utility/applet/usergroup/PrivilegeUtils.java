package id.co.mik.utility.applet.usergroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;


public class PrivilegeUtils {
	
	private static final TreeMap<String, Privilege> allMenus = new TreeMap<String, Privilege>();
	
	static {
		createTreeMap(allMenus);
	}
	
	public static void createTreeMap(TreeMap<String, Privilege> treeMap) {
		HashMap<String, Privilege> hashMap = ListOfPrivilege.getHashMap();
		
		Iterator<String> iterator = hashMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			Privilege child = hashMap.get(key);
			Privilege parent = hashMap.get(child.getParentId());
			if (parent != null) {
				parent.addChild(child);
			} else {
				treeMap.put(child.getId(), child);
			}
		}
	}
	
	public static TreeMap<String, Privilege> generateReadOnlyTree(String[] privilegeIds) {
		TreeMap<String, Privilege> treeMap = new TreeMap<String, Privilege>();
		createTreeMap(treeMap);
		
		ArrayList<Privilege> leafs = new ArrayList<Privilege>();
		getLeafs(leafs, treeMap);
		
		while (!isEquals(privilegeIds, leafs)) {
			for (Privilege leaf : leafs) {
				if (Arrays.binarySearch(privilegeIds, leaf.getId()) < 0) {
					Privilege parent = getPrivilegeFromTree(leaf.getParentId(), treeMap);
					parent.getChilds().remove(leaf.getId());
				}
			}
			leafs.clear();
			getLeafs(leafs, treeMap);
			
		}
		
		return treeMap;
	}
	
	public static TreeMap<String, Privilege> getAllMenus() {
		return allMenus;
	}
	
	public static void getLeafs(ArrayList<Privilege> leafs, TreeMap<String, Privilege> treeMap) {
		Iterator<String> iterator = treeMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			Privilege privilege = treeMap.get(key);
			if (privilege.getChilds().isEmpty()) {
				leafs.add(privilege);
			} else {
				getLeafs(leafs, privilege.getChilds());
			}
		}
	}
	
	public static Privilege getPrivilegeFromTree(String key, TreeMap<String, Privilege> treeMap) {
		if (treeMap.containsKey(key)) {
			return treeMap.get(key);
		}
		
		Iterator<String> iterator = treeMap.keySet().iterator();
		while (iterator.hasNext()) {
			String privilegeId = (String) iterator.next();
			Privilege privilege = treeMap.get(privilegeId);
			return getPrivilegeFromTree(key, privilege.getChilds());
		}
		
		return null;
	}
	
	public static ArrayList<Privilege> getPrivileges(String[] privilegeIds) {
		ArrayList<Privilege> privilege = new ArrayList<Privilege>();
		for (String privilegeId : privilegeIds) {
			privilege.add(ListOfPrivilege.getHashMap().get(privilegeId));
		}
		return privilege;
	}
	
	public static boolean isEquals(String[] privilegeIds, ArrayList<Privilege> leafs) {
		if (privilegeIds.length != leafs.size()) {
			return false;
		}
		
		int equals = 0;
		for (String privilegeId : privilegeIds) {
			for (Privilege leaf : leafs) {
				if (leaf.getId().equals(privilegeId)) {
					equals += 1;
					break;
				}
			}
		}
		
		return equals == leafs.size(); 
	}
	
	public static boolean isExists(String key, TreeMap<String, Privilege> treeMap) {
		if (treeMap.containsKey(key)) {
			return true;
		} else {
			Iterator<String> iterator = treeMap.keySet().iterator();
			while (iterator.hasNext()) {
				String childKey = (String) iterator.next();
				Privilege child = treeMap.get(childKey);
				TreeMap<String, Privilege> childs = child.getChilds();
				return isExists(key, childs);
			}
			return false;
		}
	}
}
