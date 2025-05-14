package Class;

import GUI.reservationTerrainFoot;

import javax.swing.*;

public class testdeterrain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Système de Réservation - Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 700);
            frame.setLocationRelativeTo(null);

            reservationTerrainFoot reservationPanel = new reservationTerrainFoot();
            frame.setContentPane(reservationPanel);

            frame.setVisible(true);
        });
    }
}