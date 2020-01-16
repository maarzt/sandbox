package sandbox;

public interface BiMap<T> {

	int getIndex(T value);

	T getValue(int index);
}
