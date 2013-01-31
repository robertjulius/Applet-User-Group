package id.co.mik.utility.applet.usergroup;

/**
 * @author Robert Julius
 */
public class BranchNode<T> extends java.util.Vector<T> {

	private static final long serialVersionUID = 1L;
	private String name;

	/**
	 * @param name
	 * @param elements
	 */
	public BranchNode(String name, T elements[]) {
		this.name = name;
		for (int i = 0, n = elements.length; i < n; i++) {
			add(elements[i]);
		}
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}
}
