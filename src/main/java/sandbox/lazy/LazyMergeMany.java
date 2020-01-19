package sandbox.lazy;

import sandbox.Node;
import sandbox.OctTree;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public final class LazyMergeMany {

	public static <T> OctTree<T> merge(T neutral, BinaryOperator<T> operation, List<OctTree<T>> trees)
	{
		List< Object > t = trees.stream().map(OctTree::getRoot)
				.collect(Collectors.toList());
		return new OctTree<>(trees.get(0).getDepth(),
				mergeTrees(neutral, operation, t));
	}

	static <T> Object mergeTrees(T neutral, BinaryOperator< T > operation,
			List< Object > trees)
	{
		List<Node> nodes = new ArrayList<>();
		T value = neutral;
		for(Object tree : trees) {
			if( tree instanceof Node )
				nodes.add((Node) ((Node) tree).threadSafeCopy());
			else
				value = operation.apply(value, (T) tree);
		}
		if(nodes.isEmpty())
			return value;
		return new LazyNode<>(value, nodes, operation);
	}

	private static class LazyNode<T> implements Node {

		private final T value;

		private final List<Node> nodes;

		private final BinaryOperator<T> operation;

		private final Object[] children = new Object[8];

		public LazyNode(T value, List<Node> nodes, BinaryOperator<T> operation) {
			this.value = value;
			this.nodes = nodes;
			this.operation = operation;
		}

		@Override
		public Object child(int i) {
			Object child = children[i];
			if(child == null) {
				synchronized (children) {
					List< Object > c = new ArrayList<>(nodes.size());
					for (Node node : nodes) c.add(node.child(i));
					child = LazyMergeMany.mergeTrees(value, operation, c);
					children[i] = child;
				}
			}
			return child;
		}
	}
}
