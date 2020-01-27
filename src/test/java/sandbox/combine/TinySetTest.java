package sandbox.combine;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class TinySetTest {

	@Test
	public void testUnionOfArrays() {
		Integer[] result = TinySet.union(array(2, 6, 9), array(3, 6, 8));
		assertArrayEquals(new Integer[] { 2, 3, 6, 8, 9 }, result);
	}

	@Test
	public void testUnionOfArrays2() {
		Integer[] result = TinySet.union(array(), array(3, 6, 8));
		assertArrayEquals(array(3, 6, 8), result);
	}

	@Test
	public void testUnionOfArrays3() {
		Integer[] result = TinySet.union(array(3, 6, 8), array());
		assertArrayEquals(array(3, 6, 8), result);
	}

	@Test
	public void testAdd() {
		Integer[] result = TinySet.add(array(1, 3, 7), 2);
		assertArrayEquals(array(1, 2, 3, 7), result);
	}

	@Test
	public void testAdd2() {
		Integer[] result = TinySet.add(array(1, 3, 7), 3);
		assertArrayEquals(array(1, 3, 7), result);
	}

	private static < T > T[] array(T... values) {
		return values;
	}

	@Test
	public void testEquals() {
		assertEquals(TinySet.valueOf(1, 2, 3), TinySet.valueOf(1,2,3));
		assertNotEquals(TinySet.valueOf(1, 3, 4), TinySet.valueOf(1,2,3));
		assertNotEquals(TinySet.valueOf(1, 3), TinySet.valueOf(1,2,3));
	}

	@Test
	public void testHashCode() {
		assertEquals(TinySet.valueOf(1, 2, 3).hashCode(), TinySet.valueOf(1,2,3).hashCode());
		assertNotEquals(TinySet.valueOf(1, 2).hashCode(), TinySet.valueOf(1,2,3).hashCode());
	}
}
