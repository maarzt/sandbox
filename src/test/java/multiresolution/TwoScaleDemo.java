package multiresolution;

import net.imagej.axis.Axes;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedShortType;

public class TwoScaleDemo {

	public static void main(String... args) {
		MultiResolutionImage< UnsignedShortType > image = ExampleImage.mitosis();
		MultiResolutionImage< UnsignedShortType > frame = MultiResolutionViews.hyperSlice(image, Axes.TIME, 2);
		ImageJFunctions.show(frame.resolutions().get(0));
		ImageJFunctions.show(frame.resolutions().get(1));
	}

}
