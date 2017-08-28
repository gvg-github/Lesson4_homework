public class MFU {

    Object scan;
    Object print;
    private static boolean printing = false;
    private static boolean scanning = false;

    MFU(Object scan, Object print) {
        this.scan = scan;
        this.print = print;
    }

    public void print(int pages) {

        Thread tPrint = new Thread(new Runnable() {
            @Override
            public void run() {

                synchronized (print) {

                    try {
                        while (printing) {
                            print.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    printing = true;

                    printing = printingPages(pages);
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

                synchronized (scan) {
                    while (scanning) {
                        try {
                            scan.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    scanning = true;

                    scanning = scanningPages(pages);
                    scan.notifyAll();
                }
            }
        });
        tScan.start();
    }

    private boolean scanningPages(int pages) {
        if (pages == 0) return false;
        for (int i = 1; i <= pages; i++) {
            try {
                Thread.sleep(50);
                System.out.println(Thread.currentThread().getName() + ". Scanned " + i + " pages.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean printingPages(int pages) {
        if (pages == 0) return false;
        for (int i = 1; i <= pages; i++) {
            try {
                Thread.sleep(50);
                System.out.println(Thread.currentThread().getName() + ". Printed " + i + " pages.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
