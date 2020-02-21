package multiresolution;

import net.imagej.ImgPlus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DefaultMultiResolutionImage<T> implements MultiResolutionImage<T> {

	private final List<ImgPlus<T>> resolutions;

	public DefaultMultiResolutionImage(ImgPlus<T>... resolution) {
		this.resolutions = Collections.unmodifiableList(Arrays.asList(resolution));
	}

	public DefaultMultiResolutionImage(List<ImgPlus<T>> resolutions) {
		this.resolutions = Collections.unmodifiableList(new ArrayList<>(resolutions));
	}

	public List<ImgPlus<T>> resolutions() {
		return resolutions;
	}
}
