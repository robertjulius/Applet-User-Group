package id.co.mik.utility.applet.usergroup;

/**
 * @author Robert Julius
 */
/**
 * @author ADMIN
 *
 * @param <T>
 */
public class CheckBoxNode<T> {

	private T value;
	private boolean selected;

	/**
	 * @param value
	 */
	public CheckBoxNode(T value, boolean selected) {
		this.value = value;
		this.selected = selected;
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * @return
	 */
	public T getValue() {
		return value;
	}

	/**
	 * @param value
	 */
	public void setValue(T value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
}