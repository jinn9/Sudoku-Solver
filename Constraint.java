import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class Constraint {

    private String name;
    private ArrayList<Variable> scope;

    Constraint(String name, ArrayList<Variable> scope) {
        this.name = name;
        this.scope = scope;
    }

    public ArrayList<Variable> getScope() {
        return scope;
    }

    // returns the number of unassigned variables in this constraint's scope
    public int getNumUnassigned() {
        int count = 0;
        for (Variable v : scope) {
            if (!v.isAssigned()) {
                count += 1;
            }
        }
        return count;
    }

    public String getName() {
        return name;
    }

    // implements all-diff
    public boolean check() {
        if (getNumUnassigned() == 0) {
            ArrayList<Integer> assignments = new ArrayList<>();
            for (Variable v : scope) {
                if (assignments.contains(v.getValue()))
                    return false;
                assignments.add(v.getValue());
            }
            return true;
        }
        return true;
    }

    // checks if var=val has an extension to an assignment of the other variables in the constraint that satisfies the constraint
    public boolean hasSupport(Variable var, int val) {
        if (!getScope().contains(var))
            return true;

        // create of a copy of scope
        ArrayList<Variable> varsToAssign = new ArrayList<>();
        for (Variable  v : scope)
            varsToAssign.add(v);

        varsToAssign.remove(var);

        ArrayList<Map.Entry<Variable, Integer>> assignment = new ArrayList<>();
        assignment.add(new AbstractMap.SimpleEntry<>(var, val));
        //System.out.print("When " + assignment.get(0).getKey().getName() + " is set to " + assignment.get(0).getValue() + ", supporting tuple ");


        return findvals(varsToAssign, assignment);
    }

    private boolean valsNotEqual(ArrayList<Map.Entry<Variable, Integer>> assignments) {
        ArrayList<Integer> valsSeen = new ArrayList<>();
        for (Map.Entry<Variable, Integer> assignment : assignments) {
            if (valsSeen.contains(assignment.getValue()))
                return false;
            valsSeen.add(assignment.getValue());
        }
        return true;
    }

    private boolean findvals(ArrayList<Variable> remainingVars, ArrayList<Map.Entry<Variable, Integer>> assignments) {
        Collections.sort(remainingVars);
        return findvals_(remainingVars, assignments);
    }

    private boolean findvals_(ArrayList<Variable> remainingVars, ArrayList<Map.Entry<Variable, Integer>> assignments) {
        if (remainingVars.isEmpty())
            return valsNotEqual(assignments);
        Variable var = remainingVars.remove(remainingVars.size() - 1);
        for (int val : var.getCurDomain()) {
            assignments.add(new AbstractMap.SimpleEntry<>(var, val));
            if (valsNotEqual(assignments)) {
                if (findvals_(remainingVars, assignments))
                    return true;
            }
            assignments.remove(assignments.size() - 1);
        }
        remainingVars.add(var);
        return false;
    }
}
