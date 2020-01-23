package sandbox.mapping;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.TLongIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TLongIntHashMap;

import java.util.function.BinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

public class MappedBinaryOperator< T > implements IntBinaryOperator {

	private final ValueMapping< T > mapping;

	private final BinaryOperator< T > operator;

	private final TLongIntMap indexOperator = new TLongIntHashMap();

	public MappedBinaryOperator(BinaryOperator< T > operator,
			ValueMapping< T > mapping)
	{
		this.mapping = mapping;
		this.operator = operator;
	}

	@Override
	public int applyAsInt(int a, int b) {
		long index = (long) a << 32L | b;
		int resultIndex = indexOperator.get(index);
		if( resultIndex != indexOperator.getNoEntryKey() )
			return resultIndex;
		synchronized ( indexOperator ) {
			resultIndex = indexOperator.get(index);
			if( resultIndex != indexOperator.getNoEntryKey() )
				return resultIndex;
			resultIndex = mapping.getIndex(operator.apply(mapping.getValue(a), mapping.getValue(b)));
			indexOperator.put(index, resultIndex);
			return resultIndex;
		}
	}
}
