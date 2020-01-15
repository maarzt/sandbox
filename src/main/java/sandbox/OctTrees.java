package sandbox;

import net.imglib2.RealPoint;
import net.imglib2.type.numeric.integer.IntType;

import java.util.function.BiFunction;

public class OctTrees {

	private static final IntType one = new IntType(1);

	private static final IntType zero = new IntType(0);

	public static OctTree< IntType > create(int depth, IntervalMask intervalMask) {
		return new OctTree<>(depth, interval -> {
			if (intervalMask.contains(interval)) return one;
			if (!intervalMask.intersects(interval)) return zero;
			return null;
		});
	}

	public static <A, B, C> OctTree< C > merge(OctTree< A > a, OctTree< B > b, BiFunction<A, B, C> operation) {
		int depth = a.getDepth();
		if( depth != b.getDepth() )
			throw new AssertionError("Both trees must have the same depth.");
		Object root = new SimpleTreeMerger(operation).merge(a.getRoot(), b.getRoot());
		return new OctTree<>(depth, root);
	}

	public static OctTree< IntType > max(OctTree<IntType> a, OctTree<IntType> b) {
		return merge(a,b, (a1, b1) -> a1.getInteger() > b1.getInteger() ? a1 : b1);
	}

}
