package sandbox.lazy;

import sandbox.Node;
import sandbox.OctTree;

import java.util.function.BiFunction;

public final class LazyMerge {

	private LazyMerge() {
		// prevent from instantiation
	}

	public static <A, B, C> OctTree<C> merge(OctTree< A > a, OctTree< B > b,
			BiFunction< A, B, C > operation)
	{
		return new OctTree<>(a.getDepth(),
				mergeTrees(a.getRoot(), b.getRoot(), operation));
	}

	static Object mergeTrees(Object a, Object b, BiFunction operation)
	{
		if( !isNode(a) && !isNode(b))
			return operation.apply(a, b);
		return new LazyNode(a, b, operation);
	}

	private static boolean isNode(Object a) {
		return a instanceof Node;
	}

	private static Object getChildOrValue(int i, Object a) {
		return isNode(a) ? ((Node) a).child(i) : a;
	}

	private static class LazyNode implements Node {

		private final Object a;

		private final Object b;

		private final BiFunction operation;

		private final Object[] children = new Object[8];

		public LazyNode(Object a, Object b, BiFunction operation) {
			this.a = a;
			this.b = b;
			this.operation = operation;
		}

		@Override
		public Object child(int i) {
			Object child = children[i];
			if(child == null) {
				Object childA = getChildOrValue(i, a);
				Object childB = getChildOrValue(i, b);
				child = mergeTrees(childA, childB, operation);
				children[i] = child;
			}
			return child;
		}
	}
}
