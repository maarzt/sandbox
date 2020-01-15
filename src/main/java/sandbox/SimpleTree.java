package sandbox;

import java.util.Arrays;

public class SimpleTree {

	private final Object[] childs;

	public SimpleTree(Object... childs) {
		this.childs = childs;
	}

	public Object child(int i) {
		return childs[i];
	}

	@Override
	public String toString() {
		return Arrays.toString(childs);
	}
}
