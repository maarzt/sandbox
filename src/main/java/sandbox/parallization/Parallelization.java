package sandbox.parallization;

import sandbox.executors.ForkJoinExecutorService;
import sandbox.executors.ParallelismLevel;
import sandbox.executors.SequentialExecutorService;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.function.Supplier;

/**
 * A new way to configure an algorithm for parallelization.
 * <p>
 * The algorithm just needs to call {@link Parallelization#getExecutorService()}
 * to get the {@link ExecutorService} it's supposed to use.
 * And it can use {@link Parallelization#getParallelism()} to get the number
 * of threads available.
 * <p>
 * The algorithm can now be executed singleThreaded, multiThreaded or by
 * using a predefined {@link ExecutorService} or {@link ForkJoinPool}:
 * <pre>
 *     {@code
 *     // Single Threaded call
 *     Parallelization.singleThreaded( () -> myAlgorithm( myInput ) );
 *
 *     // Multi Threaded call
 *     Parallelization.multiThreaded( () -> myAlgorithm( myOutput ) );
 *
 *     // ExecutorService
 *     Parallelization.useExecutorService( executorService, () -> myAlgorithm( myOutput ) );
 *     }
 * </pre>
 */
public final class Parallelization
{
	private Parallelization()
	{
		// prevent from instantiation
	}

	private static final ExecutorService SEQUENTIAL = new SequentialExecutorService();

	private static final ExecutorService FORK_JOIN = new ForkJoinExecutorService();

	private static final ThreadLocal< ExecutorService > executor = ThreadLocal.withInitial( () -> {
		if ( ForkJoinTask.inForkJoinPool() )
			return FORK_JOIN;
		else
			return SEQUENTIAL;
	} );

	public static ExecutorService getExecutorService()
	{
		return executor.get();
	}

	public static int getParallelism()
	{
		return ParallelismLevel.get( getExecutorService() );
	}

	public static void singleThreaded( Runnable task )
	{
		singleThreaded( wrapRunnable( task ) );
	}

	public static < T > T singleThreaded( Callable< T > task )
	{
		return useExecutor( SEQUENTIAL, task );
	}

	public static void multiThreaded( Runnable runnable )
	{
		multiThreaded( wrapRunnable( runnable ) );
	}

	public static < T > T multiThreaded( Callable< T > task )
	{
		return useExecutor( FORK_JOIN, task );
	}

	public static void useExecutor( ExecutorService executorService, Runnable runnable )
	{
		useExecutor( executorService, wrapRunnable( runnable ) );
	}

	private static Callable< ? > wrapRunnable( Runnable runnable )
	{
		return () -> {
			runnable.run();
			return null;
		};
	}

	public static < T > T useExecutor( ExecutorService executorService, Callable< T > task )
	{
		ExecutorService old = executor.get();
		executor.set( executorService );
		try
		{
			return task.call();
		}
		catch ( Exception e )
		{
			if( e instanceof RuntimeException )
				throw (RuntimeException) e;
			throw new RuntimeException( e );
		}
		finally
		{
			executor.set( old );
		}
	}
}
