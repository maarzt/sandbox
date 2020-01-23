package sandbox.combine;

import sandbox.Cube;
import sandbox.Node;
import sandbox.OctTree;

import java.util.ArrayList;
import java.util.List;

public class CoveringCubes {

	/**
	 * Returns a list of at most cubes that together cover all true bits
	 * in the octree.
	 */
	public static List< Cube > calculate(OctTree< Boolean > octTree) {
		Cube cube = new Cube(octTree.getDepth());
		List< Cube > cubes = new ArrayList<>();
		visitNode(octTree.getRoot(), cube, 8, cubes);
		return cubes;
	}

	/**
	 * Visits the given node, and adds a limited number of cubes to the list.
	 * The cubes added to the list cover all true pixel represented by the
	 * the visited node and the corresponding subtree.
	 * @param node The node that is visited.
	 * @param cube The cube that corresponds to the node.
	 * @param limit This limits the number of cubes that are added to the list.
	 * @param cubes List of cubes to which the cubes are added.
	 */
	private static List< Cube > visitNode(Object node, Cube cube, int limit,
			List< Cube > cubes)
	{
		if (node instanceof Node) {
			int bitmask = activeChildren((Node) node);
			int numChildren = Integer.bitCount(bitmask);
			if (numChildren <= limit) {
				for (int i = 0; i < 8; i++) {
					if (((bitmask >> i) & 1) == 1)
						visitNode(((Node) node).child(i), cube.child(i),
								limit / numChildren, cubes);
				}
				return cubes;
			}
		}
		cubes.add(cube.threadSafeCopy());
		return cubes;
	}

	/**
	 * Return a bitmask of active children of the Node.
	 * A child is considered active if it's not equal to false.
	 * If the i-th child is active the i-th bit in the return int is set.
	 */
	private static int activeChildren(Node node) {
		int bitmask = 0;
		for (int i = 0; i < 8; i++) {
			if (!node.child(i).equals(false)) bitmask |= 1 << i;
		}
		return bitmask;
	}
}
