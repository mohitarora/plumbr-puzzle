package slow;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import slow.internal.Source;

public class Worker implements Runnable {
  private final int lineIndex;
  private final CountDownLatch latch;

  public static final List<BigInteger> numbers = new LinkedList<>();
  private static final Object lock = new Object();
  private static final Cache cache = new Cache();

  public Worker(int lineIndex, CountDownLatch latch) {
    this.lineIndex = lineIndex;
    this.latch = latch;
  }

  @Override
  public void run() {
    try {
      long start = System.currentTimeMillis();
      synchronized (numbers) {
        int numberIndex = Source.getValueAtLine(lineIndex);
        numbers.add(calculateSecret(BigInteger.valueOf(numberIndex)));
      }
      latch.countDown();
      System.out.printf("Secret calculated in %d ms. Remaining secrets to calculate: %d%n", System.currentTimeMillis() - start, latch.getCount());
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  public BigInteger calculateSecret(BigInteger index) {
    if (index.compareTo(new BigInteger("0")) < 0) {
      return new BigInteger("0");
    }
    if (index.equals(new BigInteger("2"))) {
      return new BigInteger("1");
    }
    synchronized (lock) {
      BigInteger value = (BigInteger) cache.get(index);
      if (value != null) {
        return value;
      } else {
        value = calculateSecret(index.subtract(new BigInteger("1"))).add(calculateSecret(index.subtract(new BigInteger("2"))));
        cache.put(index, value);
        return value;
      }
    }
  }
}
