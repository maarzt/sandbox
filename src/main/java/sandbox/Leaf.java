package sandbox;

public class Leaf<T> implements Node< T > {

	private final T value;

	public Leaf(T value) {
		this.value = value;
	}

	public T value() {
		return value;
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
