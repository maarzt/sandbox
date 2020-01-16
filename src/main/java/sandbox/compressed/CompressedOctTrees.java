package sandbox.compressed;

import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.util.Util;
import sandbox.Cube;
import sandbox.IntervalMask;
import sandbox.Node;
import sandbox.OctTree;

public class CompressedOctTrees {

	static final IntType ZERO = new IntType(0);

	static final IntType ONE = new IntType(1);

	private static final BiMap< IntType > BINARY_VALUES =
			new BiMap< IntType >() {

				private final IntType[] values = { ZERO, ONE };

				@Override
				public int getIndex(IntType value) {
					return value.getInteger();
				}

				@Override
				public IntType getValue(int index) {
					return values[index];
				}
			};

	static ProxyNode< IntType > createRoot() {
		CompressedTree< IntType > compressedTree =
				new CompressedTree<>(BINARY_VALUES);
		return new ProxyNode<>(compressedTree);
	}

	public static OctTree< IntType > create(int depth, IntervalMask mask) {
		final Cube cube = new Cube(depth);
		ProxyNode< IntType > root = createRoot();
		fill(mask, cube, root);
		return new OctTree<>(depth, root.value());
	}

	public static OctTree< IntType > max(OctTree< IntType > a, OctTree< IntType > b) {
		if( a.getDepth() != b.getDepth() )
			throw new AssertionError();
		ProxyNode< IntType > root = createRoot();
		max(root, a.getRoot(), b.getRoot());
		return new OctTree<>(a.getDepth(), root.value());
	}

	private static void max(ProxyNode<IntType> result, Object a, Object b) {
		boolean aIsNode = a instanceof Node;
		boolean bIsNode = b instanceof Node;
		if (aIsNode) {
			if (bIsNode) {
				result.makeNode();
				for (int i = 0; i < 8; i++)
					max(result.childProxy(i), ((Node) a).child(i),
							((Node) b).child(i));
			}
			else {
				result.makeNode();
				for (int i = 0; i < 8; i++)
					max(result.childProxy(i), ((Node) a).child(i), b);
			}
		}
		else {
			if (bIsNode) {
				result.makeNode();
				for (int i = 0; i < 8; i++)
					max(result.childProxy(i), a, ((Node) b).child(i));
			}
			else {
				result.setValue(Util.max((IntType) a, (IntType) b));
			}
		}

	}

	private static void fill(IntervalMask mask, Cube cube,
			ProxyNode< IntType > node)
	{
		if (mask.contains(cube)) {
			node.setValue(ONE);
			return;
		}
		if (!mask.intersects(cube)) {
			node.setValue(ZERO);
			return;
		}
		node.makeNode();
		for (int i = 0; i < 8; i++) {
			fill(mask, cube.child(i), node.childProxy(i));
		}
	}
}
