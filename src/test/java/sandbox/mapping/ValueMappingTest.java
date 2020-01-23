package sandbox.mapping;

import net.imglib2.type.numeric.integer.IntType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ValueMappingTest {

	@Test
	public void testGetIndexSameValue() {
		final ValueMapping<IntType> mapping = new ValueMapping<>();
		final int i = mapping.getIndex(new IntType(42));
		final int j = mapping.getIndex(new IntType(42));
		assertEquals(i, j);
	}

	@Test
	public void testGetIndexDifferentValues() {
		final ValueMapping<String> mapping = new ValueMapping<>();
		final int i = mapping.getIndex("Hello");
		final int j = mapping.getIndex("World");
		assertNotEquals(i, j);
	}

	@Test
	public void testGetValue() {
		final ValueMapping<String> mapping = new ValueMapping<>();
		final int helloIndex = mapping.getIndex("Hello");
		final int worldIndex = mapping.getIndex("World");
		assertEquals("Hello", mapping.getValue(helloIndex));
		assertEquals("World", mapping.getValue(worldIndex));
	}
}
