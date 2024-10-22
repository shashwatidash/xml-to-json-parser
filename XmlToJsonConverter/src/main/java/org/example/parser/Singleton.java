package org.example.parser;

public class Singleton {

    private static Singleton singletonInstance;

    private Singleton() {

    }

    public static Singleton getInstance() {
        // synchronized ()
        if (singletonInstance == null) {
            singletonInstance = new Singleton();
        }
        return singletonInstance;
    }

}
