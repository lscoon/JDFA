
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import soot.PackManager;
import soot.PhaseOptions;
import soot.Scene;
import soot.SootClass;
import soot.Transform;
import soot.Unit;
import soot.jimple.spark.SparkTransformer;
import soot.options.Options;
import soot.toolkits.graph.UnitGraph;

public class MyTry {
	
	private static String sep = File.separator;
	private static String apkPath = "test";
	private static String apk1Path = apkPath + sep + "weka-3-8-3.jar";
	private static String apk2Path = apkPath + sep + "weka-3-9-3.jar";

	public static Map<String,UnitGraph> apk1 = new HashMap<>();
	public static Map<String,UnitGraph> apk2 = new HashMap<>();
	
	private static void init(String apk) {
		soot.G.reset();
		
		Options.v().set_prepend_classpath(true);
		Options.v().set_validate(true);
		Options.v().set_output_format(Options.output_format_jimple);
		Options.v().set_src_prec(Options.src_prec_class);
		
		Options.v().set_keep_line_number(true);
		Options.v().set_whole_program(true);
		
		Options.v().set_no_bodies_for_excluded(true);
		Options.v().set_app(true);

		Options.v().set_process_dir(Collections.singletonList(apk));

		Options.v().set_allow_phantom_refs(true);
		Options.v().set_allow_phantom_elms(true);
		
		//Options.v().set_soot_classpath(classPath);
		
		Scene.v().addBasicClass("java.io.PrintStream", SootClass.SIGNATURES);;
		Scene.v().addBasicClass("java.lang.System", SootClass.SIGNATURES);;
		Scene.v().addBasicClass("java.lang.Thread", SootClass.SIGNATURES);;
		Scene.v().loadNecessaryClasses();
	}
	
	private static void enableSpark() {
        HashMap<String,String> opt = new HashMap<String,String>();
        opt.put("propagator","worklist");
        opt.put("simple-edges-bidirectional","false");
        opt.put("on-fly-cg","true");
        opt.put("set-impl","double");
        opt.put("double-set-old","hybrid");
        opt.put("double-set-new","hybrid");
        opt.put("pre_jimplify", "true");
        SparkTransformer.v().transform("",opt);
        PhaseOptions.v().setPhaseOption("cg.spark", "enabled:true");
	}
	
	public static void main(String[] args) {
		
		enableSpark();
		
		init(apk1Path);
		PackManager.v().getPack("jtp").add(
				new Transform("jtp.myBody", new MyBodyTransformer()));
        PackManager.v().runBodyPacks();
        
        //System.out.println(Scene.v().getEntryPoints());
        
        init(apk2Path);
        PackManager.v().getPack("jtp").add(
				new Transform("jtp.myBodyT", new MyBodyTransformerT()));
        PackManager.v().runBodyPacks();
        
        System.out.println(computeDiff());
        //PackManager.v().getPack("wjtp").add(
      	//		new Transform("wjtp.myScene", new MySceneTransformer()));
      	//Scene.v().getCallGraph();
        //PackManager.v().writeOutput();
		//soot.Main.main(sootArgs);
	}
	
	private static float computeDiff() {
		int sum = 0;
		double sim = 0;
		for(String methodName : apk2.keySet()) {
			sum++;
			if(!apk1.containsKey(methodName))
				continue;
			
			UnitGraph ug1 = apk1.get(methodName);
			UnitGraph ug2 = apk2.get(methodName);
			Iterator<Unit> unitIt1 = ug1.iterator();
			Iterator<Unit> unitIt2 = ug2.iterator();
			LiveVariableAnalysis lva1 = new LiveVariableAnalysis(ug1);
			LiveVariableAnalysis lva2 = new LiveVariableAnalysis(ug2);
			
			Map<Integer,Integer> map1 = new LinkedHashMap<>();
			while(unitIt1.hasNext()) {
				Unit u1 = unitIt1.next();
				int k = lva1.getFlowAfter(u1).size();
				if(!map1.containsKey(k))
					map1.put(k, 0);
				else map1.put(k,map1.get(k)+1);
			}
			unitIt1.remove();
			
			Map<Integer,Integer> map2 = new LinkedHashMap<>();
			while(unitIt2.hasNext()) {
				Unit u2 = unitIt2.next();
				int k = lva2.getFlowAfter(u2).size();
				if(!map2.containsKey(k))
					map2.put(k, 0);
				else map2.put(k,map2.get(k)+1);
			}
			unitIt2.remove();
			
			float unitMax = Math.max(ug1.size(), ug2.size());
			float unitCount = 0;
			for(int x : map1.keySet()) {
				if(!map2.containsKey(x))
					continue;
				unitCount += Math.min(map1.get(x), map2.get(x));
			}
 			sim = sim + unitCount/unitMax;
			if(sim % 1000 <1)
				System.out.println("");
		}
		return (float) (sim/sum);
	}
}
