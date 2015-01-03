package slow.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import slow.Cache;

public class Source {
  private static final Cache cache = new Cache();

  private static int[] readData() {
    long start = System.currentTimeMillis();

    String url = "http://ec2-54-228-47-91.eu-west-1.compute.amazonaws.com/numbers.txt";
    int[] values = (int[]) cache.get(url);
    if(values != null){
      return values;
    }

    values = readExternalData(url);
    cache.put(url, values);
    return values;
  }

  private static int[] readExternalData(String url) {
    int[] values;
    values = new int[1_800_000];
    try (InputStream inputStream = new URL(url).openConnection().getInputStream();
         BufferedReader r = new BufferedReader(new InputStreamReader(inputStream))) {
      String l;
      int count = 0;
      while ((l = r.readLine()) != null) {
        values[count++] = Integer.valueOf(l);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return values;
  }

  public static int getValueAtLine(int lineIndex) {
    return readData()[lineIndex];
  }
}
