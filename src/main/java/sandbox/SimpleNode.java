package sandbox;

import java.util.Arrays;

public class SimpleNode implements Node {

	private final Object[] childs;

	public SimpleNode(Object... childs) {
		this.childs = childs;
	}

	@Override
	public Object child(int i) {
		return childs[i];
	}

	@Override
	public String toString() {
		return Arrays.toString(childs);
	}
}
