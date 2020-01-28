package sandbox.combine;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class TinySet implements Set< Integer > {

	private final Integer[] data;

	private final int hashCode;

	private TinySet(Integer[] data) {
		this.data = data;
		this.hashCode = initHashCode(data);
	}

	public static TinySet emptySet() {
		return valueOf();
	}

	public static TinySet valueOf(Integer... values) {
		return new TinySet(values.clone());
	}

	public static TinySet union(TinySet a, TinySet b) {
		return new TinySet(union(a.data, b.data));
	}

	public static TinySet add(TinySet set, Integer value) {
		return new TinySet(add(set.data, value));
	}

	@Override
	public int size() {
		return data.length;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public boolean contains(Object o) {
		for (Integer value : data) if (value.equals(o)) return true;
		return false;
	}

	@Override
	public Iterator< Integer > iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray() {
		return data.clone();
	}

	@Override
	public < T > T[] toArray(T[] result) {
		System.arraycopy(data, 0, result, 0, data.length);
		return result;
	}

	@Override
	public boolean add(Integer integer) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection< ? > c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection< ? extends Integer > c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection< ? > c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection< ? > c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TinySet)) return false;
		TinySet other = (TinySet) obj;
		if (this.data.length != other.data.length) return false;
		for (int i = 0; i < data.length; i++)
			if (this.data[i] != other.data[i]) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	private static int initHashCode(Integer[] data) {
		int result = 898320837;
		for (Integer value : data) result = result * 324792373 + value;
		return result;
	}

	static Integer[] union(Integer[] a, Integer[] b) {
		Integer[] result = new Integer[a.length + b.length];
		int aIndex = 0;
		int bIndex = 0;
		int rIndex = 0;
		while (aIndex < a.length && bIndex < b.length) {
			Integer aValue = a[aIndex];
			Integer bValue = b[bIndex];
			Integer min = aValue < bValue ? aValue : bValue;
			result[rIndex++] = min;
			if (aValue.equals(min)) aIndex++;
			if (bValue.equals(min)) bIndex++;
		}
		while (aIndex < a.length) result[rIndex++] = a[aIndex++];
		while (bIndex < b.length) result[rIndex++] = b[bIndex++];
		return rIndex != result.length ? Arrays.copyOf(result, rIndex) : result;
	}

	static < T > Integer[] add(Integer[] array, int value) {
		Integer[] result = new Integer[array.length + 1];
		int i = 0;
		while (i < array.length && array[i] < value) {
			result[i] = array[i];
			i++;
		}
		if (i < array.length && array[i].equals(value)) return array;
		result[i] = value;
		while (i < array.length) {
			result[i + 1] = array[i];
			i++;
		}
		return result;
	}
}
