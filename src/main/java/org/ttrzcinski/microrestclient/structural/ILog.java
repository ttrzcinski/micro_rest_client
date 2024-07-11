package org.ttrzcinski.microrestclient.structural;

public interface ILog {

    static void d(String entry) {
        System.out.println("[DEBUG] " + entry);
    }

    static void w(String entry) {
        System.out.println("[WARNING] " + entry);
    }

    static void i(String entry) {
        System.out.println("[INFO] " + entry);
    }

    static void e(String entry) {
        System.err.println("[ERROR] " + entry);
    }

    static void e(String entry, Throwable thrown) {
        System.err.println("[ERROR] " + entry);
        if (thrown == null) {
            return;
        }
        thrown.printStackTrace();
    }
}
