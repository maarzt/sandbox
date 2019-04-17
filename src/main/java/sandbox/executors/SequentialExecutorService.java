package sandbox.executors;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * A {@link ExecutorService) that is single-threaded, it never uses threads.
 */
public class SequentialExecutorService extends AbstractExecutorService implements ParallelismLevel
{
	private boolean isShutdown = false;

	@Override
	public void shutdown()
	{
		isShutdown = true;
	}

	@Override
	public List< Runnable > shutdownNow()
	{
		shutdown();
		return Collections.emptyList();
	}

	@Override
	public boolean isShutdown()
	{
		return isShutdown;
	}

	@Override
	public boolean isTerminated()
	{
		return isShutdown;
	}

	@Override
	public boolean awaitTermination( long l, TimeUnit timeUnit ) throws InterruptedException
	{
		return true;
	}

	@Override
	public void execute( Runnable runnable )
	{
		runnable.run();
	}

	@Override
	public int getParallelism()
	{
		return 1;
	}
}
