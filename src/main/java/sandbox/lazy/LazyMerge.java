package sandbox.lazy;

import sandbox.Node;

import java.util.function.BiFunction;

public interface LazyMerge {

	static Object merge(Object a, Object b,
			BiFunction operation)
	{
		boolean aIsNode = a instanceof Node;
		boolean bIsNode = b instanceof Node;
		if( ! aIsNode && ! bIsNode )
			return operation.apply(a, b);
		return new LazyNode(a, b, operation);
	}

	class LazyNode implements Node {

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
				child = merge(childA, childB, operation);
				children[i] = child;
			}
			return child;
		}

		private Object getChildOrValue(int i, Object a) {
			return a instanceof Node ? ((Node) a).child(i) : a;
		}
	}
}
