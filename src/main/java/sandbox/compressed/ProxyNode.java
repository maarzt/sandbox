package sandbox.compressed;

import sandbox.Node;

public class ProxyNode< T > {

	private CompressedTree< T > tree;

	private int linkIndex;

	private int vector;

	private ProxyNode< T > child;

	private final Node node = new Node() {

		@Override
		public Node threadSafeCopy() {
			return (Node) new ProxyNode<>(ProxyNode.this).value();
		}

		@Override
		public Object child(int i) {
			return childProxy(i).value();
		}
	};

	private ProxyNode(ProxyNode< T > original) {
		this.tree = original.tree;
		this.linkIndex = original.linkIndex;
		this.vector = original.vector;
		this.child = null;
	}

	public ProxyNode(CompressedTree< T > tree) {
		this.tree = tree;
		this.vector = 0;
	}

	public void setValue(T value) {
		vector = - tree.valueMap.getIndex(value);
		tree.data.set(linkIndex, vector);
	}

	public void makeNode() {
		vector = tree.data.size();
		tree.data.set(linkIndex, vector);
		for (int i = 0; i < 8; i++) tree.data.add(0);
	}

	public ProxyNode< T > childProxy(int i) {
		if (isLeaf()) throw new AssertionError();
		if (child == null) child = new ProxyNode<>(tree);
		child.setLinkIndex(vector + i);
		return child;
	}

	public Object value() {
		if (isLeaf()) return tree.valueMap.getValue(-vector);
		else return node;
	}

	private void setLinkIndex(int linkIndex) {
		this.linkIndex = linkIndex;
		vector = tree.data.get(linkIndex);
	}

	public void tryCollapse() {
		if (isLeaf()) return;
		int childVector = tree.data.get(vector);
		if (childVector > 0) return;
		for (int i = 1; i < 8; i++)
			if (childVector != tree.data.get(vector + i)) return;
		vector = childVector;
		tree.data.set(linkIndex, vector);
	}

	private boolean isLeaf() {
		return vector <= 0;
	}
}
