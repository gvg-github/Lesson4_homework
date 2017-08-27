import java.io.*;

/**
 * Created by GVG on 26.08.2017.
 */
public class Main {

    private static final Object mon = new Object();
    private static String currentLetter;

    public static void main(String[] args) {

        PrintLettersABC();
        WriteToFile();
        WorkWithMFU();

    }

    public static void PrintLettersABC() {
        currentLetter = "A";
        int count = 5;
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                printLetter(count, "A", "B");
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                printLetter(count, "B", "C");
            }
        });
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                printLetter(count, "C", "A");
            }
        });
        t1.start();
        t2.start();
        t3.start();
    }

    public static void printLetter(int count, String printL, String nextL) {
        synchronized (mon) {
            try {
                for (int i = 1; i <= count; i++) {
                    while (currentLetter != printL) {
                        mon.wait();
                    }
                    System.out.print(printL);
                    currentLetter = nextL;
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void WriteToFile() {

        int count = 10;
        File file = new File("1.txt");

        try {
            FileOutputStream fs1 = new FileOutputStream(file);
            String text = "Text from thread";

            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    print(text + " 1; ", count, fs1);
                }
            });

            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    print(text + " 2; ", count, fs1);
                }
            });

            Thread t3 = new Thread(new Runnable() {
                @Override
                public void run() {
                    print(text + " 3; ", count, fs1);
                }
            });

            t1.start();
            t2.start();
            t3.start();

            try {
                t1.join();
                t2.join();
                t3.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!t1.isAlive() && !t2.isAlive() && !t3.isAlive()) {
                try {
                    fs1.close();
                    System.out.println("\n" + "Text write to 1.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void print(String text, int count, FileOutputStream fs1) {

        byte[] b1 = text.getBytes();
        for (int j = 0; j < count; j++) {
            try {
                Thread.sleep(20);
                fs1.write(b1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void WorkWithMFU() {
        Object scan = new Object();
        Object print = new Object();
        MFU mfu = new MFU(scan, print);
        mfu.print(10);
        mfu.print(2);
        mfu.print(4);
        mfu.scan(5);
        mfu.scan(7);
        mfu.scan(4);
    }

}
