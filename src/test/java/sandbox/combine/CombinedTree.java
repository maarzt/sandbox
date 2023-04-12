package sandbox.combine;

import gnu.trove.list.array.TIntArrayList;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.Localizable;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.converter.Converters;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.util.Localizables;
import net.imglib2.view.Views;
import sandbox.Cube;
import sandbox.Node;
import sandbox.OctTree;
import sandbox.Sizes;
import sandbox.mapping.MappedBinaryOperator;
import sandbox.mapping.MappedUnaryOperator;
import sandbox.mapping.ValueMapping;

import java.util.List;
import java.util.Set;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

public class CombinedTree< L > {

	private final ValueMapping< TinySet > mapping = initValueMapping();

	private final IntBinaryOperator union =
			new MappedBinaryOperator<>(TinySet::union, mapping);

	private ValueMapping< TinySet > initValueMapping() {
		ValueMapping< TinySet > mapping = new ValueMapping<>();
		if (0 != mapping.getIndex(TinySet.emptySet()))
			throw new AssertionError();
		return mapping;
	}

	private final int depth;

	private final TIntArrayList data = new TIntArrayList();

	private final ThreadLocal< ProxyNode > root =
			ThreadLocal.withInitial(() -> new ProxyNode(data));

	public CombinedTree(int depth) {
		this.depth = depth;
	}

	public Set< L > getValue(Localizable position) {
		throw new UnsupportedOperationException();
	}

	public int getValueIndex(Localizable position) {
		ProxyNode node = root.get();
		long x = position.getLongPosition(0);
		long y = position.getLongPosition(1);
		long z = position.getLongPosition(2);
		int setIndex = node.getValue();
		for (int d = depth - 1; d >= 0 && node.isNode(); d--) {
			long bitX = (x >> d) & 1;
			long bitY = (y >> d) & 1;
			long bitZ = (z >> d) & 1;
			long index = bitX | (bitY << 1) | (bitZ << 2);
			node = node.child((int) index);
			setIndex = setUnionOnIndices(setIndex, node.getValue());
		}
		if (node.isLeaf()) return setIndex;
		throw new AssertionError("Tree is to deep.");
	}

	private int setUnionOnIndices(int setIndex, int value) {
		return (setIndex == 0) ? value : (value == 0 ? setIndex : union.applyAsInt(setIndex, value));
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
			return TinySet.add(set, (Integer) value);
		}, mapping);
		List< Cube > cubes = CoveringCubes.calculate(tree);
		for (Cube cube : cubes) {
			Object labelRoot = tree.getRoot();
			ProxyNode rootNode = root.get();
			long x = cube.min(0);
			long y = cube.min(1);
			long z = cube.min(2);
			for (int d = depth - 1; d >= cube.getDepth(); d--) {
				long bitX = (x >> d) & 1;
				long bitY = (y >> d) & 1;
				long bitZ = (z >> d) & 1;
				long index = bitX | (bitY << 1) | (bitZ << 2);
				labelRoot = ((Node) labelRoot).child((int) index);
				rootNode.makeNode();
				rootNode = rootNode.child((int) index);
			}
			recursiveAddLabel(rootNode, labelRoot, addLabel);
		}
	}

	private void recursiveAddLabel(ProxyNode node, Object labelNode,
			IntUnaryOperator addLabel)
	{
		if (labelNode instanceof Node) {
			node.makeNode();
			node.setOtherValue(addLabel.applyAsInt(node.getOtherValue()));
			for (int i = 0; i < 8; i++)
				recursiveAddLabel(node.child(i), ((Node) labelNode).child(i),
						addLabel);
		}
		else if (labelNode.equals(true))
			node.setValue(addLabel.applyAsInt(node.getValue()));
	}

	public void memoryStatistic() {
		Sizes.printSize(mapping);
		Sizes.printSize(union);
		data.trimToSize();
		Sizes.printSize(data);
		System.out.println("data size:" + data.size());
		int counter = 0;
		long sum = 0;
		int max = 0;
		try {
			while (true) {
				int size = mapping.getValue(counter++).size();
				sum += size;
				max = Math.max(size, max);
			}
		}
		catch (Exception e)
		{
			System.out.println("Number of sets:" + counter);
			System.out.println("Average set size:" + (double) sum / counter);
			System.out.println("Max set size:" + max);
		}
	}
}
