package sandbox.leaf;

import net.imglib2.Cursor;
import net.imglib2.Interval;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.NativeImg;
import net.imglib2.type.Type;

import java.util.Iterator;

public class FakeNativeImg<T extends Type<T>, A> implements NativeImg<T, A> {

	@Override
	public A update(Object updater) {
		return (A) updater;
	}

	@Override
	public void setLinkedType(T type) {

	}

	@Override
	public T createLinkedType() {
		return null;
	}

	@Override
	public ImgFactory< T > factory() {
		return null;
	}

	@Override
	public Img< T > copy() {
		return null;
	}

	@Override
	public Cursor< T > cursor() {
		return null;
	}

	@Override
	public Cursor< T > localizingCursor() {
		return null;
	}

	@Override
	public long min(int d) {
		return 0;
	}

	@Override
	public long max(int d) {
		return 0;
	}

	@Override
	public long size() {
		return 0;
	}

	@Override
	public T firstElement() {
		return null;
	}

	@Override
	public Object iterationOrder() {
		return null;
	}

	@Override
	public Iterator< T > iterator() {
		return null;
	}

	@Override
	public RandomAccess< T > randomAccess() {
		return null;
	}

	@Override
	public RandomAccess< T > randomAccess(Interval interval) {
		return null;
	}

	@Override
	public int numDimensions() {
		return 0;
	}
}
