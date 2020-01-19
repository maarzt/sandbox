package sandbox.lazy;

import net.imglib2.RealPoint;
import net.imglib2.type.numeric.integer.IntType;
import org.junit.Test;
import sandbox.IntervalMask;
import sandbox.SimpleNode;
import sandbox.Sphere;

import static org.junit.Assert.assertEquals;

public class LazyTreeTest {

	private final IntType ZERO = new IntType(0);

	private final IntType ONE = new IntType(1);

	@Test
	public void test() {
		IntervalMask sphere = new Sphere(new RealPoint(0, 0, 0), 0.5);
		Object tree = new LazyTree(1, sphere, ONE, ZERO);
		Object expected = new SimpleNode(ONE, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO);
		assertEquals(expected, tree);
	}
}
