package sandbox.mapping;

import gnu.trove.impl.Constants;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.util.ArrayList;
import java.util.List;

public class ValueMapping< T > {

	private static final int INT_NO_ENTRY_VALUE = -1;

	private final TObjectIntMap< T > valueToIndex =
			new TObjectIntHashMap<>(Constants.DEFAULT_CAPACITY,
					Constants.DEFAULT_LOAD_FACTOR, INT_NO_ENTRY_VALUE);

	private final List< T > values = new ArrayList<>();

	public int getIndex(T value) {
		final int index = valueToIndex.get(value);
		if (index != INT_NO_ENTRY_VALUE)
			return index;
		synchronized (values) {
			final int indexAfterSync = valueToIndex.get(value);
			if (indexAfterSync != INT_NO_ENTRY_VALUE)
				return indexAfterSync;
			final int newIndex = values.size();
			values.add(value);
			valueToIndex.put(value, newIndex);
			return newIndex;
		}
	}

	public T getValue(int index) {
		return values.get(index);
	}
}
