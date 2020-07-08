package presto;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class MainRR extends BaseMain {

  double p;
  double z;

  MainRR(String[] args) {
    super(args);
  }

  @Override
  void init() {
    super.init();
    z = Math.exp(epsilon / (threshold * 2));
    p = z / (1 + z);
    System.out.println("probability\t" + p);
  }

  @Override
  double runTrial(int trial_num) {
    //    Stream<Profile> randprofiles = profiles.parallelStream().map(this::randomizedResponse);
    //    Profile servProfile = aggregate(randprofiles);

    Profile servProfile = new Profile();
    long[] time_cost = new long[profiles.size()];
    AtomicInteger idx = new AtomicInteger();
    profiles
        .parallelStream()
        .forEach(
            profile -> {
              time_cost[idx.getAndIncrement()] = timedRandomizedResponse(profile, servProfile);
            });

    dumpProfile(
        servProfile,
        String.format(
            "%s/%s-%.2f-%d-%d-after-randomization.csv",
            csvDir, app, epsilon, (int) threshold, trial_num));

    postProcessing(trial_num, servProfile);

    return Arrays.stream(time_cost).parallel().average().getAsDouble();
  }

  Profile postProcessing(int trial_num, Profile servProfile) {
    Profile adjProfile = new Profile();

    long totalFreq = realProfile.getTotalFreq();
    //    System.out.println("total frequency of servProfile: " + totalFreq);
    if (totalFreq != 5 * funcs.size() * 1000) {
      throw new RuntimeException("BAD in PostProcessing!!!!!!!!!");
    }

    for (String mmm : funcs) {
      double x = servProfile.get(mmm);
      long estimate = Math.round(((z + 1) * x - totalFreq) / (z - 1));
      //            System.out.println("serv: " + x + ", adj: " + estimate);
      // if (estimate < 0) estimate = 0;
      // if (estimate > totalFreq) estimate = totalFreq;
      adjProfile.add(mmm, estimate);
    }
    dumpProfile(
        adjProfile,
        String.format(
            "%s/%s-%.2f-%d-%d-after-postprocessing.csv",
            csvDir, app, epsilon, (int) threshold, trial_num));
    return adjProfile;
  }

  long timedRandomizedResponse(Profile profile, Profile servProfile) {
    Timer timer = new Timer();
    timer.reset();
    randomizedResponse(profile, servProfile);
    return timer.duration();
  }

  abstract void randomizedResponse(Profile profile, Profile servProfile);
}
