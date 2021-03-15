package bexysuttx.jmemcached_server.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;

import bexysuttx.Jmemcached_common.jmemcashed.protocol.model.Status;
import bexysuttx.jmemcached_server.ServerConfig;

public class DefaultStorageTest {

    private ExecutorService executorServiceMock;
    private ServerConfig serverConfig;
    private DefaultStorage defaultStorage;

    @Before
    public void before() {
        executorServiceMock = mock(ExecutorService.class);
        serverConfig = mock(ServerConfig.class);
        when(serverConfig.getClearDataIntervalInMs()).thenReturn(10);
        defaultStorage = new DefaultStorage(serverConfig) {
            @Override
            protected ExecutorService createClearExpiredDataExecutorService() {
                return executorServiceMock;
            }
        };
        // Default state for storage
        defaultStorage.put("test", TimeUnit.SECONDS.toMillis(1), new byte[]{5, 6, 7});
    }

    @Test
    public void startClearExpiredDataExecutorService() throws IllegalAccessException {
        Runnable clearExpiredDataJob = (Runnable) FieldUtils.getDeclaredField(
                DefaultStorage.class, "clearExpiredDataJob", true).get(defaultStorage);
        verify(executorServiceMock).submit(clearExpiredDataJob);
    }

    @Test
    public void close() throws Exception {
        defaultStorage.close();
        verify(executorServiceMock, never()).shutdownNow();
        verify(executorServiceMock, never()).shutdown();
    }

    @Test
    public void createClearExpiredDataThreadFactory() {
        ThreadFactory threadFactory = defaultStorage.createClearExpiredDataThreadFactory();
        Thread thread = threadFactory.newThread(mock(Runnable.class));
        assertTrue(thread.isDaemon());
        assertEquals(Thread.MIN_PRIORITY, thread.getPriority());
        assertEquals("clearExpiredDataJobThread", thread.getName());
    }

    @Test
    public void putAdded() {
        Status status = defaultStorage.put("key", null, new byte[]{1, 2, 3});
        assertEquals(Status.ADDED, status);
    }

    @Test
    public void putReplaced() {
        Status status = defaultStorage.put("test", null, new byte[]{1, 2, 3});
        assertEquals(Status.REPLACED, status);
    }

    @Test
    public void getSuccess() {
        byte[] data = defaultStorage.get("test");
        assertArrayEquals(new byte[]{5, 6, 7}, data);
    }

    @Test
    public void getNotFound() {
        byte[] data = defaultStorage.get("not_found");
        assertNull(data);
    }

    @Test
    public void getExpired() throws InterruptedException {
        // Default test value ttl is 1 SECOND
        TimeUnit.MILLISECONDS.sleep(1100);
        byte[] data = defaultStorage.get("test");
        assertNull(data);
    }

    @Test
    public void removeSuccess() {
        Status status = defaultStorage.remove("test");
        assertEquals(Status.REMOVED, status);
    }

    @Test
    public void removeNotFound() {
        Status status = defaultStorage.remove("not_found");
        assertEquals(Status.NOT_FOUND, status);
    }

    @Test
    public void removeExpired() throws InterruptedException {
        // Default test value ttl is 1 SECOND
        TimeUnit.MILLISECONDS.sleep(1100);
        Status status = defaultStorage.remove("test");
        assertEquals(Status.NOT_FOUND, status);
    }

    @Test
    public void clear() {
        Status status = defaultStorage.clear();
        assertEquals(Status.CLEARED, status);
        assertNull(defaultStorage.get("test"));
    }
}