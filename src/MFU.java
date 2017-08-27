/**
 * Created by GVG on 27.08.2017.
 */
public class MFU {

    Object scan;
    Object print;

    MFU(Object scan, Object print) {
        this.scan = scan;
        this.print = print;
    }

    public void print(int pages) {

        Thread tPrint = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (scan){
                    scan.notifyAll();
                }
                synchronized (print) {
                    printPages(pages);
                    print.notifyAll();
                    try {
                        print.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    print.notifyAll();
                }
            }
        });
        tPrint.start();
    }

    public void scan(int pages) {
        Thread tScan = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (print){
                    print.notifyAll();
                }
                synchronized (scan) {
                    scan.notifyAll();
                    scanPages(pages);
                    try {
                        scan.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    scan.notifyAll();
                }
            }
        });
        tScan.start();
    }

    private synchronized void printPages(int pages) {
        if (pages == 0) return;
        for (int i = 1; i <= pages; i++) {
            try {
                Thread.sleep(50);
                System.out.println(Thread.currentThread().getName() + ". Printed " + i + " pages.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void scanPages(int pages) {
        if (pages == 0) return;
        for (int i = 1; i <= pages; i++) {
            try {
                Thread.sleep(50);
                System.out.println(Thread.currentThread().getName() + ". Scanned " + i + " pages.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
