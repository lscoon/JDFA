
import java.util.Iterator;
import java.util.Map;

import soot.Local;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.FlowSet;

public class MySceneTransformer extends SceneTransformer{

	@Override
	protected void internalTransform(String arg0, Map<String, String> arg1) {
		// TODO Auto-generated method stub

		/*
		SootMethod sMethod = Scene.v().getMainMethod();

		// Create graph based on the method
		UnitGraph graph = new BriefUnitGraph(sMethod.getActiveBody());

		// Perform LV Analysis on the Graph
		LiveVariableAnalysis analysis = new LiveVariableAnalysis(graph);

		// Print live variables at the entry and exit of each node
		
		Iterator<Unit> unitIt = graph.iterator();

		while (unitIt.hasNext()) {
			Unit s = unitIt.next();

			System.out.print(s);
			
			int d = 40 - s.toString().length();
			while (d > 0) {
				System.out.print(".");
				d--;
			}
			
			FlowSet<Local> set = analysis.getFlowBefore(s);

			System.out.print("\t[entry: ");
			for (Local local: set) {
				System.out.print(local+" ");
			}

			set = analysis.getFlowAfter(s);
			
			System.out.print("]\t[exit: ");
			for (Local local: set) {
				System.out.print(local+" ");
			}
			System.out.println("]");
		}*/
	}

}
