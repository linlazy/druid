package com.example.druid;

import com.example.druid.db.queue.NamedThreadFactory;

/**
 * @author linlazy
 */
public class Test {

    public static void main(String[] args) {



        /**
         * db执行线程
         */
        ThreadGroup threadGroup = new ThreadGroup("dbGroup");
        NamedThreadFactory dbPrepareThreadFactory = new NamedThreadFactory(threadGroup,
                "dbPrepareThread");

        Thread pollDBQueueThread = dbPrepareThreadFactory.newThread(() -> {
                System.out.println("dbPrepareThreadFactory");
        });
        pollDBQueueThread.setDaemon(true);
        pollDBQueueThread.start();

        try {
            Thread.sleep(30000);
            System.out.println("pollDBQueueThread");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
