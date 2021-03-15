package bexysuttx.jmemcached_server.impl;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Status;
import bexysuttx.jmemcached_server.ServerConfig;
import bexysuttx.jmemcached_server.Storage;

class DefaultStorage implements Storage {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultStorage.class);
	protected final Map<String, StorageItem> map;
	protected ExecutorService executorService;
	protected Runnable clearExpiredDataJob;

	DefaultStorage(ServerConfig serverConfig) {
		int clearDataIntervalInMs = serverConfig.getClearDataIntervalInMs();
		this.map = createMap();
		this.executorService = createClearExpiredDataExecutorService();
		this.clearExpiredDataJob = new ClearExpiredDataJob(map, clearDataIntervalInMs);
		this.executorService.submit(clearExpiredDataJob);

	}

	protected Map<String, StorageItem> createMap() {
		return new ConcurrentHashMap<>();
	}

	protected ExecutorService createClearExpiredDataExecutorService() {
		return Executors.newSingleThreadExecutor(createClearExpiredDataThreadFactory());
	}

	protected ThreadFactory createClearExpiredDataThreadFactory() {
		return new ThreadFactory() {

			@Override
			public Thread newThread(Runnable r) {
				Thread clearExpiredDataJobThread = new Thread(r, "clearExpiredDataJobThread");
				clearExpiredDataJobThread.setDaemon(true);
				clearExpiredDataJobThread.setPriority(Thread.MIN_PRIORITY);
				return clearExpiredDataJobThread;
			}
		};

	}

	@Override
	public void close() throws Exception {
		// Thread demon destroy automatically

	}

	@Override
	public Status put(String key, Long ttl, byte[] data) {
		StorageItem oldItem = map.put(key, new StorageItem(key, ttl, data));
		if (oldItem == null) {
			return Status.ADDED;
		} else {
			return Status.REPLACED;
		}
	}

	@Override
	public byte[] get(String key) {
		StorageItem item = map.get(key);
		if (item == null || item.isExpired()) {
			return null;
		}
		return item.data;
	}

	@Override
	public Status remove(String key) {
		StorageItem item = map.remove(key);
		return item != null && !item.isExpired() ? Status.REMOVED : Status.NOT_FOUND;
	}

	@Override
	public Status clear() {
		map.clear();
		return Status.CLEARED;
	}

	protected static class StorageItem {
		private final String key;
		private final byte[] data;
		private final Long ttl;

		StorageItem(String key, Long ttl, byte[] data) {
			this.key = key;
			this.data = data;
			this.ttl = ttl != null ? ttl + System.currentTimeMillis() : null;
		}

		protected boolean isExpired() {
			return ttl != null && ttl.longValue() < System.currentTimeMillis();
		}

		@Override
		public String toString() {
			StringBuilder s = new StringBuilder(key);
			if (data == null) {
				s.append("=null");
			} else {
				s.append("=").append(data.length).append(" bytes");
			}
			if (ttl != null) {
				s.append(" (").append(new Date(this.ttl.longValue())).append(')');
			}

			return s.toString();
		}

	}

	protected static class ClearExpiredDataJob implements Runnable {
		private final Map<String, StorageItem> map;
		private final int clearDataIntervalInMs;

		public ClearExpiredDataJob(Map<String, StorageItem> map, int clearDataIntervalInMs) {
			this.map = map;
			this.clearDataIntervalInMs = clearDataIntervalInMs;
		}

		protected boolean isStopRun() {
			return Thread.interrupted();
		}

		protected void sleepClearExpiredDataJob() throws InterruptedException {
			TimeUnit.MILLISECONDS.sleep(clearDataIntervalInMs);
		}

		@Override
		public void run() {
			LOGGER.debug("ClearExpiredDataThread started with interval: " + clearDataIntervalInMs + " ms");
			while (!isStopRun()) {
				LOGGER.trace("Invoke clear job");
				for (Map.Entry<String, StorageItem> entry : map.entrySet()) {
					if (entry.getValue().isExpired()) {
						StorageItem item = map.remove(entry.getKey());
						LOGGER.debug("Remove expired storage item:{}", item);
					}
				}
				try {
					sleepClearExpiredDataJob();
				} catch (InterruptedException e) {
					break;
				}
			}

		}

	}

}
