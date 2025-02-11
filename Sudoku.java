import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Sudoku {

    private static final int DIM = 9;
    private static final List<Variable> variables = new ArrayList<>();
    private static final List<Constraint> constraints = new ArrayList<>();

    public Sudoku(int[][] initialBoard) {
        initialize(initialBoard);
    }
    /**
     * initialize variables and constraints
     */
    private void initialize(int[][] initialBoard) {
        initializeVariables(initialBoard);
        initializeConstraints();
    }

    private void initializeVariables(int[][] initialBoard) {
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                variables.add(new Variable(initialBoard[i][j], i, j));
            }
        }
    }

    private void initializeConstraints() {
        // element i represents scope for i_th row constraint
        List<List<Variable>> rowConstraintScopes = new ArrayList<>();

        // element i represents scope for i_th column constraint
        List<List<Variable>> colConstraintScopes = new ArrayList<>();

        // element i represents scope for i_th sub-square constraint
        List<List<Variable>> subConstraintScopes = new ArrayList<>();

        for (int i = 0; i < DIM; i++) {
            rowConstraintScopes.add(new ArrayList<>());
            colConstraintScopes.add(new ArrayList<>());
            subConstraintScopes.add(new ArrayList<>());
        }

        // find scope for each constraint
        for (Variable v : variables) {
            int i = v.getRow(), j = v.getCol();

            rowConstraintScopes.get(i).add(v);

            colConstraintScopes.get(j).add(v);

            if (i <= 2) {
                if (j <= 2) {
                    subConstraintScopes.get(0).add(v);
                } else if (j <= 5) {
                    subConstraintScopes.get(1).add(v);
                } else {
                    subConstraintScopes.get(2).add(v);
                }
            } else if (i <= 5) {
                if (j <= 2) {
                    subConstraintScopes.get(3).add(v);
                } else if (j <= 5) {
                    subConstraintScopes.get(4).add(v);
                } else {
                    subConstraintScopes.get(5).add(v);
                }
            } else {
                if (j <= 2) {
                    subConstraintScopes.get(6).add(v);
                } else if (j <= 5) {
                    subConstraintScopes.get(7).add(v);
                } else {
                    subConstraintScopes.get(8).add(v);
                }
            }
        }

        for (int i = 0; i < DIM; i++) {
            constraints.add(new Constraint("row " + (i+1) + " constraint", rowConstraintScopes.get(i)));
            constraints.add(new Constraint("column " + (i+1) + " constraint", colConstraintScopes.get(i)));
            constraints.add(new Constraint("sub-square " + (i+1) + " constraint", subConstraintScopes.get(i)));
        }
    }

    /**
     * return a list of constraints whose scope contains var
     */
    private List<Constraint> constraintsOf(Variable var) {
        List<Constraint> constraintsWithVar = new ArrayList<>();

        for (Constraint c : constraints) {
            if (c.getScope().contains(var)) {
                constraintsWithVar.add(c);
            }
        }
        return constraintsWithVar;
    }

    private void displaySoln() {
        // print the final board
        System.out.println("\n");
        System.out.println("The final output\n");

        int row = 0, col = 0;
        for (Variable v : variables) {
            System.out.print(v.getValue() + " ");
            if ((col + 1) % 3 == 0) {
                System.out.print(" ");
            }
            if ((col + 1) == DIM) {
                if ((row + 1) % 3 == 0) {
                    System.out.println();
                    System.out.println();
                } else {
                    System.out.println();
                }
                row += 1;
            }
            col = (col + 1) % DIM;
        }
    }

    // !! Don't use this method to solve the Sudoku as it is too slow
    private void BTSearch(ArrayList<Variable> unAssignedVars) {

        if (unAssignedVars.isEmpty()) {
            // print the final board
            displaySoln();
        }

        // select next variable to assign
        Variable var = unAssignedVars.remove(unAssignedVars.size() - 1);
        for (int val : var.getDomain()) {
            var.setValue(val);
            //System.out.println("variable " + var.getName() + " is set to " + var.getValue());

            boolean constraintsOK = true;
            for (Constraint constraint : constraintsOf(var)) {
                // check if there is a constraint that is violated by setting val to var
                if (constraint.getNumUnassigned() == 0) {
                    if (!constraint.check()) {
                        constraintsOK = false;
                        //System.out.println(var.getName() + " violates " + constraint.getName() + " when set to " + var.getValue());
                        break;
                    }
                }
            }
            if (constraintsOK) {
                //System.out.println("Variable " + var.getName() + " does not violate constraints when set to " + var.getValue());
                BTSearch(unAssignedVars);
            }
        }
        // when it reaches here, no assignment to var satisfy all the constraints
        var.setValue(0);    // undo assignment to var
        unAssignedVars.add(var);    // restore var to unAssignedVars
    }

    /**
     * implement GAC
     */
    private void GAC(List<Variable> unAssignedVars) {
        if (unAssignedVars.isEmpty()) {
            // print the final board
            displaySoln();
            System.exit(0);
        }

        Variable var = unAssignedVars.remove(unAssignedVars.size() - 1);
        for (int val : var.getCurDomain()) {
            var.setValue(val);
            // a flag to check if domain is wiped out
            boolean noDWO = !GacEnforce(constraintsOf(var), var, val).equals("DWO");
            // check if "domain wipe out" happens
            if (noDWO) {
                GAC(unAssignedVars);
            }
            // before moving on to the next value, restore all the values that were pruned when we assigned var to val
            var.restoreValues(var, val);
        }
        // when it reaches here, no assignment to var can satisfy all the constraints
        var.setValue(0);    // undo assignment to var
        unAssignedVars.add(var);    // restore var to unAssignedVars
    }

    private String GacEnforce(List<Constraint> constraints, Variable assignedVar, int assignedVal) {
        while (!constraints.isEmpty()) {
            Constraint constraint = constraints.remove(constraints.size() - 1);
            for (Variable var : constraint.getScope()) {
                Iterator<Integer> it = var.getCurDomain().iterator();
                while (it.hasNext()) {
                    int val = it.next();
                    if (!constraint.hasSupport(var, val)) {
                        // prune val from var's domain as val cannot be used to satisfy constraint
                        var.pruneValue(val, assignedVar, assignedVal, it);
                        if (var.getCurDomainSize() == 0) {
                            return "DWO";
                        }
                        for (Constraint recheck : constraintsOf(var)) {
                            if (!recheck.equals(constraint) && !constraints.contains(recheck)) {
                                constraints.add(recheck);
                            }
                        }
                    }
                }
            }
        }
        return "OK";
    }

    public void run() {
        List<Variable> unAssignedVars = new ArrayList<>();
        for (Variable v : variables) {
            if (v.getValue() == 0) {
                unAssignedVars.add(v);
            }
        }
        GAC(unAssignedVars);
    }
}