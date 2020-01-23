package sandbox.mapping;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MappedBinaryOperatorTest {

	@Test
	public void test() {
		ValueMapping< String > mapping = new ValueMapping<>();
		MappedBinaryOperator< String > operator =
				new MappedBinaryOperator<>((a, b) -> a + b, mapping);
		int a = mapping.getIndex("a");
		int b = mapping.getIndex("b");
		int result = operator.applyAsInt(a, b);
		assertEquals("ab", mapping.getValue(result));
	}
}
