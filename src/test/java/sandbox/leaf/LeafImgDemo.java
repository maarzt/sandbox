package sandbox.leaf;

import bdv.util.BdvFunctions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealPoint;
import net.imglib2.img.Img;
import net.imglib2.test.ImgLib2Assert;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.IntType;
import sandbox.OctTree;
import sandbox.OctTrees;
import sandbox.Sphere;

import java.util.Random;

public class LeafImgDemo {

	private static final Sphere sphere =
			new Sphere(new RealPoint(900, 400, 2000), 1000);

	public static void main(String... args) {
		Img< IntType > image = LeafImgs.create(new IntType(), 4, 8, sphere);
		BdvFunctions.show(image, "sphere").setDisplayRange(0, 1);
	}

}
