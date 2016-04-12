/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverprogram;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author jonathan.guerne
 */
public class ModelStatique extends AbstractTableModel {

    String[] entetes = {"ID", "Nom", "IP"};
    Object[][] donnees;

    public ModelStatique() {
        donnees = new Object[][]{
            {"0", "Sykes", "192"},
            {"1", "Van de Kampf", "192"},
            {"2", "Cuthbert", "192"},
            {"3", "Valance", "192"},
            {"4", "Schrödinger", "192"},
            {"5", "Duke", "192"},
            {"6", "Trump", "192"}};
    }

    @Override
    public int getRowCount() {
       return donnees.length;
    }

    @Override
    public int getColumnCount() {
        return entetes.length;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        return donnees[i][i1];
    }

}
