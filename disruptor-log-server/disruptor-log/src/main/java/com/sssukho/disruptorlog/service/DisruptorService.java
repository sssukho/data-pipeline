package com.sssukho.disruptorlog.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.sssukho.disruptorlog.dto.LogEvent;
import com.sssukho.disruptorlog.meta.TraceMetaInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadFactory;

@Slf4j
public class DisruptorService {

    Object service;

    ArrayProcessingService ais;
    public TraceMetaInfo info;

    private static final int DEFAULT_RINGBUFFER_SIZE = 2 * 1024;
    public String ASYNC_LOGGER_THREAD_NAME = "";
    public int CONSUMER_THREADS = 1;

    private RingBuffer<LogEvent> RING_BUFFER;
    private EventHandler<LogEvent>[] eventHandlers;
    private EventTranslatorTwoArg<LogEvent, Object, DeferredResult> EVENT_TRANSLATOR;
    private int asyncQueueSize = DEFAULT_RINGBUFFER_SIZE;
    private volatile boolean initialized = false;

    public void init(String name, int threadCount, TraceMetaInfo info, ArrayProcessingService ais) {
        ASYNC_LOGGER_THREAD_NAME = name;
        if(threadCount > 0) {
            CONSUMER_THREADS = threadCount;
        }

        init();

        this.info = info;
        this.ais = ais;
    }

    public void event(Map logMap, DeferredResult output) {
        this.publishEvent(logMap, output);
    }

    protected void init() {
        if(initialized) {
            return;
        }

        synchronized (this) {
            if(initialized) { // double check
                return;
            }
            // waitStrategy
            WaitStrategy waitStrategy = new BlockingWaitStrategy();
            ThreadFactory factory = new ThreadFactory() {
                @Override
                public Thread newThread(Runnable runnable) {
                    Thread thread = new Thread(runnable, ASYNC_LOGGER_THREAD_NAME);
                    thread.setDaemon(true);
                    thread.setPriority(Thread.NORM_PRIORITY);
                    return thread;
                }
            };

            // Ring Buffer
            Disruptor<LogEvent> disruptor = new Disruptor(LogEvent.FACTORY,
                    asyncQueueSize,
                    factory,
                    ProducerType.MULTI,
                    waitStrategy);

            this.eventHandlers = new EventHandlerImpl[CONSUMER_THREADS];
            for (int i = 0; i < CONSUMER_THREADS; i++) {
                this.eventHandlers[i] = new EventHandlerImpl(i);
            }

            disruptor.handleEventsWith(this.eventHandlers);
            disruptor.start(); // start the consumer thread

            RING_BUFFER = disruptor.getRingBuffer();
            EVENT_TRANSLATOR =
                    new EventTranslatorTwoArg<LogEvent, Object, DeferredResult>() {
                        @Override
                        public void translateTo(LogEvent event, long sequence, Object logString, DeferredResult output) {
                            event.set(logString, output);
                        }
                    };
            initialized = true;
        }
    }

    public void process(ArrayList<LogEvent> list) {
        ArrayList<HashMap> insertLogList = new ArrayList<HashMap>();
        HashMap<String, ArrayList<HashMap>> updateLogListMap = new HashMap<>();

        for (LogEvent e : list) {
            HashMap map = (HashMap) e.getLogString();
            List<HashMap> body = (List<HashMap>) map.get("body");
            for (HashMap data : body) {
                insertLogList.add(data);
            }
        }

        try {
            log.info("Counts of trace logs to be inserted : {}", insertLogList.size());
            ais.arrayInsert(info, insertLogList);

            // bulk insert 완료 후 200 OK
            for (LogEvent event : list) {
                event.setSuccess();
                event.clear();
            }
        } catch (Exception e) {
            log.error("Inserting log data has failed. Check failed logs. => {}", e);

            // 실패한 경우에는 로그 파일에다 쓰고 나서 200 OK
            for(LogEvent event : list) {
                event.setSuccess();
                event.clear();
            }
        }
    }

    // worker threads calls this
    public void publishEvent(Object logString, DeferredResult output) {
        if (logString == null) {
            return;
        }

        RING_BUFFER.publishEvent(EVENT_TRANSLATOR, logString, output);
    }

    boolean tryPublishEvent(Object logString, DeferredResult output) {
        if (logString == null) {
            return true;
        }

        return RING_BUFFER.tryPublishEvent(EVENT_TRANSLATOR, logString, output);
    }

    class EventHandlerImpl<T1, T2> implements EventHandler<LogEvent>, LifecycleAware {
        private int id;
        ObjectMapper mapper = new ObjectMapper();
        private ArrayList<LogEvent> list = new ArrayList<LogEvent>();
        public EventHandlerImpl(int id) { this.id = id; }

        @Override
        public void onEvent(LogEvent event, long sequence, boolean endOfBatch) throws Exception {

            if (sequence % CONSUMER_THREADS == id) {
                list.add(event);
            }

            if(endOfBatch && list.size() > 0) {
                if(list.size() > 0) {
                    process(list);
                }
                list.clear();
            }
        }

        //
        // LifecycleAware I/F methods
        //
        /**
         * Called once on Consumer thread start before first event is available. async writer thread
         * (single thread) calls this.
         */
        @Override
        public void onStart() { }

        /**
         * <p>
         * Called once just before the Consumer thread is shutdown.
         * </p>
         * Sequence event processing will already have stopped before this method is called. No
         * events will be processed after this message. async writer thread (single thread) calls
         * this.
         */
        @Override
        public void onShutdown() { }
    }
}
