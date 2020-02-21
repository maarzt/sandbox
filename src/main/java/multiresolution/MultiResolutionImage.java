package multiresolution;

import net.imagej.ImgPlus;

import java.util.List;

/**
 * Interface to represent an image pyramid.
 *
 * @param <T> Pixel type.
 */
public interface MultiResolutionImage<T> {

	List<ImgPlus<T> > resolutions();
}
