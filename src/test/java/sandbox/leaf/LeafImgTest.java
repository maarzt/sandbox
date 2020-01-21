package sandbox.leaf;

import net.imglib2.FinalInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.IntAccess;
import net.imglib2.img.basictypeaccess.array.IntArray;
import net.imglib2.test.ImgLib2Assert;
import net.imglib2.type.numeric.integer.IntType;
import org.junit.Test;
import sandbox.OctTree;

public class LeafImgTest {

	@Test
	public void testSize() {
		Img<IntType> img = new LeafImg(new IntType(), 3, null);
		ImgLib2Assert.assertIntervalEquals(new FinalInterval(32, 32, 32), img);
	}

	@Test
	public void testData() {
		OctTree< IntAccess > tree = new OctTree<>(0, new IntArray(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 }));
		Img<IntType> img = new LeafImg(new IntType(), 1, tree);
		ImgLib2Assert.assertImageEquals(ArrayImgs.ints(new int[]{1,2,3,4,5,6,7,8}, 2, 2, 2), img);
	}
}
