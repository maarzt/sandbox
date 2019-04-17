package sandbox.executors;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * An ExecutorService that uses a ForkJoinPool to execute tasks,
 * and therefor supports nested calls.
 * <p>
 * If used inside a {@link ForkJoinPool} that pool is used,
 * in all other cases the common pool ({@link ForkJoinPool#commonPool()}) is used.
 * <p>
 * Shutdown and awaitTermination is not supported.
 */
public class ForkJoinExecutorService extends AbstractExecutorService implements ParallelismLevel
{
	@Override
	public void shutdown()
	{
	}

	@Override
	public List< Runnable > shutdownNow()
	{
		return Collections.emptyList();
	}

	@Override
	public boolean isShutdown()
	{
		return false;
	}

	@Override
	public boolean isTerminated()
	{
		return false;
	}

	@Override
	public boolean awaitTermination( long l, TimeUnit timeUnit ) throws InterruptedException
	{
		// NB: it's possible to implement this method. One might use a set of weak references to collect all tasks submitted.
		// And this method call ForkJoinTask.get( long, timeUnit), to get the timing correct.
		// But doing so introduces reduced performance, as the set of tasks needs to be managed.
		// It's simpler to not use await termination at all.
		// Alternative is to collect the futures and call get on them.
		throw new UnsupportedOperationException("ForkJoinExecutorService, awaitTermination is not implemented.");
	}

	@Override
	public < T > List< Future< T > > invokeAll( Collection< ? extends Callable< T > > collection ) throws InterruptedException
	{
		List< ForkJoinTask< T > > futures = collection.stream().map( ForkJoinTask::adapt ).collect( Collectors.toList());
		ForkJoinTask.invokeAll( futures );
		return Collections.unmodifiableList( futures );
	}

	@Override
	public < T > List< Future< T > > invokeAll( Collection< ? extends Callable< T > > collection, long l, TimeUnit timeUnit ) throws InterruptedException
	{
		// TODO fix timing;
		return invokeAll( collection );
	}

	@Override
	public Future< ? > submit( Runnable runnable )
	{
		return ForkJoinTask.adapt( runnable ).fork();
	}

	@Override
	public < T > Future< T > submit( Runnable runnable, T t )
	{
		return ForkJoinTask.adapt( runnable, t ).fork();
	}

	@Override
	public < T > Future< T > submit( Callable< T > callable )
	{
		return ForkJoinTask.adapt( callable ).fork();
	}

	@Override
	public void execute( Runnable runnable )
	{
		ForkJoinTask.adapt( runnable ).fork();
	}

	@Override
	public int getParallelism()
	{
		return ForkJoinTask.getPool().getParallelism();
	}
}
