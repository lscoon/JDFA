
import java.io.File;
import java.util.Collections;
import java.util.HashMap;

import soot.PackManager;
import soot.PhaseOptions;
import soot.Scene;
import soot.SootClass;
import soot.Transform;
import soot.jimple.spark.SparkTransformer;
import soot.options.Options;

public class MyTry {
	
	private static String sep = File.separator;
	//private static String classPath = "turing" + sep + "";
	private static String apkPath = "test" + sep + "weka.jar";
	
	private static void init() {
		soot.G.reset();
		
		Options.v().set_prepend_classpath(true);
		Options.v().set_validate(true);
		Options.v().set_output_format(Options.output_format_jimple);
		Options.v().set_src_prec(Options.src_prec_class);
		
		Options.v().set_keep_line_number(true);
		Options.v().set_whole_program(true);
		
		Options.v().set_no_bodies_for_excluded(true);
		Options.v().set_app(true);
		
		Options.v().set_process_dir(Collections.singletonList(apkPath));

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
		
		init();
		enableSpark();
		PackManager.v().getPack("wjtp").add(
				new Transform("wjtp.myScene", new MySceneTransformer()));
		PackManager.v().getPack("jtp").add(
				new Transform("jtp.myBody", new MyBodyTransformer()));
        PackManager.v().runPacks();
        System.out.println(MyBodyTransformer.count);
        System.out.println(Scene.v().getEntryPoints());
        System.out.println(Scene.v().getReachableMethods().size());
        System.out.println(Scene.v().getCallGraph().size());
        //PackManager.v().writeOutput();
		
		//soot.Main.main(sootArgs);
	}
}
