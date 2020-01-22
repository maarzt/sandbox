package sandbox.lazy;

import sandbox.Node;
import sandbox.OctTree;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class LazyMergeTree<T> implements Node {

	private T value;

	private final BinaryOperator<T> operation;

	private final List<Node> nodes;

	private int index = 0;

	private LazyMergeTree<T> child;

	public static <T> OctTree<T> merge(T neutral, BinaryOperator<T> operation, List<OctTree<T>> trees)
	{
		List< Object > t = trees.stream().map(OctTree::getRoot)
				.collect(Collectors.toList());
		return new OctTree<>(trees.get(0).getDepth(),
				mergeTrees(neutral, operation, t));
	}

	public static <T> Object mergeTrees(T neutral,
			BinaryOperator< T > operation, List< Object > trees) {
		List< Node > nodes = new ArrayList<>();
		for(Object node : trees) {
			if(node instanceof Node)
				nodes.add((Node) node);
			else
				neutral = operation.apply(neutral, (T) node);
		}
		if(nodes.isEmpty())
			return neutral;
		else return new LazyMergeTree<>(neutral, operation, nodes);
	}

	private LazyMergeTree(T value, BinaryOperator< T > operation,
			List< Node > nodes) {
		this.value = value;
		this.operation = operation;
		this.nodes = nodes;
		this.index = -1;
		this.child = null;
	}

	private Object setChildIndex(int i, List<Node> parentNodes, T parentValue) {
		if (index != i) {
			value = parentValue;
			nodes.clear();
			for(Node parentNode : parentNodes) {
				Object node = parentNode.child(i);
				if(node instanceof Node)
					nodes.add((Node) node);
				else
					value = operation.apply(value, (T) node);
			}
			index = i;
		}
		if (nodes.isEmpty())
			return value;
		else
			return this;
	}

	@Override
	public Object child(int i) {
		if(child == null)
			child = new LazyMergeTree<>(null, operation, new ArrayList<>());
		return child.setChildIndex(i, nodes, value);
	}

	@Override
	public Node threadSafeCopy() {
		return new LazyMergeTree<>(value, operation, threadSafeCopy(nodes));
	}

	private List<Node> threadSafeCopy(List<Node> nodes) {
		List<Node> result = new ArrayList<>();
		for(Node node : nodes)
			result.add(node.threadSafeCopy());
		return result;
	}
}
