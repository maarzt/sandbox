package sandbox.mapping;

import org.junit.Test;

import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;

import static org.junit.Assert.assertEquals;

public class MappedUnaryOperatorTest {

	@Test
	public void test() {
		UnaryOperator<String> operator = s -> s + "!";
		ValueMapping<String> mapping = new ValueMapping<>();
		int index = mapping.getIndex("Hello");
		IntUnaryOperator mappedOperator = new MappedUnaryOperator<>(operator, mapping);
		int resultIndex = mappedOperator.applyAsInt(index);
		String result = mapping.getValue(resultIndex);
		assertEquals(operator.apply("Hello"), result);
	}
}
