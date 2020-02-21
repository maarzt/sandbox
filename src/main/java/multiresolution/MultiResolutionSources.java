package multiresolution;

import bdv.viewer.Source;

import java.util.AbstractList;

public class MultiResolutionSources<T> extends AbstractList<Source<T>>{

	private final MultiResolution<T> multiResolution;

	public MultiResolutionSources(MultiResolution<T> multiResolution) {
		this.multiResolution = multiResolution;
	}

	@Override
	public Source<T> get(int index) {
		return null;
	}

	@Override
	public int size() {
		return 0;
	}
}
