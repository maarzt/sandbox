package sandbox.mapping;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

import static org.junit.Assert.assertEquals;

public class MappedParameterizedUnaryOperatorTest {

	@Test
	public void test() {
		BiFunction<Set<String>, String, Set<String>> add = (set, value) -> {
			HashSet< String > strings = new HashSet<>(set);
			strings.add(value);
			return strings;
		};
		ValueMapping<Set<String>> mapping = new ValueMapping<>();
		MappedParameterizedUnaryOperator<Set<String>, String> mappedAdd = new MappedParameterizedUnaryOperator<>(add, mapping);
		int inputIndex = mapping.getIndex(Collections.singleton("Hello"));
		int outputIndex = mappedAdd.applyAsInt(inputIndex, "World");
		assertEquals(new HashSet<>(Arrays.asList("Hello", "World")), mapping.getValue(outputIndex));
	}
}
