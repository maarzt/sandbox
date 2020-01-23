package sandbox.mapping;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
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

	@Test
	public void testIfCachingWorks() {
		AtomicInteger counter = new AtomicInteger();
		UnaryOperator<String> operator = s -> {
			counter.incrementAndGet();
			return s + "!";
		};
		ValueMapping<String> mapping = new ValueMapping<>();
		IntUnaryOperator mappedOperator = new MappedUnaryOperator<>(operator, mapping);
		mappedOperator.applyAsInt(mapping.getIndex("Hello"));
		mappedOperator.applyAsInt(mapping.getIndex("Hello"));
		assertEquals(1, counter.get());
	}
}
