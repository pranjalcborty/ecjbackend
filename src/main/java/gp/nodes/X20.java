package gp.nodes;

import ec.EvolutionState;
import ec.Problem;
import ec.app.tutorial4.DoubleData;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import gp.problems.CommonProblem;

public class X20 extends GPNode {

    public String toString() {
        return "x20";
    }

    public int expectedChildren() {
        return 0;
    }

    @Override
    public void eval(final EvolutionState state,
                     final int thread,
                     final GPData input,
                     final ADFStack stack,
                     final GPIndividual individual,
                     final Problem problem) {

        DoubleData rd = ((DoubleData) (input));
        rd.x = ((CommonProblem) problem).currentX20;
    }
}
