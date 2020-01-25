package sandbox.combine;

import gnu.trove.list.array.TIntArrayList;
import sandbox.Cube;

public class ProxyNode {

	private TIntArrayList data;

	private int linkIndex;

	private int vector;

	private ProxyNode child;

	public ProxyNode(TIntArrayList data) {
		this.data = data;
		if(data.size() == 0)
			data.add(0);
		this.linkIndex = 0;
		this.vector = data.get(0);
	}

	public void setValue(int value) {
		if(isLeaf()) {
			vector = -value;
			data.set(linkIndex, vector);
		}
		else
			data.set(vector + 8, value);
	}

	public void makeNode() {
		if(isNode())
			return;
		int value = -vector;
		vector = data.size();
		data.set(linkIndex, vector);
		for (int i = 0; i < 8; i++) data.add(0);
		data.add(value);
		data.add(0);
	}

	public ProxyNode child(int i) {
		if (isLeaf()) throw new AssertionError();
		if (child == null) child = new ProxyNode(data);
		child.setLinkIndex(vector + i);
		return child;
	}

	public int getValue() {
		if (isLeaf()) return -vector;
		else return data.get(vector + 8);
	}

	public void setOtherValue(int value) {
		data.set(vector + 9, value);
	}

	public int getOtherValue() {
		return data.get(vector + 9);
	}

	private void setLinkIndex(int linkIndex) {
		this.linkIndex = linkIndex;
		vector = data.get(linkIndex);
	}

	public boolean isLeaf() {
		return vector <= 0;
	}

	public boolean isNode() {
		return ! isLeaf();
	}
}
