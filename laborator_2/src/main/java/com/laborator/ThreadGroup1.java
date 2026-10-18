package com.laborator;

public class ThreadGroup1 {
    public static void main(String[] args) {
        ThreadGroup sys = Thread.currentThread().getThreadGroup();
        sys.list();
    }
}