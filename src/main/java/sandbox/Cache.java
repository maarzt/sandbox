package sandbox;

import net.imglib2.util.Pair;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Cache< K, V, D > {

	private final ConcurrentMap< K, Entry< K, V, D > > cache =
			new ConcurrentHashMap<>();

	private final ReferenceQueue< V > queue = new ReferenceQueue<>();

	private final Function< K, Pair< V, D > > loader;

	private final BiConsumer< K, D > remover;

	public Cache(Function< K, Pair< V, D > > loader, BiConsumer< K, D > remover)
	{
		this.loader = loader;
		this.remover = remover;
	}

	private static class Entry< K, V, D > extends WeakReference< V > {

		private final K key;

		private final D data;

		private Entry(V referent, K key, D data, ReferenceQueue< V > queue) {
			super(referent, queue);
			this.key = key;
			this.data = data;
		}
	}

	public V get(K key) {
		clean();
		return cache.computeIfAbsent(key, this::createEntry).get();
	}

	private void clean() {
		while (true) {
			@SuppressWarnings("unchecked")
			Entry< K, V, D > entry = (Entry< K, V, D >) queue.poll();
			if (entry == null) return;
			remover.accept(entry.key, entry.data);
			cache.remove(entry.key);
		}
	}

	private Entry< K, V, D > createEntry(K key) {
		Pair< V, D > valueAndData = loader.apply(key);
		D data = valueAndData.getB();
		V value = valueAndData.getA();
		return new Entry<>(value, key, data, queue);
	}
}
