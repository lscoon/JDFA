
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.Local;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.FlowSet;

import soot.Body;
import soot.BodyTransformer;

public class MyBodyTransformerT extends BodyTransformer{

	public static int count = 0;
	@Override
	protected void internalTransform(Body arg0, String arg1, Map<String, String> arg2) {
		// TODO Auto-generated method stub
		String dcn = arg0.getMethod().getDeclaringClass().getName();
		String mn = arg0.getMethod().getName();
		if(dcn.startsWith("org")||dcn.startsWith("jdk"))
			return;
		
		//System.out.println(dcn + "+" + arg0.getMethod().getName());
		UnitGraph graph = new BriefUnitGraph(arg0);
		MyTry.apk2.put(dcn.concat(mn), graph);
		//System.out.println("2");
	}

}
