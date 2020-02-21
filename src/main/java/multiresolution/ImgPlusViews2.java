package multiresolution;

import net.imagej.ImgPlus;
import net.imagej.axis.AxisType;
import net.imglib2.img.display.imagej.ImgPlusViews;

public class ImgPlusViews2 {

	public static <T> ImgPlus<T> hyperSlice(ImgPlus<T> image, AxisType axis, long position) {
		int d = image.dimensionIndex(axis);
		if(d == -1)
			return image;
		return ImgPlusViews.hyperSlice((ImgPlus) image, d, position);
	}
}
