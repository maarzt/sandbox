package multiresolution;

import ij.ImagePlus;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imglib2.img.VirtualStackAdapter;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedShortType;

import java.net.URL;

public class TwoScaleDemo {

	public static void main(String... args) {
		ImgPlus< UnsignedShortType > lowResolution = openImgPlus("/mitosis.tif");
		ImgPlus< UnsignedShortType > highResolution = openImgPlus("/mitosis-downscaled-2.tif");
		MultiResolutionImage< UnsignedShortType > image = new DefaultMultiResolutionImage<>(lowResolution, highResolution);
		MultiResolutionImage< UnsignedShortType > frame = MultiResolutionViews
				.hyperSlice(image, Axes.TIME, 2);
		ImageJFunctions.show(frame.resolutions().get(0));
		ImageJFunctions.show(frame.resolutions().get(1));
	}

	private static ImgPlus< UnsignedShortType > openImgPlus(String filename) {
		URL url = TwoScaleDemo.class.getResource(filename);
		ImagePlus image = new ImagePlus(url.getFile());
		return VirtualStackAdapter.wrapShort(image);
	}
}
