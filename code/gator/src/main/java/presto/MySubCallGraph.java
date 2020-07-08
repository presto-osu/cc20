package presto;

import com.google.common.collect.Sets;
import org.apache.commons.math3.util.Pair;
import soot.Body;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;

import java.util.Set;

public class MySubCallGraph {
  static long numStmts = 0;

  Set<Pair<Stmt, SootMethod>> contextSensitiveEdges = Sets.newHashSet();

  public static MySubCallGraph from(SootMethod caller) {
    MySubCallGraph ret = new MySubCallGraph();
    Body body = caller.retrieveActiveBody();
    for (Unit unit : body.getUnits()) {
      Stmt curStmt = (Stmt) unit;
      numStmts += 1;
      if (!curStmt.containsInvokeExpr()) continue;
      for (SootMethod target : StaticUtils.dispatch(curStmt)) {
        ret.contextSensitiveEdges.add(new Pair<>(curStmt, target));
      }
    }
    return ret;
  }
}
