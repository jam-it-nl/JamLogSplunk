package jamcommons.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

import com.mendix.core.Core;
import com.mendix.logging.ILogNode;

public class SerialExecutor {

	private AtomicLong tasknumber = new AtomicLong();
	private ExecutorService executor;
	private String queue;

	private SerialExecutor(String queue) {
		this.queue = queue;
		executor = Executors.newSingleThreadExecutor(new ThreadFactory() {
			private ThreadFactory defaultFactory = Executors.defaultThreadFactory();

			@Override
			public Thread newThread(Runnable runnable) {
				Thread thread = defaultFactory.newThread(runnable);
				thread.setPriority(Thread.MIN_PRIORITY);
				thread.setName(queue);
				return thread;
			}
		});
	}

	public void execute(Runnable command) {
		if (command == null) {
			throw new NullPointerException("command");
		}

		long currentTasknumber = tasknumber.incrementAndGet();
		LOG.debug("[" + queue + "] Scheduling task #" + currentTasknumber);

		executor.submit(new Runnable() {
			@Override
			public void run() {
				LOG.debug("[" + queue + "] Running task #" + currentTasknumber);
				try {
					command.run();
				} catch (Exception e) {
					LOG.error("[" + queue + "] Execution of task #" + currentTasknumber + " failed: " + e.getMessage(), e);
					throw e; // single thread executor will continue, even if an exception is thrown.
				}
				LOG.debug("[" + queue + "] Completed task #" + currentTasknumber + ". Tasks left: " + (tasknumber.get() - currentTasknumber));
			}
		});
	}

	private static final ILogNode LOG = Core.getLogger(SerialExecutor.class.getSimpleName());
	private static Map<String, SerialExecutor> instances = new HashMap<>();

	public static SerialExecutor getInstance(String queue) {
		if (instances.containsKey(queue)) {
			return instances.get(queue);
		} else {
			SerialExecutor instance = new SerialExecutor(queue);
			instances.put(queue, instance);
			return instance;
		}
	}
}
