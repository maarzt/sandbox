package sandbox.mapping;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;

import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;

public class MappedUnaryOperator< T > implements IntUnaryOperator {

	private final ValueMapping< T > mapping;

	private final UnaryOperator< T > operator;

	private final TIntIntMap indexOperator = new TIntIntHashMap();

	public MappedUnaryOperator(UnaryOperator< T > operator,
			ValueMapping< T > mapping)
	{
		this.mapping = mapping;
		this.operator = operator;
	}

	@Override
	public int applyAsInt(int index) {
		int resultIndex = indexOperator.get(index);
		if( resultIndex != indexOperator.getNoEntryKey() )
			return resultIndex;
		synchronized ( indexOperator ) {
			resultIndex = indexOperator.get(index);
			if( resultIndex != indexOperator.getNoEntryKey() )
				return resultIndex;
			resultIndex = mapping.getIndex(operator.apply(mapping.getValue(index)));
			indexOperator.put(index, resultIndex);
			return resultIndex;
		}
	}
}
