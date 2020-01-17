package sandbox.lazy;

import org.junit.Test;
import sandbox.SimpleNode;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class LazyMergeManyTest {

	@Test
	public void test() {
		Object a = new SimpleNode(true, false, false, false, false, false, false, false);
		Object b = new SimpleNode(false, false, true, false, false, false, false, false);
		Object result = LazyMergeMany.mergeTrees(false, (x, y) -> x || y, Arrays.asList(a, b));
		Object expected = new SimpleNode(true, false, true, false, false, false, false, false);
		assertEquals(expected, result);
	}
}
