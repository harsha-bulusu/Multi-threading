import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;


class PrimeNumberCount {

    AtomicInteger totalPrimes = new AtomicInteger(0);
    AtomicInteger currValue = new AtomicInteger(0);

    private boolean isPrime(int num) {
        if (num == 0 || num == 1) return false;

        for (int i = 2; i <= (int)Math.sqrt(num); i++) {
            if (num % i == 0) return false;
        }

        return true;
    }

    private void countPrimes(int start, int end) {
        LocalDateTime startTime = LocalDateTime.now();

        for (int i = start; i <= end; i++) {
            if (isPrime(i)) totalPrimes.incrementAndGet();
        }

        LocalDateTime endTime = LocalDateTime.now();
        Duration duration = Duration.between(startTime, endTime);
        System.out.println(Thread.currentThread() + " has completed execution in " + duration.getSeconds());
    }

    private static void countPrimesByThreading(int start, int end, int threadCount) {
        int total = end - start + 1;
        int threadShare = total / threadCount;
        PrimeNumberCount obj = new PrimeNumberCount();
        int cnt = 0;

        for (int i = 0; i < threadCount; i++) {
            int threadStart = cnt;
            int threadEnd = cnt + threadShare;
            Runnable task = () -> obj.countPrimes(threadStart, threadEnd);
            cnt += threadShare;
            Thread thread = new Thread(task);
            thread.start();
        }
    }

    private static void countPrimesInSync() {
        PrimeNumberCount obj = new PrimeNumberCount();
        long startTime = System.currentTimeMillis();
        obj.countPrimes(1, 100000000);
        long endTime = System.currentTimeMillis();

        System.out.println("Time taken for counting is: " + ((double)(endTime - startTime) / (double)(1000 * 60)) + " mins \nTotal number of primes are: " + obj.totalPrimes.get());
    }

    private static void countPrimesByThreadingWithFairness(int start, int end, int threadCount) {
        PrimeNumberCount obj = new PrimeNumberCount();
        for (int i = 0; i < threadCount; i++) {
            Runnable task = () -> {
                LocalDateTime startTime = LocalDateTime.now();
                int val = obj.currValue.get();
                while (val < end) {
                    obj.isPrime(val);
                    val = obj.currValue.incrementAndGet();
                }
                LocalDateTime endTime = LocalDateTime.now();
                Duration duration = Duration.between(startTime, endTime);
                System.out.println(Thread.currentThread().getName() + " has completed execution in " + duration.getSeconds());
            };

            Thread thread = new Thread(task);
            thread.start();
        }
    }

    public static void main(String[] args) {
        // countPrimesInSync();
        countPrimesByThreading(1, 1000000000, 10);
        // countPrimesByThreadingWithFairness(1, 1000000000, 10);
    }
}