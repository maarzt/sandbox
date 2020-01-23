package sandbox.mapping;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class MappedParameterizedUnaryOperator< T, P > {

	private final BiFunction< T, P, T > operation;

	private final ValueMapping< T > mapping;

	private final Map< P, MappedUnaryOperator< T > > indexOperators =
			new ConcurrentHashMap<>();

	public MappedParameterizedUnaryOperator(BiFunction< T, P, T > operation,
			ValueMapping< T > mapping)
	{
		this.operation = operation;
		this.mapping = mapping;
	}

	public int applyAsInt(int index, P parameter) {
		MappedUnaryOperator< T > indexOperator = indexOperators
				.computeIfAbsent(parameter, this::createUnaryOperator);
		return indexOperator.applyAsInt(index);
	}

	private MappedUnaryOperator< T > createUnaryOperator(P parameter) {
		return new MappedUnaryOperator<>(v -> operation.apply(v, parameter),
				mapping);
	}
}
