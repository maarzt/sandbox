package sandbox;

import java.util.Arrays;

public class Branch<T> implements Node< T > {

	private final Node[] childs;

	public Branch(Node<T>... childs) {
		this.childs = childs;
	}

	public Node<T> child(int i) {
		return childs[i];
	}

	@Override
	public String toString() {
		return Arrays.toString(childs);
	}
}
