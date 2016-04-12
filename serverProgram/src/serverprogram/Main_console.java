/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverprogram;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author jonathan.guerne
 */
public class Main_console extends JFrame {

    //http://baptiste-wicht.developpez.com/tutoriels/java/swing/jtable/
    public Main_console() {
        super();

        setTitle("JTable basique dans un JPanel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JTable tableau = new JTable(new ModelStatique());

        getContentPane().add(new JScrollPane(tableau), BorderLayout.CENTER);

        pack();
    }   
    
    public static void main(String[] args) {
        new Main_console().setVisible(true);
    }

}

