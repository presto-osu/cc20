package presto;

import com.google.common.collect.Lists;
import soot.PackManager;
import soot.Transform;
import soot.options.Options;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {
  static final String TAG = Main.class.getSimpleName();

  public static void main(final String[] args) throws FileNotFoundException {
    Configs.apkPath = args[0];
    Configs.sdkPlatformsPath = args[1];
    String[] parts = Configs.apkPath.split("/");
    Configs.apkName = parts[parts.length - 1];


    BufferedReader reader = new BufferedReader(new FileReader("excluded_packages.txt"));
    reader.lines().forEach(p -> Configs.excludedPackages.add(p.trim()));
    //    Configs.excludedPackages.add("dummyMainClass");

    runJTP(Configs.apkPath, Configs.sdkPlatformsPath);
  }

  // jimple transformation pack
  static void runJTP(final String apkPath, final String platformDir) {
    settings(apkPath, platformDir);

    PackManager.v()
        .getPack("wjtp")
        .add(new Transform("wjtp.myInstrumenter", new MySceneTransformer()));

    final String[] sootArgs = {
      "-w",
      "-process-multiple-dex",
      "-p",
      "jb",
      "stabilize-local-names:true",
      "-keep-line-number",
      "-allow-phantom-refs",
    };

    soot.Main.main(sootArgs);
  }

  static void settings(final String apkPath, final String platformDir) {
    // prefer Android APK files// -src-prec apk
    Options.v().set_src_prec(Options.src_prec_apk);
    // output as APK, too//-f J
    Options.v().set_output_format(Options.output_format_none);
    //    Options.v().set_output_format(Options.output_format_jimple);
    // set Android platform jars and apk
    Options.v().set_android_jars(platformDir);
    Options.v().set_process_dir(Lists.newArrayList(apkPath));

    // load instrument helper class
    Options.v().set_prepend_classpath(true);
    //    Options.v().set_soot_classpath(Configs.runtimeClsDir);

    // sometimes using array to parse args does not work
    Options.v().set_process_multiple_dex(true);
    Options.v().set_whole_program(true);
    Options.v().set_allow_phantom_refs(true);
  }
}
