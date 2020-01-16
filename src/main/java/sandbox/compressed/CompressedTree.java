package sandbox.compressed;

import gnu.trove.list.array.TIntArrayList;

public class CompressedTree< T > {

	final TIntArrayList data;

	final BiMap< T > valueMap;

	public CompressedTree(BiMap< T > valueMap) {
		this.data = new TIntArrayList();
		this.data.add(0);
		this.valueMap = valueMap;
	}
}
