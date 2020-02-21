package multiresolution;

import net.imagej.ImgPlus;

import java.util.List;

public interface MultiResolutionImage<T> {

	List<ImgPlus<T> > resolutions();
}
