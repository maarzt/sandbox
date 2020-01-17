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

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Node))
			return false;
		Node node = (Node) obj;
		for (int i = 0; i < 8; i++) {
			if(!child(i).equals(node.child(i)))
				return false;
		}
		return true;
	}
}
