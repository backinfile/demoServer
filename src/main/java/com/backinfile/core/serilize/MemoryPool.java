package com.backinfile.core.serilize;


import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.backinfile.support.Log;

public class MemoryPool {
    private static final int MAX_MEMORY_COUNT = 20;

    private static final ConcurrentLinkedQueue<AutoExpandStream> memories = new ConcurrentLinkedQueue<>();
    private static final AtomicInteger countCreated = new AtomicInteger(0);

    public AutoExpandStream newMemory() {
        AutoExpandStream mem = memories.poll();
        if (mem == null) {
            mem = new AutoExpandStream();
            int count = countCreated.incrementAndGet();
            Log.Core.warn("创建新的缓冲池，已创建count = {0}", count);
        }
        return mem;
    }

    public void delMemory(AutoExpandStream mem) {
        mem.clear();
        memories.add(mem);
    }
}
