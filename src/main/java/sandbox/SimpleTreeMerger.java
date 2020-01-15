package sandbox;

import java.util.function.BiFunction;
import java.util.function.Predicate;

class SimpleTreeMerger {

	private final BiFunction operation;

	private final Predicate exit;

	public SimpleTreeMerger(BiFunction operation, Predicate exit) {
		this.operation = operation;
		this.exit = exit;
	}

	public Object merge(Object treeA, Object treeB) {
		boolean aIsBranch = treeA instanceof Node;
		boolean bIsBranch = treeB instanceof Node;
		if (aIsBranch) {
			if (bIsBranch)
				return internMerge((Node) treeA, (Node) treeB);
			else return internMerge((Node) treeA, treeB);
		}
		else {
			if (bIsBranch) return internMerge(treeA, (Node) treeB);
			else return internMerge(treeA, treeB);
		}
	}

	private Object internMerge(Node treeA, Node treeB) {
		Object[] childs = new Object[8];
		for (int i = 0; i < 8; i++)
			childs[i] = merge(treeA.child(i), treeB.child(i));
		return new SimpleNode(childs);
	}

	private Object internMerge(Node treeA, Object valueB) {
		if(exit.test(valueB))
			return treeA;
		Object[] childs = new Object[8];
		for (int i = 0; i < 8; i++) childs[i] = merge(treeA.child(i), valueB);
		return new SimpleNode(childs);
	}

	private Object internMerge(Object valueA, Node treeB) {
		if(exit.test(valueA))
			return treeB;
		Object[] childs = new Object[8];
		for (int i = 0; i < 8; i++) childs[i] = merge(valueA, treeB.child(i));
		return new SimpleNode(childs);
	}

	private Object internMerge(Object valueA, Object valueB) {
		return operation.apply(valueA, valueB);
	}
}
