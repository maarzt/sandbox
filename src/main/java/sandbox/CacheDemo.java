package sandbox;

import net.imglib2.img.basictypeaccess.array.IntArray;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;

import java.lang.ref.WeakReference;

public class CacheDemo {

	private static final int BLOCK_SIZE =
			(int) (Runtime.getRuntime().maxMemory() / Integer.BYTES / 10);

	public static void main(String... args) {
		System.out.println(System.getProperty("java.version"));
		tweakGarbageCollection();
		Cache<Long, IntArray, int[]> cache = new Cache<>(CacheDemo::load, CacheDemo::remove);
		for (long i = 0; i < 100; i++) {
			cache.get(i).setValue(0, (int) i);
		}
	}

	private static void tweakGarbageCollection() {
		// This trick makes the demo work even for huge data blocks
		WeakReference< int[] > reference = new WeakReference<>(new int[BLOCK_SIZE]);
	}

	private static Pair<IntArray, int[]> load(Long key) {
		System.out.println("Create: " + key);
		int[] data = new int[BLOCK_SIZE];
		IntArray value = new IntArray(data);
		return new ValuePair<>(value, data);
	}

	private static void remove(Long key, int[] data) {
		System.out.println("Remove, key: " + key + " data: " + arrayToString(data));
	}

	private static String arrayToString(int[] data) {
		StringBuilder result = new StringBuilder("[");
		for (int i = 0; i < 3; i++)
			result.append(data[i]).append(", ");
		return result.append("... ]").toString();
	}

}
