package slow;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Accumulator {
  private BigInteger result = BigInteger.ZERO;

  public BigInteger sum(List<BigInteger> numbers) {
    ExecutorService executorService = Executors.newFixedThreadPool(8);
    for (final BigInteger number : numbers) {
      executorService.submit(new Runnable() {
        @Override
        public void run() {
          result = result.add(number);
        }
      });
    }
    executorService.shutdown();
    return result;
  }
}
