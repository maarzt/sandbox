package sandbox;

import java.util.Arrays;

public class Node {

	private final Object[] childs;

	public Node(Object... childs) {
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
