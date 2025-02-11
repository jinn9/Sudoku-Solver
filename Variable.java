import java.util.Map;
import java.util.HashMap;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.ArrayList;

// Class Variable
// Each Variable represents a cell in the board
public class Variable implements Comparable {
    //stores pruned values indexed by a (variable, value) pair
    public static Map<Map.Entry<Variable, Integer>, ArrayList<Map.Entry<Variable, Integer>>> undoDict = new HashMap<>();

    private int value;
    private int[] domain;
    private ArrayList<Integer> curDomain;
    private String name;
    private int row;
    private int col;

    public Variable(int value, int row, int col) {
        this.value = value;
        this.name = "variable_" + (row + 1) + (col + 1);
        this.domain = new int[] {1,2,3,4,5,6,7,8,9};
        this.row = row;
        this.col = col;

        // create a copy of domain
        curDomain = new ArrayList<>();
        for (int val : domain) {
            curDomain.add(val);
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int[] getDomain() {
        return domain;
    }

    public void setDomain(int[] domain) {
        this.domain = domain;
    }

    public ArrayList<Integer> getCurDomain() {
        // returns an assigned value if the variable is assigned
        if (isAssigned()) {
            ArrayList<Integer> curDomain = new ArrayList<>();
            curDomain.add(value);
            return curDomain;
        }
        return this.curDomain;
    }

    public void setCurDomain(ArrayList<Integer> curDomain) {
        this.curDomain = curDomain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void unAssign() {
        setValue(0);
    }

    // checks if this Variable is assigned to some value
    public boolean isAssigned() {
        return value > 0;
    }

    public int getCurDomainSize() {
        if (isAssigned()) {
            return 1;
        }
        return curDomain.size();
    }

    // checks if val is in current domain
    public boolean inCurDomain(int val) {
        if (isAssigned()) {
            return val == value;
        }
        return curDomain.contains(val);
    }

    // removes value from current domain
    public void pruneValue(int val, Variable reasonVar, int reasonVal, Iterator<Integer> it) {
        it.remove(); //remove val from its domain

        Map.Entry<Variable, Integer> key = new AbstractMap.SimpleEntry<>(reasonVar, reasonVal);
        Map.Entry<Variable, Integer> value = new AbstractMap.SimpleEntry<>(this, val);
        if (!undoDict.containsKey(key)) {
            undoDict.put(key, new ArrayList<>());
        }
        undoDict.get(key).add(value);
    }

    private void restoreVal(int val) {
        curDomain.add(val);
    }

    // restore all the values that were pruned when reasonVar was set to reasonVal
    public void restoreValues(Variable reasonVar, int reasonVal) {
        Map.Entry<Variable, Integer> key = new AbstractMap.SimpleEntry<>(reasonVar, reasonVal);
        for (Map.Entry<Variable, Integer> item : undoDict.get(key)) {
            Variable var = item.getKey();
            int val = item.getValue();

            var.restoreVal(val);
        }
        undoDict.remove(reasonVar);
    }

    @Override
    public int compareTo(Object otherVar) {
        int otherCurDomainSize = ((Variable)otherVar).getCurDomainSize();
        return getCurDomainSize() - otherCurDomainSize;
    }


}
