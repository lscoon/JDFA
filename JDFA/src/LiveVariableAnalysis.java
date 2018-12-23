
import java.util.*;

import soot.Local;
import soot.Unit;
import soot.ValueBox;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ArraySparseSet;

class LiveVariableAnalysis extends BackwardFlowAnalysis<Unit, FlowSet<Local>> {
	
	private FlowSet<Local> emptySet;

	public LiveVariableAnalysis(DirectedGraph g) {
		// You can perform some initialization code here
        // Then we launch the analysis
		
		// First obligation
		super(g);
		
		// Create the emptyset
		emptySet = new ArraySparseSet<Local>();
		
		// Second obligation
		doAnalysis();

	}
	

	// This method performs the joining of successor nodes
	// Since live variables is a may analysis we join by union 
	@Override
	protected void merge(FlowSet<Local> inSet1, FlowSet<Local> inSet2, FlowSet<Local> outSet) {
		// Merges two source flow sets into a destination flow set
		inSet1.union(inSet2, outSet);
	}


	@Override
	protected void copy(FlowSet<Local> srcSet, FlowSet<Local> destSet) {
		// Copies src to dest
		srcSet.copy(destSet);
	}

	
	// Used to initialize the in and out sets for each node. In
	// our case we build up the sets as we go, so we initialize
	// with the empty set.
	@Override
	protected FlowSet<Local> newInitialFlow() {
		// Returns the flow set used when a new flow set is necessary
        // Not always the same as entryInitialFlow
		
		return emptySet.clone();
	}


	// Returns FlowSet representing the initial set of the entry
	// node. In our case the entry node is the last node and it
	// should contain the empty set.
	@Override
	protected FlowSet<Local> entryInitialFlow() {
		// Returns the initial flow set (the flow set used a the beginning
        // of the analysis)
		return emptySet.clone();
	}

	
	// Sets the outSet with the values that flow through the 
	// node from the inSet based on reads/writes at the node
	// Set the outSet (entry) based on the inSet (exit)
	@Override
	protected void flowThrough(FlowSet<Local> inSet, Unit node, FlowSet<Local> outSet) {
		// The core of the analysis: analyses a node, reading the previous flow
        // set from src, writing the new flow set into dest
        // You can print the unit analyzed and the resulting flow set
		
		// outSet is the set at enrty of the node
		// inSet is the set at exit of the node
		// out <- (in - write(node)) union read(node)
		
		// out <- (in - write(node))

		FlowSet writes = (FlowSet)emptySet.clone();

		for (ValueBox def: node.getUseAndDefBoxes()) {
			if (def.getValue() instanceof Local) {
				writes.add(def.getValue());
			}
		}
		inSet.difference(writes, outSet);

		// out <- out union read(node)

		for (ValueBox use: node.getUseBoxes()) {
			if (use.getValue() instanceof Local) {
				outSet.add((Local) use.getValue());
			}
		}
	}
}