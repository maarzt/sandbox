package sandbox.lazy;

import net.imglib2.type.numeric.integer.IntType;
import sandbox.Cube;
import sandbox.IntervalMask;
import sandbox.Node;
import sandbox.OctTree;

public class LazyTree implements Node {

	private final IntType foreground;

	private final IntType background;

	private final Cube cube;

	private final IntervalMask mask;

	private final LazyTree child;

	public LazyTree(int depth, IntervalMask mask, IntType foreground, IntType background) {
		this(new Cube(depth), mask, foreground, background);
	}

	private LazyTree(Cube cube, IntervalMask mask, IntType foreground, IntType background) {
		this.mask = mask;
		this.cube = cube;
		this.foreground = foreground;
		this.background = background;
		this.child = cube.hasChildren() ? new LazyTree(cube.child(0), mask, foreground, background) : null;
	}

	public static OctTree< IntType > octTree(int depth, IntervalMask mask,
			IntType foreground, IntType background) {
		return new OctTree<>(depth, new LazyTree(depth, mask, foreground, background));
	}

	@Override
	public Object child(int i) {
		Cube childCube = cube.child(i);
		if(mask.contains(childCube))
			return foreground;
		if(!mask.intersects(childCube))
			return background;
		return child;
	}

	@Override
	public Node threadSafeCopy() {
		return new LazyTree(cube.threadSafeCopy(), mask, foreground, background);
	}

}
