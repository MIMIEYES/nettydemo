package com.mimieye.netty.common;

import io.netty.util.internal.PlatformDependent;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicLong;

public class PlatformDepedentReporter extends Thread {

    private AtomicLong directMemory = null;

    public PlatformDepedentReporter init() {
        Field[] fields = PlatformDependent.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {

            fields[i].setAccessible(true);

            String fieldName = fields[i].getName();
            if ("DIRECT_MEMORY_COUNTER".equals(fieldName)) {
                try {
                    directMemory = (AtomicLong) fields[i].get(PlatformDependent.class);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    doReport();
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    private void doReport() {
        if (directMemory != null) {
            System.out.println("--------------------------directMemory:" + directMemory.get());
        }
    }
}
