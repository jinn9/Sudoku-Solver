import java.util.ArrayList;
import java.util.Iterator;

public class Sudoku {

    private static final int DIM = 9; // dimension of the board
    private static ArrayList<Variable> variables = new ArrayList<>();
    private static ArrayList<Constraint> constraints = new ArrayList<>();

    Sudoku(int[][] initialBoard) {
        initialize(initialBoard);
    }

    // initialize variables and constraints
    private void initialize(int[][] initialBoard) {

        // initialize variables
        int value;
        int domain[] = {1,2,3,4,5,6,7,8,9};
        Variable var;

        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                value = initialBoard[i][j];
                var = new Variable(value, domain, "variable_" + (i+1) + (j+1));
                variables.add(var);
            }
        }

        // element i represents scope for i_th row constraint
        ArrayList<Variable> rowConstraintScopes[] = new ArrayList[DIM];
        // element i represents scope for i_th column constraint
        ArrayList<Variable> colConstraintScopes[] = new ArrayList[DIM];
        // element i represents scope for i_th sub-square constraint
        ArrayList<Variable> subConstraintScopes[] = new ArrayList[DIM];

        // find scope for each constraint
        for (Variable v : variables) {
            // row index for v
            int i = Character.getNumericValue(v.getName().charAt(9)) - 1;
            // column index for v
            int j = Character.getNumericValue(v.getName().charAt(10)) - 1;

            // row constraint scope
            switch (i) {
                case 0:
                    if (rowConstraintScopes[0] == null) {
                        rowConstraintScopes[0] = new ArrayList<>();
                    }
                    rowConstraintScopes[0].add(v);
                    break;
                case 1:
                    if (rowConstraintScopes[1] == null) {
                        rowConstraintScopes[1] = new ArrayList<>();
                    }
                    rowConstraintScopes[1].add(v);
                    break;
                case 2:
                    if (rowConstraintScopes[2] == null) {
                        rowConstraintScopes[2] = new ArrayList<>();
                    }
                    rowConstraintScopes[2].add(v);
                    break;
                case 3:
                    if (rowConstraintScopes[3] == null) {
                        rowConstraintScopes[3] = new ArrayList<>();
                    }
                    rowConstraintScopes[3].add(v);
                    break;
                case 4:
                    if (rowConstraintScopes[4] == null) {
                        rowConstraintScopes[4] = new ArrayList<>();
                    }
                    rowConstraintScopes[4].add(v);
                    break;
                case 5:
                    if (rowConstraintScopes[5] == null) {
                        rowConstraintScopes[5] = new ArrayList<>();
                    }
                    rowConstraintScopes[5].add(v);
                    break;
                case 6:
                    if (rowConstraintScopes[6] == null) {
                        rowConstraintScopes[6] = new ArrayList<>();
                    }
                    rowConstraintScopes[6].add(v);
                    break;
                case 7:
                    if (rowConstraintScopes[7] == null) {
                        rowConstraintScopes[7] = new ArrayList<>();
                    }
                    rowConstraintScopes[7].add(v);
                    break;
                case 8:
                    if (rowConstraintScopes[8] == null) {
                        rowConstraintScopes[8] = new ArrayList<>();
                    }
                    rowConstraintScopes[8].add(v);
                    break;
            }

            // column constraint scope
            switch (j) {
                case 0:
                    if (colConstraintScopes[0] == null) {
                        colConstraintScopes[0] = new ArrayList<>();
                    }
                    colConstraintScopes[0].add(v);
                    break;
                case 1:
                    if (colConstraintScopes[1] == null) {
                        colConstraintScopes[1] = new ArrayList<>();
                    }
                    colConstraintScopes[1].add(v);
                    break;
                case 2:
                    if (colConstraintScopes[2] == null) {
                        colConstraintScopes[2] = new ArrayList<>();
                    }
                    colConstraintScopes[2].add(v);
                    break;
                case 3:
                    if (colConstraintScopes[3] == null) {
                        colConstraintScopes[3] = new ArrayList<>();
                    }
                    colConstraintScopes[3].add(v);
                    break;
                case 4:
                    if (colConstraintScopes[4] == null) {
                        colConstraintScopes[4] = new ArrayList<>();
                    }
                    colConstraintScopes[4].add(v);
                    break;
                case 5:
                    if (colConstraintScopes[5] == null) {
                        colConstraintScopes[5] = new ArrayList<>();
                    }
                    colConstraintScopes[5].add(v);
                    break;
                case 6:
                    if (colConstraintScopes[6] == null) {
                        colConstraintScopes[6] = new ArrayList<>();
                    }
                    colConstraintScopes[6].add(v);
                    break;
                case 7:
                    if (colConstraintScopes[7] == null) {
                        colConstraintScopes[7] = new ArrayList<>();
                    }
                    colConstraintScopes[7].add(v);
                    break;
                case 8:
                    if (colConstraintScopes[8] == null) {
                        colConstraintScopes[8] = new ArrayList<>();
                    }
                    colConstraintScopes[8].add(v);
                    break;
            }

            // sub-square constraint scope
            if (i >= 0 && i <= 2) {
                if (j >= 0 && j <= 2) {
                    if (subConstraintScopes[0] == null) {
                        subConstraintScopes[0] = new ArrayList<>();
                    }
                    subConstraintScopes[0].add(v);
                } else if (j >= 3 && j <= 5) {
                    if (subConstraintScopes[1] == null) {
                        subConstraintScopes[1] = new ArrayList<>();
                    }
                    subConstraintScopes[1].add(v);
                } else {
                    if (subConstraintScopes[2] == null) {
                        subConstraintScopes[2] = new ArrayList<>();
                    }
                    subConstraintScopes[2].add(v);
                }

            } else if (i >= 3 && i <= 5) {
                if (j >= 0 && j <= 2) {
                    if (subConstraintScopes[3] == null) {
                        subConstraintScopes[3] = new ArrayList<>();
                    }
                    subConstraintScopes[3].add(v);
                } else if (j >= 3 && j <= 5) {
                    if (subConstraintScopes[4] == null) {
                        subConstraintScopes[4] = new ArrayList<>();
                    }
                    subConstraintScopes[4].add(v);
                } else {
                    if (subConstraintScopes[5] == null) {
                        subConstraintScopes[5] = new ArrayList<>();
                    }
                    subConstraintScopes[5].add(v);
                }

            } else {
                if (j >= 0 && j <= 2) {
                    if (subConstraintScopes[6] == null) {
                        subConstraintScopes[6] = new ArrayList<>();
                    }
                    subConstraintScopes[6].add(v);
                } else if (j >= 3 && j <= 5) {
                    if (subConstraintScopes[7] == null) {
                        subConstraintScopes[7] = new ArrayList<>();
                    }
                    subConstraintScopes[7].add(v);
                } else {
                    if (subConstraintScopes[8] == null) {
                        subConstraintScopes[8] = new ArrayList<>();
                    }
                    subConstraintScopes[8].add(v);
                }
            }
        }

        // add row constraints
        for (int i = 0; i < DIM; i++) {
            ArrayList<Variable> scope = rowConstraintScopes[i];
            constraints.add(new Constraint("row " + (i+1) + " constraint", scope));
        }

        // add column constraints
        for (int i = 0; i < DIM; i++) {
            ArrayList<Variable> scope = colConstraintScopes[i];
            constraints.add(new Constraint("column " + (i+1) + " constraint", scope));
        }

        // add sub-square constraints
        for (int i = 0; i < DIM; i++) {
            ArrayList<Variable> scope = subConstraintScopes[i];
            constraints.add(new Constraint("sub-square " + (i+1) + " constraint", scope));
        }
    }

    // returns a list of constraints whose scope contains var
    private ArrayList<Constraint> constraintsOf(Variable var) {
        ArrayList<Constraint> constraintsWithVar = new ArrayList<>();

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

        int row = 0;
        int col = 0;
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

    // implements GAC
    private void GAC(ArrayList<Variable> unAssignedVars) {
        if (unAssignedVars.isEmpty()) {
            // print the final board
            displaySoln();
            System.exit(0);
        }

        Variable var = unAssignedVars.remove(unAssignedVars.size() - 1);
        for (int val : var.getCurDomain()) {
            var.setValue(val);
            boolean noDWO = true; // a flag to check if domain is wiped out
            // check if "domain wipe out" happens
            if (GacEnforce(constraintsOf(var), var, val) == "DWO") {
                noDWO = false;
            }
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

    private String GacEnforce(ArrayList<Constraint> constraints, Variable assignedVar, int assignedVal) {
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
        ArrayList<Variable> unAssignedVars = new ArrayList<>();
        for (Variable v : variables) {
            if (v.getValue() == 0)
                unAssignedVars.add(v);
        }
        GAC(unAssignedVars);
    }
}