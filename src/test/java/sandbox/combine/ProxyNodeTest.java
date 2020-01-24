package sandbox.combine;

import gnu.trove.list.array.TIntArrayList;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ProxyNodeTest {

	@Test
	public void testSetAndGetValue() {
		TIntArrayList data = new TIntArrayList();
		ProxyNode p = new ProxyNode(data);
		p.setValue(42);
		assertEquals(42, p.getValue());
	}

	@Test
	public void testMakeNode() {
		TIntArrayList data = new TIntArrayList();
		ProxyNode p = new ProxyNode(data);
		p.setValue(42);
		assertTrue(p.isLeaf());
		p.makeNode();
		assertTrue(p.isNode());
		assertEquals(42, p.getValue());
	}

	@Test
	public void testRecreateRootNode() {
		TIntArrayList data = new TIntArrayList();
		ProxyNode root = new ProxyNode(data);
		root.setValue(42);
		root.makeNode();
		ProxyNode child = root.child(2);
		child.setValue(43);
		child.makeNode();
		ProxyNode grandChild = child.child(2);
		grandChild.setValue(44);
		ProxyNode root2 = new ProxyNode(data);
		assertTrue(root2.isNode());
		assertEquals(42, root2.getValue());
		ProxyNode child2 = root2.child(2);
		assertTrue(child2.isNode());
		assertEquals(43, child2.getValue());
		ProxyNode grandChild2 = child2.child(2);
		assertTrue(grandChild2.isLeaf());
		assertEquals(44, grandChild2.getValue());

	}
}
