package sandbox.lazy;

import org.junit.Test;
import sandbox.SimpleNode;

import java.util.function.BiFunction;

import static org.junit.Assert.assertEquals;

public class LazyTreeTest {

	@Test
	public void test() {
		Object a = new SimpleNode(true, false, false, false, false, false, false, false);
		Object b = new SimpleNode(false, false, true, false, false, false, false, false);
		Object e = new SimpleNode(true, false, true, false, false, false, false, false);
		BiFunction<Boolean, Boolean, Boolean> operation = (x, y) -> x || y;
		Object c = LazyMerge.merge(a, b, operation);
		assertEquals(e, c);
	}
}
