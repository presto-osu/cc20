package presto;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class MainRRBinomialProjection extends MainRRBinomial {
  MainRRBinomialProjection(String[] args) {
    super(args);
  }

  public static void main(String[] args) {
    new MainRRBinomialProjection(args).run();
  }

  //   @Override
  //   double runTrial(int trial_num) {
  //     //    Stream<Profile> randprofiles =
  // profiles.parallelStream().map(this::randomizedResponse);
  //     //    Profile servProfile = aggregate(randprofiles);

  //     Profile servProfile = new Profile();
  //     long[] time_cost = new long[profiles.size()];

  //     try (CSVReader reader =
  //         new CSVReaderBuilder(
  //                 new FileReader(
  //                     String.format(
  //                         "csv/%s-%.2f-%d-%d-after-randomization.csv",
  //                         app, epsilon, (int) threshold, trial_num)))
  //             .withCSVParser(parser)
  //             .build()) {
  //       String[] nextLine;
  //       while ((nextLine = reader.readNext()) != null) {
  //         servProfile.put(nextLine[0].trim(), Long.parseLong(nextLine[1]));
  //       }
  //     } catch (IOException e) {
  //       throw new RuntimeException(e);
  //     }

  //     postProcessing(trial_num, servProfile);

  //     return Arrays.stream(time_cost).parallel().average().getAsDouble();
  //   }

  @Override
  Profile postProcessing(int trial_num, Profile servProfile) {
    Profile adjProfile = super.postProcessing(trial_num, servProfile);
    projectToSimplex(trial_num, adjProfile, realProfile.getTotalFreq());
    return null;
  }

  private static File NULL_FILE =
      new File((System.getProperty("os.name").startsWith("Windows") ? "NUL" : "/dev/null"));

  void projectToSimplex(int trial_num, Profile adjProfile, long sum) {
    // TODO: call octave or matlab
    String lePairCSVName = Paths.get("le-pairs-csv", app + ".csv").toAbsolutePath().toString();
    String cmd =
        String.format(
            "addpath(genpath('./common/src/main/matlab'));projectToSimplex('%s', '%s', '%s', %d);exit;",
            String.format(
                "%s/%s-%.2f-%d-%d-after-postprocessing.csv",
                csvDir, app, epsilon, (int) threshold, trial_num),
            String.format(
                "%s/%s-%.2f-%d-%d-after-qp.csv", csvDir, app, epsilon, (int) threshold, trial_num),
            lePairCSVName,
            sum);
    try {
      ProcessBuilder pb =
          new ProcessBuilder(
              "matlab", "-nodisplay", "-nosplash", "-nodesktop", "-nojvm", "-r", cmd);
      pb.inheritIO();
      pb.redirectOutput(NULL_FILE);
      Process process = pb.start();
      process.waitFor();
    } catch (InterruptedException | IOException e) {
      throw new RuntimeException(e);
    }

    //    Map<String, Double> ret = Maps.newHashMap();
    //    try (CSVReader reader =
    //        new CSVReaderBuilder(
    //                new FileReader(
    //                    String.format(
    //                        "csv/%s-%.2f-%d-%d-after-qp.csv", app, epsilon, (int) threshold,
    // trial_num)))
    //            .withCSVParser(parser)
    //            .build()) {
    //      String[] nextLine;
    //      while ((nextLine = reader.readNext()) != null)
    //        ret.put(nextLine[0].trim(), Double.parseDouble(nextLine[1]));
    //    } catch (IOException e) {
    //      throw new RuntimeException(e);
    //    }
    //    return ret;
  }
}
