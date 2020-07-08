package presto;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.math3.util.Pair;
import soot.*;
import soot.jimple.*;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.MHGDominatorsFinder;
import soot.util.Chain;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StaticUtils {
  public static boolean dominatesExit(SootMethod src, Stmt stop) {
    Body body = src.retrieveActiveBody();
    ExceptionalUnitGraph cfg = new ExceptionalUnitGraph(body);
    Set<Stmt> visited = reachabilityAnalysis(cfg, stop);
    for (Unit t : cfg.getTails()) if (visited.contains(t)) return false;
    return true;
  }

  public static Set<Stmt> reachabilityAnalysis(ExceptionalUnitGraph cfg, Stmt stop) {
    Set<Stmt> visited = Sets.newHashSet();
    List<Unit> worklist = Lists.newLinkedList();
    worklist.addAll(cfg.getHeads());
    while (!worklist.isEmpty()) {
      Stmt cur = (Stmt) worklist.remove(0);
      if (visited.contains(cur)) continue;
      visited.add(cur);
      if (cur.equals(stop)) continue;
      worklist.addAll(cfg.getSuccsOf(cur));
    }
    return visited;
  }

  static boolean isApplicationClass(SootClass cls) {
    for (String p : Configs.excludedPackages) if (cls.getName().startsWith(p)) return false;
    return cls.isApplicationClass();
  }

  public static boolean overrideLibraryMethod(SootMethod mtd) {
    Set<SootClass> superClassesAndInterfaces = Sets.newHashSet();
    addSuper(mtd.getDeclaringClass(), superClassesAndInterfaces);
    for (SootClass cls : superClassesAndInterfaces) {
      if (isApplicationClass(cls)) continue;
      if (cls.declaresMethod(mtd.getSubSignature())) return true;
    }
    return !isApplicationClass(mtd.getDeclaringClass());
  }

  private static void addSuper(SootClass cls, Set<SootClass> superClassesAndInterfaces) {
    if (cls.hasSuperclass()) {
      SootClass sclass = cls.getSuperclass();
      superClassesAndInterfaces.add(sclass);
      addSuper(sclass, superClassesAndInterfaces);
    }
    Chain<SootClass> interfaces = cls.getInterfaces();
    for (SootClass sclass : interfaces) {
      superClassesAndInterfaces.add(sclass);
      addSuper(sclass, superClassesAndInterfaces);
    }
  }

  public static boolean isCallSiteInLoop(SootMethod mtd, Stmt callSite) {
    Body body = mtd.retrieveActiveBody();
    ExceptionalUnitGraph cfg = new ExceptionalUnitGraph(body);

    Map<Unit, Color> colors = Maps.newHashMap();
    for (Unit u : body.getUnits()) colors.put(u, Color.WHITE);
    Set<Pair<Unit, Unit>> retreatingEdges = Sets.newHashSet();
    for (Unit u : cfg.getHeads()) dfs(cfg, u, colors, retreatingEdges);

    MHGDominatorsFinder domFinder = new MHGDominatorsFinder(cfg);
    Set<Pair<Unit, Unit>> backEdges =
        retreatingEdges.stream()
            .filter(
                pair -> {
                  Unit t = pair.getFirst();
                  Unit h = pair.getSecond();
                  return domFinder.isDominatedBy(t, h);
                })
            .collect(Collectors.toSet());

    for (Pair<Unit, Unit> pair : backEdges) {
      Unit t = pair.getFirst();
      Unit h = pair.getSecond();
      Set<Unit> visited = Sets.newHashSet();
      visited.add(h);
      backwardDFS(cfg, t, visited);
      if (visited.contains(callSite)) return true;
    }
    return false;
  }

  enum Color {
    WHITE,
    GREY,
    BLACK
  }

  static void dfs(
      ExceptionalUnitGraph cfg,
      Unit n,
      Map<Unit, Color> colors,
      Set<Pair<Unit, Unit>> retreatingEdges) {
    colors.put(n, Color.GREY);
    for (Unit m : cfg.getSuccsOf(n))
      if (colors.get(m) == Color.WHITE) dfs(cfg, m, colors, retreatingEdges);
      else if (colors.get(m) == Color.GREY) retreatingEdges.add(new Pair<>(n, m));
      else if (colors.get(m) != Color.BLACK) throw new RuntimeException("Error in DFS.");
    colors.put(n, Color.BLACK);
  }

  static void backwardDFS(ExceptionalUnitGraph cfg, Unit n, Set<Unit> visited) {
    if (visited.contains(n)) return;
    visited.add(n);
    for (Unit p : cfg.getPredsOf(n)) backwardDFS(cfg, p, visited);
  }

  public static Set<SootMethod> dispatch(Stmt stmt) {
    if (!stmt.containsInvokeExpr())
      throw new RuntimeException("Statement must contain invoke expression.");

    Set<SootMethod> ret = Sets.newHashSet();
    InvokeExpr ie = stmt.getInvokeExpr();

    SootMethod callee;
    try {
      callee = ie.getMethod();
    } catch (SootMethodRefImpl.ClassResolutionFailedException ex) {
      Logger.warn("Failed to resolve " + stmt);
      return ret;
    }

    // non-virtual calls
    if (ie instanceof StaticInvokeExpr || ie instanceof SpecialInvokeExpr) {
      SootMethod target = callee;
      if (ie instanceof SpecialInvokeExpr)
        target =
            Scene.v()
                .getOrMakeFastHierarchy()
                .resolveSpecialDispatch((SpecialInvokeExpr) ie, callee);
      //      if (!isApplicationClass(target.getDeclaringClass())) return ret;
      ret.add(target);
      return ret;
    }

    // virtual calls: invokevirtual, invokedynamic, invokeinterface
    Local rcv_var = (Local) ((InstanceInvokeExpr) ie).getBase();
    Type rcv_t = rcv_var.getType();
    // could be ArrayType, for clone() calls
    if (!(rcv_t instanceof RefType)) {
      return ret;
    }
    SootClass stc = ((RefType) rcv_t).getSootClass();
    Set<SootMethod> targets;
    try {
      targets = Scene.v().getOrMakeFastHierarchy().resolveAbstractDispatch(stc, callee);
    } catch (Exception e) {
      return ret;
    }
    for (SootMethod t : targets) {
      //      if (!isApplicationClass(t.getDeclaringClass())) continue;
      ret.add(t);
    }
    return ret;
  }
}
