import javax.swing.table.*;
import java.util.*;

public class ProcessTableModel extends AbstractTableModel {

    private String[] columnNames = {"PID", "Process name", "Burst"};
    private List<Process> processList;

    public ProcessTableModel(List<Process> pl) {
        super();
        processList = pl;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return processList.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        Process p = processList.get(row);
        switch(col) {
            case 0:
                return p.getName();
            case 1:
                return p.getId();
            case 2:
                return p.getBurst();
        }

        return null;
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }


}
