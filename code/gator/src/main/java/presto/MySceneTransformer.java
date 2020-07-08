package presto;

import com.google.common.collect.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.math3.util.Pair;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.Stmt;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MySceneTransformer extends SceneTransformer {
  final String TAG = MySceneTransformer.class.getSimpleName();

  public MySceneTransformer() {
    System.out.println("MySceneTransformer created.");
  }

  @Override
  protected void internalTransform(String phaseName, Map<String, String> options) {
    //    Set<SootMethod> methodsOutside = Sets.newConcurrentHashSet();
    //    Set<SootMethod> methodsInside = Sets.newConcurrentHashSet();
    Map<SootMethod, MySubCallGraph> mtd2cg = Maps.newHashMap();
    Set<SootMethod> appMethods = Sets.newHashSet();
    Multimap<SootMethod, Stmt> mtd2callsites = LinkedListMultimap.create();

    System.out.println("Start analysis...");
    long start = System.currentTimeMillis();
    for (Iterator<SootClass> clsIter = Scene.v().getApplicationClasses().snapshotIterator();
        clsIter.hasNext(); ) {
      SootClass klass = clsIter.next();
      Lists.newCopyOnWriteArrayList(klass.getMethods()).stream()
          .filter(SootMethod::isConcrete)
          .forEach(
              mtd -> {
                mtd2cg.put(mtd, MySubCallGraph.from(mtd));
                MySubCallGraph cg = mtd2cg.get(mtd);
                for (Pair<Stmt, SootMethod> e : cg.contextSensitiveEdges)
                  mtd2callsites.put(e.getSecond(), e.getFirst());
                if (StaticUtils.isApplicationClass(mtd.getDeclaringClass())) appMethods.add(mtd);
              });
    }

    Set<SootMethod> singleCallSiteMtds = Sets.newHashSet();
    mtd2callsites
        .asMap()
        .forEach(
            (mtd, callSites) -> {
              if (callSites.size() == 1) {
                singleCallSiteMtds.add(mtd);
              }
            });

    Logger.stat("#app_methods=" + appMethods.size());
    Logger.stat("#single_call_site_methods=" + singleCallSiteMtds.size());

    //    Set<Map<String, String>> allMtds = Sets.newHashSet();
    //    for (SootMethod m : appMethods) {
    //      String sig = m.getSignature().substring(1, m.getSignature().length() - 1);
    //      Map<String, String> map = Maps.newHashMap();
    //      map.put("sig", sig);
    //      allMtds.add(map);
    //    }
    //    dump("mtds/" + Configs.apkName + ".json", allMtds);

    // inequality: freq(S) >= freq(T)
    {
      Set<String> res = Sets.newHashSet();
      for (SootMethod caller : appMethods) {
        MySubCallGraph cg = mtd2cg.get(caller);
        for (Pair<Stmt, SootMethod> pair : cg.contextSensitiveEdges) {
          SootMethod callee = pair.getSecond();
          if (!callee.isConcrete()) continue;
          Stmt callSite = pair.getFirst();
          if (StaticUtils.overrideLibraryMethod(callee) || !singleCallSiteMtds.contains(callee))
            continue;
          boolean callSiteInLoop = StaticUtils.isCallSiteInLoop(caller, callSite);
          if (!callSiteInLoop) { // the call site is not in any loop
            res.add(
                caller.getSignature().substring(1, caller.getSignature().length() - 1)
                    + " >>> "
                    + callee.getSignature().substring(1, callee.getSignature().length() - 1));
          }
        }
      }
      System.out.println("[presto] #GE pairs: " + res.size());
      dump("cg-ge/" + Configs.apkName + ".json", res);
    }

    // inequality: freq(S) <= freq(T)
    {
      Set<String> res = Sets.newHashSet();
      for (SootMethod caller : appMethods) {
        if (!caller.isConcrete()) continue;
        MySubCallGraph cg = mtd2cg.get(caller);
        for (Pair<Stmt, SootMethod> pair : cg.contextSensitiveEdges) {
          SootMethod callee = pair.getSecond();
          if (!callee.isConcrete()) continue;
          Stmt callSite = pair.getFirst();
          boolean domExits = StaticUtils.dominatesExit(caller, callSite);
          if (domExits && StaticUtils.dispatch(callSite).size() == 1) {
            res.add(
                caller.getSignature().substring(1, caller.getSignature().length() - 1)
                    + " <<< "
                    + callee.getSignature().substring(1, callee.getSignature().length() - 1));
          }
        }
      }
      System.out.println("[presto] #LE pairs: " + res.size());
      dump("cg-le/" + Configs.apkName + ".json", res);
    }

    System.out.println("Time: " + (System.currentTimeMillis() - start) / 1000.0);
    System.out.println("#Stmts: " + MySubCallGraph.numStmts);
  }

  private Gson gson = new GsonBuilder().setPrettyPrinting().create();

  <T> void dump(String file, Set<T> res) {
    try {
      FileWriter writer = new FileWriter(file);
      writer.write(gson.toJson(res));
      writer.flush();
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
