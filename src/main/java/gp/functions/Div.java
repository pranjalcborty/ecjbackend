package gp.functions;

import ec.EvolutionState;
import ec.Problem;
import ec.app.tutorial4.DoubleData;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class Div extends GPNode {

    public String toString() {
        return "/";
    }

    public int expectedChildren() {
        return 2;
    }

    public void eval(final EvolutionState state,
                     final int thread,
                     final GPData input,
                     final ADFStack stack,
                     final GPIndividual individual,
                     final Problem problem) {

        DoubleData rd = ((DoubleData) (input));

        children[1].eval(state, thread, input, stack, individual, problem);

        if (rd.x == 0) {
            rd.x = 1;
        } else {
            double result;
            result = rd.x;

            children[0].eval(state, thread, input, stack, individual, problem);
            rd.x = rd.x / result;
        }
    }
}

