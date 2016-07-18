package org.leibnizcenter.rechtspraak.tagging;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

/**
 * Created by maarten on 29-3-16.
 */
public class MyJTable extends JTable {
    public TableCellRenderer getCellRenderer(int row, int column) {
//        if (column == Annotator.MyTableModel.COLUMN_TEXT) {
//            String txt = getModel().getValueAt(row, column).toString();
//            return new CellArea(txt, this, row, column, isCellSelected(row, column));
//        } else {
        return super.getCellRenderer(row, column);
//        }
    }
}
