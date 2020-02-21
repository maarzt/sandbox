package multiresolution;

import ij.ImagePlus;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imglib2.img.VirtualStackAdapter;
import net.imglib2.type.numeric.integer.UnsignedShortType;

import java.net.URL;

public class ExampleImage {

	public static MultiResolutionImage< UnsignedShortType > mitosis() {
		ImgPlus< UnsignedShortType > lowResolution = openImgPlus("/mitosis.tif");
		ImgPlus< UnsignedShortType > highResolution = openImgPlus("/mitosis-downscaled-2.tif");
		return new DefaultMultiResolutionImage<>(lowResolution, highResolution);
	}

	private static ImgPlus< UnsignedShortType > openImgPlus(String filename) {
		URL url = TwoScaleDemo.class.getResource(filename);
		ImagePlus image = new ImagePlus(url.getFile());
		return VirtualStackAdapter.wrapShort(image);
	}
}
