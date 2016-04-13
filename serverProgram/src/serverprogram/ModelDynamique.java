/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverprogram;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author jonathan.guerne
 */
public class ModelDynamique extends AbstractTableModel {

    private final String[] entetes = {"ID", "Nom", "IP", "Ban_ID", "Ban_IP"};
    private final ArrayList<Users> list_users = new ArrayList<Users>();

    public ModelDynamique() {
        super();
    }

    @Override
    public int getColumnCount() {
        return entetes.length;
    }

    @Override
    public int getRowCount() {
        return list_users.size();
    }

    @Override
    public String getColumnName(int i) {
        return entetes[i];
    }

    @Override
    public Object getValueAt(int rowindex, int columIndex) {
        switch (columIndex) {
            case 0:
                return list_users.get(rowindex).getID();
            case 1:
                return list_users.get(rowindex).getName();
            case 2:
                return list_users.get(rowindex).getAdresseIP();
            case 3:
                return list_users.get(rowindex).isBan_id();
            case 4:
                return list_users.get(rowindex).isBan_ip();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object o, int rowindex, int columIndex) {
        switch (columIndex) {
            case 0:
                list_users.get(rowindex).setID((int)o);
                break;
            case 1:
                list_users.get(rowindex).setName((String)o);
                break;
            case 2:
                list_users.get(rowindex).setAdresseIP((String)o);
                break;
            case 3:
                list_users.get(rowindex).setBan_id((boolean)o);
                break;
            case 4:
                list_users.get(rowindex).setBan_ip((boolean)o);
                break;
        }
        fireTableRowsUpdated(rowindex, rowindex);
    }

    public void addUser(Users u) {
        list_users.add(u);
        fireTableRowsInserted(list_users.size() - 1, list_users.size() - 1);
    }

    public void removeUser(int rowIndex) {
        list_users.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}
