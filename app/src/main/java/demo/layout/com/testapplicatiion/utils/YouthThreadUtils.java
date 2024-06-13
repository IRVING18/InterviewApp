package demo.layout.com.testapplicatiion.utils;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import androidx.annotation.CallSuper;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2018/05/08
 *     desc  : utils about thread
 * </pre>
 */
public final class YouthThreadUtils {

    private static final String TAG = "ThreadUtils";

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    private static final Map<Integer, Map<Integer, ExecutorService>> TYPE_PRIORITY_POOLS = new HashMap<>();

    private static final Map<Task<?>, ExecutorService> TASK_POOL_MAP = new ConcurrentHashMap<>();

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final Timer TIMER = new Timer();

    private static final byte TYPE_SINGLE = -1;
    private static final byte TYPE_CACHED = -2;
    private static final byte TYPE_IO = -4;
    private static final byte TYPE_CPU = -8;

    private static Executor sDeliver;

    /**
     * Return whether the thread is the main thread.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static Handler getMainHandler() {
        return HANDLER;
    }

    public static void runOnUiThread(final Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            HANDLER.post(runnable);
        }
    }

    public static void runOnUiThreadDelayed(final Runnable runnable, long delayMillis) {
        HANDLER.postDelayed(runnable, delayMillis);
    }

    /**
     * Return a thread pool that reuses a fixed number of threads
     * operating off a shared unbounded queue, using the provided
     * ThreadFactory to create new threads when needed.
     *
     * @param size The size of thread in the pool.
     * @return a fixed thread pool
     */
    public static ExecutorService getFixedPool(@IntRange(from = 1) final int size) {
        return getPoolByTypeAndPriority(size);
    }

    /**
     * Return a thread pool that reuses a fixed number of threads
     * operating off a shared unbounded queue, using the provided
     * ThreadFactory to create new threads when needed.
     *
     * @param size The size of thread in the pool.
     * @param priority The priority of thread in the poll.
     * @return a fixed thread pool
     */
    public static ExecutorService getFixedPool(@IntRange(from = 1) final int size,
        @IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(size, priority);
    }

    /**
     * Return a thread pool that uses a single worker thread operating
     * off an unbounded queue, and uses the provided ThreadFactory to
     * create a new thread when needed.
     *
     * @return a single thread pool
     */
    public static ExecutorService getSinglePool() {
        return getPoolByTypeAndPriority(TYPE_SINGLE);
    }

    /**
     * Return a thread pool that uses a single worker thread operating
     * off an unbounded queue, and uses the provided ThreadFactory to
     * create a new thread when needed.
     *
     * @param priority The priority of thread in the poll.
     * @return a single thread pool
     */
    public static ExecutorService getSinglePool(@IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(TYPE_SINGLE, priority);
    }

    /**
     * Return a thread pool that creates new threads as needed, but
     * will reuse previously constructed threads when they are
     * available.
     *
     * @return a cached thread pool
     */
    public static ExecutorService getCachedPool() {
        return getPoolByTypeAndPriority(TYPE_CACHED);
    }

    /**
     * Return a thread pool that creates new threads as needed, but
     * will reuse previously constructed threads when they are
     * available.
     *
     * @param priority The priority of thread in the poll.
     * @return a cached thread pool
     */
    public static ExecutorService getCachedPool(@IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(TYPE_CACHED, priority);
    }

    /**
     * Return a thread pool that creates (2 * CPU_COUNT + 1) threads
     * operating off a queue which size is 128.
     *
     * @return a IO thread pool
     */
    public static ExecutorService getIoPool() {
        return getPoolByTypeAndPriority(TYPE_IO);
    }

    /**
     * Return a thread pool that creates (2 * CPU_COUNT + 1) threads
     * operating off a queue which size is 128.
     *
     * @param priority The priority of thread in the poll.
     * @return a IO thread pool
     */
    public static ExecutorService getIoPool(@IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(TYPE_IO, priority);
    }

    /**
     * Return a thread pool that creates (CPU_COUNT + 1) threads
     * operating off a queue which size is 128 and the maximum
     * number of threads equals (2 * CPU_COUNT + 1).
     *
     * @return a cpu thread pool for
     */
    public static ExecutorService getCpuPool() {
        return getPoolByTypeAndPriority(TYPE_CPU);
    }

    /**
     * Return a thread pool that creates (CPU_COUNT + 1) threads
     * operating off a queue which size is 128 and the maximum
     * number of threads equals (2 * CPU_COUNT + 1).
     *
     * @param priority The priority of thread in the poll.
     * @return a cpu thread pool for
     */
    public static ExecutorService getCpuPool(@IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(TYPE_CPU, priority);
    }

    /**
     * Executes the given task in a fixed thread pool.
     *
     * @param size The size of thread in the fixed thread pool.
     * @param task The task to execute.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByFixed(@IntRange(from = 1) final int size, final Task<T> task) {
        execute(getPoolByTypeAndPriority(size), task);
    }

    /**
     * Executes the given task in a fixed thread pool.
     *
     * @param size The size of thread in the fixed thread pool.
     * @param task The task to execute.
     * @param priority The priority of thread in the poll.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByFixed(@IntRange(from = 1) final int size,
        final Task<T> task,
        @IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(size, priority), task);
    }

    /**
     * Executes the given task in a fixed thread pool after the given delay.
     *
     * @param size The size of thread in the fixed thread pool.
     * @param task The task to execute.
     * @param delay The time from now to delay execution.
     * @param unit The time unit of the delay parameter.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByFixedWithDelay(@IntRange(from = 1) final int size,
        final Task<T> task,
        final long delay,
        final TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(size), task, delay, unit);
    }

    /**
     * Executes the given task in a fixed thread pool after the given delay.
     *
     * @param size The size of thread in the fixed thread pool.
     * @param task The task to execute.
     * @param delay The time from now to delay execution.
     * @param unit The time unit of the delay parameter.
     * @param priority The priority of thread in the poll.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByFixedWithDelay(@IntRange(from = 1) final int size,
        final Task<T> task,
        final long delay,
        final TimeUnit unit,
        @IntRange(from = 1, to = 10) final int priority) {
        executeWithDelay(getPoolByTypeAndPriority(size, priority), task, delay, unit);
    }

    /**
     * Executes the given task in a fixed thread pool at fix rate.
     *
     * @param size The size of thread in the fixed thread pool.
     * @param task The task to execute.
     * @param period The period between successive executions.
     * @param unit The time unit of the period parameter.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByFixedAtFixRate(@IntRange(from = 1) final int size,
        final Task<T> task,
        final long period,
        final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(size), task, 0, period, unit);
    }

    /**
     * Executes the given task in a fixed thread pool at fix rate.
     *
     * @param size The size of thread in the fixed thread pool.
     * @param task The task to execute.
     * @param period The period between successive executions.
     * @param unit The time unit of the period parameter.
     * @param priority The priority of thread in the poll.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByFixedAtFixRate(@IntRange(from = 1) final int size,
        final Task<T> task,
        final long period,
        final TimeUnit unit,
        @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(size, priority), task, 0, period, unit);
    }

    /**
     * Executes the given task in a fixed thread pool at fix rate.
     *
     * @param size The size of thread in the fixed thread pool.
     * @param task The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period The period between successive executions.
     * @param unit The time unit of the initialDelay and period parameters.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByFixedAtFixRate(@IntRange(from = 1) final int size,
        final Task<T> task,
        long initialDelay,
        final long period,
        final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(size), task, initialDelay, period, unit);
    }

    /**
     * Executes the given task in a fixed thread pool at fix rate.
     *
     * @param size The size of thread in the fixed thread pool.
     * @param task The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period The period between successive executions.
     * @param unit The time unit of the initialDelay and period parameters.
     * @param priority The priority of thread in the poll.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByFixedAtFixRate(@IntRange(from = 1) final int size,
        final Task<T> task,
        long initialDelay,
        final long period,
        final TimeUnit unit,
        @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(size, priority), task, initialDelay, period, unit);
    }

    /**
     * Executes the given task in a single thread pool.
     *
     * @param task The task to execute.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeBySingle(final Task<T> task) {
        execute(getPoolByTypeAndPriority(TYPE_SINGLE), task);
    }

    /**
     * Executes the given task in a single thread pool.
     *
     * @param task The task to execute.
     * @param priority The priority of thread in the poll.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeBySingle(final Task<T> task,
        @IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(TYPE_SINGLE, priority), task);
    }

    /**
     * Executes the given task in a single thread pool after the given delay.
     *
     * @param task The task to execute.
     * @param delay The time from now to delay execution.
     * @param unit The time unit of the delay parameter.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeBySingleWithDelay(final Task<T> task,
        final long delay,
        final TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_SINGLE), task, delay, unit);
    }

    /**
     * Executes the given task in a single thread pool after the given delay.
     *
     * @param task The task to execute.
     * @param delay The time from now to delay execution.
     * @param unit The time unit of the delay parameter.
     * @param priority The priority of thread in the poll.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeBySingleWithDelay(final Task<T> task,
        final long delay,
        final TimeUnit unit,
        @IntRange(from = 1, to = 10) final int priority) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_SINGLE, priority), task, delay, unit);
    }

    /**
     * Executes the given task in a single thread pool at fix rate.
     *
     * @param task The task to execute.
     * @param period The period between successive executions.
     * @param unit The time unit of the period parameter.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeBySingleAtFixRate(final Task<T> task,
        final long period,
        final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_SINGLE), task, 0, period, unit);
    }

    /**
     * Executes the given task in a single thread pool at fix rate.
     *
     * @param task The task to execute.
     * @param period The period between successive executions.
     * @param unit The time unit of the period parameter.
     * @param priority The priority of thread in the poll.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeBySingleAtFixRate(final Task<T> task,
        final long period,
        final TimeUnit unit,
        @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_SINGLE, priority), task, 0, period, unit);
    }

    /**
     * Executes the given task in a single thread pool at fix rate.
     *
     * @param task The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period The period between successive executions.
     * @param unit The time unit of the initialDelay and period parameters.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeBySingleAtFixRate(final Task<T> task,
        long initialDelay,
        final long period,
        final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_SINGLE), task, initialDelay, period, unit);
    }

    /**
     * Executes the given task in a single thread pool at fix rate.
     *
     * @param task The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period The period between successive executions.
     * @param unit The time unit of the initialDelay and period parameters.
     * @param priority The priority of thread in the poll.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeBySingleAtFixRate(final Task<T> task,
        long initialDelay,
        final long period,
        final TimeUnit unit,
        @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(
            getPoolByTypeAndPriority(TYPE_SINGLE, priority), task, initialDelay, period, unit
        );
    }

    /**
     * Executes the given task in a cached thread pool.
     *
     * @param task The task to execute.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByCached(final Task<T> task) {
        execute(getPoolByTypeAndPriority(TYPE_CACHED), task);
    }

    /**
     * Executes the given task in a cached thread pool.
     *
     * @param task The task to execute.
     * @param priority The priority of thread in the poll.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByCached(final Task<T> task,
        @IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(TYPE_CACHED, priority), task);
    }

    /**
     * Executes the given task in a cached thread pool after the given delay.
     *
     * @param task The task to execute.
     * @param delay The time from now to delay execution.
     * @param unit The time unit of the delay parameter.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByCachedWithDelay(final Task<T> task,
        final long delay,
        final TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_CACHED), task, delay, unit);
    }

    /**
     * Executes the given task in a cached thread pool after the given delay.
     *
     * @param task The task to execute.
     * @param delay The time from now to delay execution.
     * @param unit The time unit of the delay parameter.
     * @param priority The priority of thread in the poll.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByCachedWithDelay(final Task<T> task,
        final long delay,
        final TimeUnit unit,
        @IntRange(from = 1, to = 10) final int priority) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_CACHED, priority), task, delay, unit);
    }

    /**
     * Executes the given task in a cached thread pool at fix rate.
     *
     * @param task The task to execute.
     * @param period The period between successive executions.
     * @param unit The time unit of the period parameter.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByCachedAtFixRate(final Task<T> task,
        final long period,
        final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CACHED), task, 0, period, unit);
    }

    /**
     * Executes the given task in a cached thread pool at fix rate.
     *
     * @param task The task to execute.
     * @param period The period between successive executions.
     * @param unit The time unit of the period parameter.
     * @param priority The priority of thread in the poll.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByCachedAtFixRate(final Task<T> task,
        final long period,
        final TimeUnit unit,
        @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CACHED, priority), task, 0, period, unit);
    }

    /**
     * Executes the given task in a cached thread pool at fix rate.
     *
     * @param task The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period The period between successive executions.
     * @param unit The time unit of the initialDelay and period parameters.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByCachedAtFixRate(final Task<T> task,
        long initialDelay,
        final long period,
        final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CACHED), task, initialDelay, period, unit);
    }

    /**
     * Executes the given task in a cached thread pool at fix rate.
     *
     * @param task The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period The period between successive executions.
     * @param unit The time unit of the initialDelay and period parameters.
     * @param priority The priority of thread in the poll.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByCachedAtFixRate(final Task<T> task,
        long initialDelay,
        final long period,
        final TimeUnit unit,
        @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(
            getPoolByTypeAndPriority(TYPE_CACHED, priority), task, initialDelay, period, unit
        );
    }

    /**
     * Executes the given task in an IO thread pool.
     *
     * @param task The task to execute.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByIo(final Task<T> task) {
        execute(getPoolByTypeAndPriority(TYPE_IO), task);
    }

    /**
     * Executes the given task in an IO thread pool.
     *
     * @param task The task to execute.
     * @param priority The priority of thread in the poll.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByIo(final Task<T> task,
        @IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(TYPE_IO, priority), task);
    }

    /**
     * Executes the given task in an IO thread pool after the given delay.
     *
     * @param task The task to execute.
     * @param delay The time from now to delay execution.
     * @param unit The time unit of the delay parameter.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByIoWithDelay(final Task<T> task,
        final long delay,
        final TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_IO), task, delay, unit);
    }

    /**
     * Executes the given task in an IO thread pool after the given delay.
     *
     * @param task The task to execute.
     * @param delay The time from now to delay execution.
     * @param unit The time unit of the delay parameter.
     * @param priority The priority of thread in the poll.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByIoWithDelay(final Task<T> task,
        final long delay,
        final TimeUnit unit,
        @IntRange(from = 1, to = 10) final int priority) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_IO, priority), task, delay, unit);
    }

    /**
     * Executes the given task in an IO thread pool at fix rate.
     *
     * @param task The task to execute.
     * @param period The period between successive executions.
     * @param unit The time unit of the period parameter.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByIoAtFixRate(final Task<T> task,
        final long period,
        final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_IO), task, 0, period, unit);
    }

    /**
     * Executes the given task in an IO thread pool at fix rate.
     *
     * @param task The task to execute.
     * @param period The period between successive executions.
     * @param unit The time unit of the period parameter.
     * @param priority The priority of thread in the poll.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByIoAtFixRate(final Task<T> task,
        final long period,
        final TimeUnit unit,
        @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_IO, priority), task, 0, period, unit);
    }

    /**
     * Executes the given task in an IO thread pool at fix rate.
     *
     * @param task The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period The period between successive executions.
     * @param unit The time unit of the initialDelay and period parameters.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByIoAtFixRate(final Task<T> task,
        long initialDelay,
        final long period,
        final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_IO), task, initialDelay, period, unit);
    }

    /**
     * Executes the given task in an IO thread pool at fix rate.
     *
     * @param task The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period The period between successive executions.
     * @param unit The time unit of the initialDelay and period parameters.
     * @param priority The priority of thread in the poll.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByIoAtFixRate(final Task<T> task,
        long initialDelay,
        final long period,
        final TimeUnit unit,
        @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(
            getPoolByTypeAndPriority(TYPE_IO, priority), task, initialDelay, period, unit
        );
    }

    /**
     * Executes the given task in a cpu thread pool.
     *
     * @param task The task to execute.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByCpu(final Task<T> task) {
        execute(getPoolByTypeAndPriority(TYPE_CPU), task);
    }

    /**
     * Executes the given task in a cpu thread pool.
     *
     * @param task The task to execute.
     * @param priority The priority of thread in the poll.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByCpu(final Task<T> task,
        @IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(TYPE_CPU, priority), task);
    }

    /**
     * Executes the given task in a cpu thread pool after the given delay.
     *
     * @param task The task to execute.
     * @param delay The time from now to delay execution.
     * @param unit The time unit of the delay parameter.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByCpuWithDelay(final Task<T> task,
        final long delay,
        final TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_CPU), task, delay, unit);
    }

    /**
     * Executes the given task in a cpu thread pool after the given delay.
     *
     * @param task The task to execute.
     * @param delay The time from now to delay execution.
     * @param unit The time unit of the delay parameter.
     * @param priority The priority of thread in the poll.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByCpuWithDelay(final Task<T> task,
        final long delay,
        final TimeUnit unit,
        @IntRange(from = 1, to = 10) final int priority) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_CPU, priority), task, delay, unit);
    }

    /**
     * Executes the given task in a cpu thread pool at fix rate.
     *
     * @param task The task to execute.
     * @param period The period between successive executions.
     * @param unit The time unit of the period parameter.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByCpuAtFixRate(final Task<T> task,
        final long period,
        final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CPU), task, 0, period, unit);
    }

    /**
     * Executes the given task in a cpu thread pool at fix rate.
     *
     * @param task The task to execute.
     * @param period The period between successive executions.
     * @param unit The time unit of the period parameter.
     * @param priority The priority of thread in the poll.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByCpuAtFixRate(final Task<T> task,
        final long period,
        final TimeUnit unit,
        @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CPU, priority), task, 0, period, unit);
    }

    /**
     * Executes the given task in a cpu thread pool at fix rate.
     *
     * @param task The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period The period between successive executions.
     * @param unit The time unit of the initialDelay and period parameters.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByCpuAtFixRate(final Task<T> task,
        long initialDelay,
        final long period,
        final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CPU), task, initialDelay, period, unit);
    }

    /**
     * Executes the given task in a cpu thread pool at fix rate.
     *
     * @param task The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period The period between successive executions.
     * @param unit The time unit of the initialDelay and period parameters.
     * @param priority The priority of thread in the poll.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByCpuAtFixRate(final Task<T> task,
        long initialDelay,
        final long period,
        final TimeUnit unit,
        @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(
            getPoolByTypeAndPriority(TYPE_CPU, priority), task, initialDelay, period, unit
        );
    }

    /**
     * Executes the given task in a custom thread pool.
     *
     * @param pool The custom thread pool.
     * @param task The task to execute.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByCustom(final ExecutorService pool, final Task<T> task) {
        execute(pool, task);
    }

    /**
     * Executes the given task in a custom thread pool after the given delay.
     *
     * @param pool The custom thread pool.
     * @param task The task to execute.
     * @param delay The time from now to delay execution.
     * @param unit The time unit of the delay parameter.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByCustomWithDelay(final ExecutorService pool,
        final Task<T> task,
        final long delay,
        final TimeUnit unit) {
        executeWithDelay(pool, task, delay, unit);
    }

    /**
     * Executes the given task in a custom thread pool at fix rate.
     *
     * @param pool The custom thread pool.
     * @param task The task to execute.
     * @param period The period between successive executions.
     * @param unit The time unit of the period parameter.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByCustomAtFixRate(final ExecutorService pool,
        final Task<T> task,
        final long period,
        final TimeUnit unit) {
        executeAtFixedRate(pool, task, 0, period, unit);
    }

    /**
     * Executes the given task in a custom thread pool at fix rate.
     *
     * @param pool The custom thread pool.
     * @param task The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period The period between successive executions.
     * @param unit The time unit of the initialDelay and period parameters.
     * @param <T> The type of the task's result.
     */
    public static <T> void executeByCustomAtFixRate(final ExecutorService pool,
        final Task<T> task,
        long initialDelay,
        final long period,
        final TimeUnit unit) {
        executeAtFixedRate(pool, task, initialDelay, period, unit);
    }

    /**
     * Cancel the given task.
     *
     * @param task The task to cancel.
     */
    public static void cancel(final Task<?> task) {
        if (task == null) return;
        task.cancel();
    }

    /**
     * Cancel the given tasks.
     *
     * @param tasks The tasks to cancel.
     */
    public static void cancel(final Task<?>... tasks) {
        if (tasks == null || tasks.length == 0) return;
        for (Task<?> task : tasks) {
            if (task == null) continue;
            task.cancel();
        }
    }

    /**
     * Cancel the given tasks.
     *
     * @param tasks The tasks to cancel.
     */
    public static void cancel(final List<Task<?>> tasks) {
        if (tasks == null || tasks.size() == 0) return;
        for (Task<?> task : tasks) {
            if (task == null) continue;
            task.cancel();
        }
    }

    /**
     * Cancel the tasks in pool.
     *
     * @param executorService The pool.
     */
    public static void cancel(ExecutorService executorService) {
        if (executorService instanceof YouthPollExecutor) {
            for (Map.Entry<Task<?>, ExecutorService> taskTaskInfoEntry : TASK_POOL_MAP.entrySet()) {
                if (taskTaskInfoEntry.getValue() == executorService) {
                    cancel(taskTaskInfoEntry.getKey());
                }
            }
        } else {
            Log.e(TAG, "The executorService is not ThreadUtils's pool.");
        }
    }

    /**
     * Set the deliver.
     *
     * @param deliver The deliver.
     */
    public static void setDeliver(final Executor deliver) {
        sDeliver = deliver;
    }

    private static <T> void execute(final ExecutorService pool, final Task<T> task) {
        execute(pool, task, 0, 0, null);
    }

    private static <T> void executeWithDelay(final ExecutorService pool,
        final Task<T> task,
        final long delay,
        final TimeUnit unit) {
        execute(pool, task, delay, 0, unit);
    }

    private static <T> void executeAtFixedRate(final ExecutorService pool,
        final Task<T> task,
        long delay,
        final long period,
        final TimeUnit unit) {
        execute(pool, task, delay, period, unit);
    }

    private static <T> void execute(final ExecutorService pool, final Task<T> task,
        long delay, final long period, final TimeUnit unit) {
        synchronized (TASK_POOL_MAP) {
            if (TASK_POOL_MAP.get(task) != null) {
                Log.e(TAG, "Task can only be executed once.");
                return;
            }
            TASK_POOL_MAP.put(task, pool);
        }
        if (period == 0) {
            if (delay == 0) {
                pool.execute(task);
            } else {
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        pool.execute(task);
                    }
                };
                TIMER.schedule(timerTask, unit.toMillis(delay));
            }
        } else {
            task.setSchedule(true);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    pool.execute(task);
                }
            };
            TIMER.scheduleAtFixedRate(timerTask, unit.toMillis(delay), unit.toMillis(period));
        }
    }

    private static ExecutorService getPoolByTypeAndPriority(final int type) {
        return getPoolByTypeAndPriority(type, Thread.NORM_PRIORITY);
    }

    private static ExecutorService getPoolByTypeAndPriority(final int type, final int priority) {
        synchronized (TYPE_PRIORITY_POOLS) {
            ExecutorService pool;
            Map<Integer, ExecutorService> priorityPools = TYPE_PRIORITY_POOLS.get(type);
            if (priorityPools == null) {
                priorityPools = new ConcurrentHashMap<>();
                pool = YouthPollExecutor.createPool(type, priority);
                priorityPools.put(priority, pool);
                TYPE_PRIORITY_POOLS.put(type, priorityPools);
            } else {
                pool = priorityPools.get(priority);
                if (pool == null) {
                    pool = YouthPollExecutor.createPool(type, priority);
                    priorityPools.put(priority, pool);
                }
            }
            return pool;
        }
    }

    static final class YouthPollExecutor extends ThreadPoolExecutor {

        private static ExecutorService createPool(final int type, final int priority) {
            int maxSize = CPU_COUNT + 1;
            if (YouthRomUtils.isHuawei() || Build.VERSION.SDK_INT <= 26) {
                maxSize = CPU_COUNT;
                if(maxSize>4){
                    maxSize = 4;
                }
            }
            switch (type) {
                case TYPE_SINGLE:
                    return new YouthPollExecutor(1, 1,
                        0L, TimeUnit.MILLISECONDS,
                        new YouthLinkedBlockingQueue(),
                        new YouthThreadFactory("single", priority)
                    );
                case TYPE_CACHED:
                    if (YouthRomUtils.isHuawei() || Build.VERSION.SDK_INT <= 26) {
                        maxSize = 20;
                    }else {
                        maxSize = 40;
                    }
                    return new YouthPollExecutor(0, maxSize,
                        30, TimeUnit.SECONDS,
                        new YouthLinkedBlockingQueue(true),
                        new YouthThreadFactory("cached", priority)
                    );
                case TYPE_IO:
                    return new YouthPollExecutor(maxSize, maxSize,
                        30, TimeUnit.SECONDS,
                        new YouthLinkedBlockingQueue(),
                        new YouthThreadFactory("io", priority)
                    );
                case TYPE_CPU:
                    return new YouthPollExecutor(maxSize, maxSize,
                        30, TimeUnit.SECONDS,
                        new YouthLinkedBlockingQueue(true),
                        new YouthThreadFactory("cpu", priority)
                    );
                default:
                    return new YouthPollExecutor(type, type,
                        0L, TimeUnit.MILLISECONDS,
                        new YouthLinkedBlockingQueue(),
                        new YouthThreadFactory("fixed(" + type + ")", priority)
                    );
            }
        }

        private final AtomicInteger mSubmittedCount = new AtomicInteger();

        private final YouthLinkedBlockingQueue mWorkQueue;

        YouthPollExecutor(int corePoolSize, int maximumPoolSize,
            long keepAliveTime, TimeUnit unit,
            YouthLinkedBlockingQueue workQueue,
            ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize,
                keepAliveTime, unit,
                workQueue,
                threadFactory
            );
            workQueue.mPool = this;
            mWorkQueue = workQueue;
        }

        private int getSubmittedCount() {
            return mSubmittedCount.get();
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            mSubmittedCount.decrementAndGet();
            super.afterExecute(r, t);
        }

        @Override
        public void execute(@NonNull Runnable command) {
            if (this.isShutdown()) return;
            mSubmittedCount.incrementAndGet();
            try {
                super.execute(command);
            } catch (RejectedExecutionException ignore) {
                Log.e(TAG, "This will not happen!");
                mWorkQueue.offer(command);
            } catch (Throwable t) {
                mSubmittedCount.decrementAndGet();
            }
        }
    }

    private static final class YouthLinkedBlockingQueue extends LinkedBlockingQueue<Runnable> {

        private volatile YouthPollExecutor mPool;

        private int mCapacity = Integer.MAX_VALUE;

        YouthLinkedBlockingQueue() {
            super();
        }

        YouthLinkedBlockingQueue(boolean isAddSubThreadFirstThenAddQueue) {
            super();
            if (isAddSubThreadFirstThenAddQueue) {
                mCapacity = 0;
            }
        }

        YouthLinkedBlockingQueue(int capacity) {
            super();
            mCapacity = capacity;
        }

        @Override
        public boolean offer(@NonNull Runnable runnable) {
            if (mCapacity <= size() &&
                mPool != null && mPool.getPoolSize() < mPool.getMaximumPoolSize()) {
                // create a non-core thread
                return false;
            }
            return super.offer(runnable);
        }
    }

    static final class YouthThreadFactory extends AtomicLong implements ThreadFactory {
        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        private static final long serialVersionUID = -9209200509960368598L;
        private final String namePrefix;
        private final int priority;
        private final boolean isDaemon;

        YouthThreadFactory(String prefix, int priority) {
            this(prefix, priority, false);
        }

        YouthThreadFactory(String prefix, int priority, boolean isDaemon) {
            namePrefix = prefix + "-pool-" +
                POOL_NUMBER.getAndIncrement() +
                "-thread-";
            this.priority = priority;
            this.isDaemon = isDaemon;
        }

        @Override
        public Thread newThread(@NonNull Runnable r) {
            YouthThread t = new YouthThread(r, namePrefix + getAndIncrement()) {
                @Override
                public void run() {
                    try {
                        super.run();
                    } catch (Throwable t) {
                        Log.e(TAG, "Request threw uncaught throwable", t);
                    }
                }
            };
            t.setDaemon(isDaemon);
            t.setUncaughtExceptionHandler((t1, e) -> Log.d(TAG, "uncaughtExceptionHandler: " + t1, e));
            t.setPriority(priority);
            return t;
        }
    }

    static class YouthThread extends Thread {

        public YouthThread() {
        }

        public YouthThread(@Nullable Runnable target) {
            super(target);
        }

        public YouthThread(@Nullable ThreadGroup group, @Nullable Runnable target) {
            super(group, target);
        }

        public YouthThread(@NonNull String name) {
            super(name);
        }

        public YouthThread(@Nullable ThreadGroup group, @NonNull String name) {
            super(group, name);
        }

        public YouthThread(@Nullable Runnable target, @NonNull String name) {
            super(target, name);
        }

        public YouthThread(@Nullable ThreadGroup group, @Nullable Runnable target, @NonNull String name) {
            super(group, target, name);
        }

        public YouthThread(@Nullable ThreadGroup group, @Nullable Runnable target, @NonNull String name,
            long stackSize) {
            super(group, target, name, stackSize);
        }

        @Override
        public synchronized void start() {
            try {
                super.start();
            } catch (OutOfMemoryError error) {
                Log.d(TAG, "YouthThread#start: ", error);
            }
        }

        @NonNull @Override
        public String toString() {
            ThreadGroup group = getThreadGroup();
            if (group != null) {
                return "YouthThread[" + getName() + "," + getPriority() + "," + group.getName() + "]";
            } else {
                return "YouthThread[" + getName() + "," + getPriority() + "," + "" + "]";
            }
        }
    }

    public abstract static class SimpleTask<T> extends Task<T> {

        @Override
        public void onCancel() {
            Log.e(TAG, "onCancel: " + Thread.currentThread());
        }

        @Override
        public void onFail(Throwable t) {
            Log.e(TAG, "onFail: ", t);
        }
    }

    public abstract static class Task<T> implements Runnable {

        private static final int NEW = 0;
        private static final int RUNNING = 1;
        private static final int EXCEPTIONAL = 2;
        private static final int COMPLETING = 3;
        private static final int CANCELLED = 4;
        private static final int INTERRUPTED = 5;
        private static final int TIMEOUT = 6;

        private final AtomicInteger state = new AtomicInteger(NEW);

        private volatile boolean isSchedule;
        private volatile Thread runner;

        private Timer mTimer;
        private long mTimeoutMillis;
        private OnTimeoutListener mTimeoutListener;

        private Executor deliver;

        public abstract T doInBackground() throws Throwable;

        public abstract void onSuccess(T result);

        public abstract void onCancel();

        public abstract void onFail(Throwable t);

        @Override
        public void run() {
            if (isSchedule) {
                if (runner == null) {
                    if (!state.compareAndSet(NEW, RUNNING)) return;
                    runner = Thread.currentThread();
                    if (mTimeoutListener != null) {
                        Log.w(TAG, "Scheduled task doesn't support timeout.");
                    }
                } else {
                    if (state.get() != RUNNING) return;
                }
            } else {
                if (!state.compareAndSet(NEW, RUNNING)) return;
                runner = Thread.currentThread();
                if (mTimeoutListener != null) {
                    mTimer = new Timer();
                    mTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (!isDone() && mTimeoutListener != null) {
                                timeout();
                                mTimeoutListener.onTimeout();
                                onDone();
                            }
                        }
                    }, mTimeoutMillis);
                }
            }
            try {
                final T result = doInBackground();
                if (isSchedule) {
                    if (state.get() != RUNNING) return;
                    getDeliver().execute(() -> onSuccess(result));
                } else {
                    if (!state.compareAndSet(RUNNING, COMPLETING)) return;
                    getDeliver().execute(() -> {
                        onSuccess(result);
                        onDone();
                    });
                }
            } catch (InterruptedException ignore) {
                state.compareAndSet(CANCELLED, INTERRUPTED);
            } catch (final Throwable throwable) {
                if (!state.compareAndSet(RUNNING, EXCEPTIONAL)) return;
                getDeliver().execute(() -> {
                    onFail(throwable);
                    onDone();
                });
            }
        }

        public void cancel() {
            cancel(true);
        }

        public void cancel(boolean mayInterruptIfRunning) {
            synchronized (state) {
                if (state.get() > RUNNING) return;
                state.set(CANCELLED);
            }
            if (mayInterruptIfRunning) {
                if (runner != null) {
                    runner.interrupt();
                }
            }

            getDeliver().execute(() -> {
                onCancel();
                onDone();
            });
        }

        private void timeout() {
            synchronized (state) {
                if (state.get() > RUNNING) return;
                state.set(TIMEOUT);
            }
            if (runner != null) {
                runner.interrupt();
            }
        }

        public boolean isCanceled() {
            return state.get() >= CANCELLED;
        }

        public boolean isDone() {
            return state.get() > RUNNING;
        }

        public Task<T> setDeliver(Executor deliver) {
            this.deliver = deliver;
            return this;
        }

        /**
         * Scheduled task doesn't support timeout.
         */
        public Task<T> setTimeout(final long timeoutMillis, final OnTimeoutListener listener) {
            mTimeoutMillis = timeoutMillis;
            mTimeoutListener = listener;
            return this;
        }

        private void setSchedule(boolean isSchedule) {
            this.isSchedule = isSchedule;
        }

        private Executor getDeliver() {
            if (deliver == null) {
                return getGlobalDeliver();
            }
            return deliver;
        }

        @CallSuper
        protected void onDone() {
            TASK_POOL_MAP.remove(this);
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
                mTimeoutListener = null;
            }
        }

        public interface OnTimeoutListener {
            void onTimeout();
        }
    }

    private static Executor getGlobalDeliver() {
        if (sDeliver == null) {
            sDeliver = YouthThreadUtils::runOnUiThread;
        }
        return sDeliver;
    }
}
