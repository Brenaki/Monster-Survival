package game;

import javax.swing.JFrame;

import config.ui.Window;

/**
 * 
 * @author bnk
 */
public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Monster Survival");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new Window());
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
