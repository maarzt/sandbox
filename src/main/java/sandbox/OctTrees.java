package sandbox;

import net.imglib2.type.numeric.integer.IntType;

import java.util.function.BiFunction;
import java.util.function.Predicate;

public class OctTrees {

	private static final IntType one = new IntType(1);

	private static final IntType zero = new IntType(0);

	public static OctTree< IntType > create(int depth, IntervalMask intervalMask) {
		IntType one = OctTrees.one;
		IntType zero = OctTrees.zero;
		return create(depth, intervalMask, one, zero);
	}

	public static OctTree< IntType > create(int depth,
			IntervalMask intervalMask, IntType foreground, IntType background)
	{
		return new OctTree<>(depth, interval -> {
			if (intervalMask.contains(interval)) return foreground;
			if (!intervalMask.intersects(interval)) return background;
			return null;
		});
	}

	public static <A, B, C> OctTree< C > merge(OctTree< A > a, OctTree< B > b, BiFunction<A, B, C> operation, Predicate<A> exitA, Predicate<B> exitB) {
		int depth = a.getDepth();
		if( depth != b.getDepth() )
			throw new AssertionError("Both trees must have the same depth.");
		Object root = new SimpleTreeMerger(operation, exitA, exitB).merge(a.getRoot(), b.getRoot());
		return new OctTree<>(depth, root);
	}

	public static OctTree< IntType > max(OctTree<IntType> a, OctTree<IntType> b) {
		Predicate< IntType > exit = a1 -> a1.getInteger() <= 0;
		return merge(a,b, (a1, b1) -> a1.getInteger() > b1.getInteger() ? a1 : b1, exit, exit);
	}

}
