package sandbox.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadPoolExecutor;

public interface ParallelismLevel
{
	int getParallelism();

	static int get( ExecutorService executorService )
	{
		if( executorService instanceof ForkJoinPool )
			return ( ( ForkJoinPool ) executorService ).getParallelism();
		if( executorService instanceof ThreadPoolExecutor )
			return ( ( ThreadPoolExecutor ) executorService ).getPoolSize();
		if( executorService instanceof ParallelismLevel )
			return ( ( ParallelismLevel ) executorService ).getParallelism();
		return Runtime.getRuntime().availableProcessors();
	}
}
