package id.co.mik.utility.applet.usergroup;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * @author Robert Julius
 */
public class TreeNodeUtils {
	
	/**
	 * @param node
	 * @return
	 */
	public static Object[] generateCheckBoxTreeNode(TreeMap<String, Privilege> node) {

		Collection<Privilege> nodes = node.values();
		Object[] result = new Object[nodes.size()];

		Iterator<Privilege> iterator = nodes.iterator();
		for (int i=0 ; iterator.hasNext() ; ++i) {

			Privilege privilege = (Privilege) iterator.next();			
			if (privilege.getChilds().isEmpty()) {
				result[i] = new CheckBoxNode<Privilege>(privilege, false);
			} else {
				result[i] = new BranchNode<Object>(privilege.getName(), generateCheckBoxTreeNode(privilege.getChilds()));
			}
		}

		return result;
	}
	
	public static Object[] generateCommonTreeNode(TreeMap<String, Privilege> node) {

		Collection<Privilege> nodes = node.values();
		Object[] result = new Object[nodes.size()];

		Iterator<Privilege> iterator = nodes.iterator();
		for (int i=0 ; iterator.hasNext() ; ++i) {

			Privilege privilege = (Privilege) iterator.next();			
			if (privilege.getChilds().isEmpty()) {
				result[i] = privilege;
			} else {
				result[i] = new BranchNode<Object>(privilege.getName(), generateCommonTreeNode(privilege.getChilds()));
			}
		}

		return result;
	}
}
