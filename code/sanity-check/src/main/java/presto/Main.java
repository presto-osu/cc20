package presto;

import com.google.common.collect.Sets;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.apache.commons.math3.util.Pair;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

public class Main extends BaseMain {
  Main(String[] args) {
    super(args);
  }

  public static void main(String[] args) {
    new Main(args).run();
  }

  @Override
  void run() {
    Set<Pair<String, String>> pairs = Sets.newHashSet();
    String lePairCSVName = Paths.get("le-pairs-csv", app + ".csv").toAbsolutePath().toString();
    try (CSVReader reader =
        new CSVReaderBuilder(new FileReader(lePairCSVName)).withCSVParser(parser).build()) {
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null) {
        pairs.add(new Pair<String, String>(nextLine[0].trim(), nextLine[1].trim()));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    long total = funcs.size() * 5;
    double counter = 0.;
    for (Profile prof : profiles) {
      if (prof.getTotalFreq() != total) {
        System.out.printf("inconsistency:\t%d\t%s%n", prof.getTotalFreq(), prof.jf);
      }
      for (String f : prof.funcProfiles.keySet()) {
        if (!funcs.contains(f)) {
          System.out.printf("wrong_method:\t%s\t%s%n", f, prof.jf);
        }
      }
      for (Pair<String, String> p : pairs) {
        long f1 = prof.get(p.getFirst());
        long f2 = prof.get(p.getSecond());
        if (f1 > f2) {
          counter += 1;
          System.out.println("actual_pair:\t" + p);
        }
      }
    }
    System.out.printf("greater_than_pairs:\t%s\t%s%n", counter / profiles.size(), pairs.size());
  }

  @Override
  double runTrial(int trial_num) {
    return 0;
  }
}
