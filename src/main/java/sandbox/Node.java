package sandbox;

public interface Node {

	Object child(int i);

	default Node threadSafeCopy() {
		return this;
	}
}
