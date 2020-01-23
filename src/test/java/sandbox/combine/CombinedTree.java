package sandbox.combine;

import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.Localizable;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.converter.Converters;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.util.Localizables;
import net.imglib2.view.Views;
import sandbox.Node;
import sandbox.OctTree;
import sandbox.mapping.MappedBinaryOperator;
import sandbox.mapping.MappedUnaryOperator;
import sandbox.mapping.ValueMapping;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

public class CombinedTree< L > {

	private final ValueMapping< Set< L > > mapping = initValueMapping();

	private final IntBinaryOperator union =
			new MappedBinaryOperator<>(CombinedTree::setUnion, mapping);

	private static < L > Set< L > setUnion(Set< L > a, Set< L > b) {
		HashSet< L > r = new HashSet<>(a);
		r.addAll(b);
		return r;
	}

	private ValueMapping< Set< L > > initValueMapping() {
		ValueMapping< Set< L > > mapping = new ValueMapping<>();
		if (0 != mapping.getIndex(Collections.emptySet()))
			throw new AssertionError();
		return mapping;
	}

	private final int depth;

	private final MyNode root = new MyNode();

	public CombinedTree(int depth) {
		this.depth = depth;
	}

	public Set< L > getValue(Localizable position) {
		return mapping.getValue(getValueIndex(position));
	}

	public int getValueIndex(Localizable position) {
		MyNode node = root;
		long x = position.getLongPosition(0);
		long y = position.getLongPosition(1);
		long z = position.getLongPosition(2);
		int setIndex = root.leafLabels;
		for (int d = depth - 1; d >= 0 && node.isNode(); d--) {
			long bitX = (x >> d) & 1;
			long bitY = (y >> d) & 1;
			long bitZ = (z >> d) & 1;
			long index = bitX | (bitY << 1) | (bitZ << 2);
			node = node.child((int) index);
			setIndex = union.applyAsInt(setIndex, node.leafLabels);
		}
		if (node.isLeaf()) return setIndex;
		throw new AssertionError("Tree is to deep.");
	}

	public RandomAccessibleInterval< IntType > indexImage() {
		RandomAccessible< Localizable > positions =
				Localizables.randomAccessible(3);
		RandomAccessible< IntType > indexImage = Converters.convert(positions,
				(position, o) -> o.setInteger(getValueIndex(position)),
				new IntType());
		Interval interval = new FinalInterval(1 << depth, 1 << depth, 1 << depth);
		return Views.interval(indexImage, interval);
	}

	public void addLabel(L value, OctTree< Boolean > tree) {
		IntUnaryOperator addLabel = new MappedUnaryOperator<>(set -> {
			HashSet< L > r = new HashSet<>(set);
			r.add(value);
			return r;
		}, mapping);
		Object labelRoot = tree.getRoot();
		recursiveAddLabel(root, labelRoot, addLabel);
	}

	private void recursiveAddLabel(MyNode node, Object labelNode,
			IntUnaryOperator addLabel)
	{
		if (labelNode instanceof Node) {
			node.makeNode();
			//FIXME: This needs the idea of covering cubes
			//node.nodeLabels = addLabel.applyAsInt(node.nodeLabels);
			for (int i = 0; i < 8; i++)
				recursiveAddLabel(node.child(i), ((Node) labelNode).child(i),
						addLabel);
		}
		else if (labelNode.equals(true))
			node.leafLabels = addLabel.applyAsInt(node.leafLabels);
	}

	private class MyNode {

		private int leafLabels = 0;

		private int nodeLabels = 0;

		private Object[] children = null;

		public boolean isLeaf() {
			return children == null;
		}

		public boolean isNode() {
			return !isLeaf();
		}

		public void makeNode() {
			if (children == null) children =
					new Object[] { new MyNode(), new MyNode(), new MyNode(),
							new MyNode(), new MyNode(), new MyNode(),
							new MyNode(), new MyNode() };
		}

		/**
		 * Returns the set of all labels, that have this node as a leaf node
		 * with value true;
		 */
		public Set< L > getContainingLabels() {
			return mapping.getValue(nodeLabels);
		}

		/**
		 * Returns the set of all labels, that have this node as an none leaf
		 * node and the node is under a subtree specified by the covering cubes.
		 */
		public Set< L > getIntersectingLabels() {
			return mapping.getValue(leafLabels);
		}

		public MyNode child(int i) {
			return (MyNode) children[i];
		}
	}
}
