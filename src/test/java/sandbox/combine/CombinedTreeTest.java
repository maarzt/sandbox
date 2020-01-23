package sandbox.combine;

import net.imglib2.Point;
import org.junit.Test;
import sandbox.OctTree;
import sandbox.OctTrees;
import sandbox.Sphere;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class CombinedTreeTest {

	@Test
	public void testEmptyTree() {
		CombinedTree<String> tree = new CombinedTree<>(1);
		Set<String> result = tree.getValue(new Point(0, 0, 0));
		assertEquals(Collections.emptySet(), result);
	}

	@Test
	public void testAddLabel() {
		CombinedTree<String> tree = new CombinedTree<>(1);
		Point center = new Point(0, 0, 0);
		OctTree<Boolean> labelTree = OctTrees.create(1, new Sphere(
				center, 0.5), true, false);
		tree.addLabel("a", labelTree);
		assertEquals(Collections.singleton("a"), tree.getValue(center));
	}
}
