package GUI;

import Model.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class reservationTerrainFoot extends JPanel {

    public reservationTerrainFoot() {
        try {
            initUI();
        } catch (Exception e) {
            e.printStackTrace();
            this.add(new JLabel("Erreur lors du chargement des créneaux : " + e.getMessage()));
        }
    }

    private void initUI() throws Exception {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Réservation des terrains de foot", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        this.add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        Model m = new Model();
        ArrayList<String> creneaux = m.getCreneauxFromDB();

        for (String creneauInfo : creneaux) {
            contentPanel.add(createCreneauCard(creneauInfo));
            contentPanel.add(Box.createVerticalStrut(10));
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private Set<String> getReservedHours(String selectedDate) throws SQLException, ClassNotFoundException {
        Model m = new Model();
        Vector<String> reservedHoursVector = m.heuredebut(LocalDate.parse(selectedDate));
        Set<String> reservedHoursSet = new HashSet<>();
        for (int i = 0; i < reservedHoursVector.size(); i += 2) {
            String reservedHour = reservedHoursVector.get(i);
            String hourOnly = reservedHour.split(":")[0];
            reservedHoursSet.add(hourOnly);
        }
        return reservedHoursSet;
    }

    private JPanel createCreneauCard(String info) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setBackground(new Color(245, 245, 245));
        card.setMaximumSize(new Dimension(700, 130));

        JPanel infoPanel = new JPanel(new GridLayout(5, 1));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] parts = info.split(",");

        if (parts.length == 4) {
            int id_terrain = Integer.parseInt(parts[0]);
            infoPanel.add(new JLabel("📅 Nom : " + parts[1]));
            infoPanel.add(new JLabel("🏁 Type : " + parts[2]));
            infoPanel.add(new JLabel("🏁 Disponibilité : " + parts[3]));

            JButton reserverBtn = new JButton("Réserver");
            reserverBtn.setBackground(new Color(40, 167, 69));
            reserverBtn.setForeground(Color.WHITE);

            reserverBtn.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    JPanel formPanel = new JPanel();
                    formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
                    formPanel.setBackground(new Color(255, 255, 255, 230));
                    formPanel.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                            BorderFactory.createEmptyBorder(30, 40, 30, 40)
                    ));

                    Font labelFont = new Font("SansSerif", Font.BOLD, 14);
                    Dimension fieldSize = new Dimension(350, 35);

                    JLabel dateLabel = new JLabel("📅 Date :");
                    dateLabel.setFont(labelFont);
                    formPanel.add(dateLabel);

                    JComboBox<String> dateComboBox = new JComboBox<>();
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate currentDate = LocalDate.now();
                    for (int i = 0; i <= 15; i++) {
                        LocalDate futureDate = currentDate.plusDays(i);
                        dateComboBox.addItem(futureDate.format(dateFormatter));
                    }
                    formPanel.add(dateComboBox);

                    formPanel.add(Box.createVerticalStrut(10));
                    JLabel startLabel = new JLabel("🕙 Heure de début :");
                    startLabel.setFont(labelFont);
                    formPanel.add(startLabel);

                    JComboBox<String> hourStart = new JComboBox<>();
                    JComboBox<String> minuteStart = new JComboBox<>();
                    JComboBox<String> hourEnd = new JComboBox<>();
                    JComboBox<String> minuteEnd = new JComboBox<>();

                    JPanel startTimePanel = new JPanel();
                    startTimePanel.setLayout(new BoxLayout(startTimePanel, BoxLayout.X_AXIS));
                    startTimePanel.setMaximumSize(fieldSize);
                    startTimePanel.setOpaque(false);
                    startTimePanel.add(hourStart);
                    startTimePanel.add(Box.createHorizontalStrut(10));
                    startTimePanel.add(minuteStart);
                    formPanel.add(startTimePanel);

                    formPanel.add(Box.createVerticalStrut(10));
                    JLabel endLabel = new JLabel("🕛 Heure de fin (2h après début) :");
                    endLabel.setFont(labelFont);
                    formPanel.add(endLabel);

                    JPanel endTimePanel = new JPanel();
                    endTimePanel.setLayout(new BoxLayout(endTimePanel, BoxLayout.X_AXIS));
                    endTimePanel.setMaximumSize(fieldSize);
                    endTimePanel.setOpaque(false);
                    endTimePanel.add(hourEnd);
                    endTimePanel.add(Box.createHorizontalStrut(10));
                    endTimePanel.add(minuteEnd);
                    formPanel.add(endTimePanel);

                    try {
                        String selectedDate = (String) dateComboBox.getSelectedItem();
                        Set<String> reservedHours = getReservedHours(selectedDate);

                        for (int h = 0; h <= 20; h++) {
                            String hourStr = String.format("%02d", h);
                            if (!reservedHours.contains(hourStr)) {
                                hourStart.addItem(hourStr);
                            }
                        }

                        for (int m = 0; m < 60; m += 5) {
                            minuteStart.addItem(String.format("%02d", m));
                        }

                        ActionListener updateEndTime = new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (hourStart.getSelectedItem() == null || minuteStart.getSelectedItem() == null) return;
                                int selectedHour = Integer.parseInt((String) hourStart.getSelectedItem());
                                int selectedMinute = Integer.parseInt((String) minuteStart.getSelectedItem());

                                if (selectedHour == 20 && selectedMinute != 0) {
                                    selectedMinute = 0;
                                    minuteStart.setSelectedItem("00");
                                }

                                LocalTime startTime = LocalTime.of(selectedHour, selectedMinute);
                                LocalTime endTime = startTime.plusHours(2);

                                hourEnd.removeAllItems();
                                minuteEnd.removeAllItems();

                                if (endTime.getHour() <= 22) {
                                    hourEnd.addItem(String.format("%02d", endTime.getHour()));
                                    minuteEnd.addItem(String.format("%02d", endTime.getMinute()));
                                }
                            }
                        };

                        hourStart.addActionListener(updateEndTime);
                        minuteStart.addActionListener(updateEndTime);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    formPanel.add(Box.createVerticalStrut(10));
                    JLabel firstNameLabel = new JLabel("👤 Prénom :");
                    JTextField firstNameField = new JTextField();
                    firstNameField.setMaximumSize(fieldSize);
                    formPanel.add(firstNameLabel);
                    formPanel.add(firstNameField);

                    formPanel.add(Box.createVerticalStrut(10));
                    JLabel lastNameLabel = new JLabel("👤 Nom :");
                    lastNameLabel.setFont(labelFont);
                    formPanel.add(lastNameLabel);
                    JTextField lastNameField = new JTextField();
                    lastNameField.setMaximumSize(fieldSize);
                    formPanel.add(lastNameField);

                    formPanel.add(Box.createVerticalStrut(20));
                    JButton submitBtn = new JButton("Réserver");
                    submitBtn.setBackground(new Color(33, 150, 243));
                    submitBtn.setForeground(Color.WHITE);
                    submitBtn.setFocusPainted(false);
                    submitBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
                    submitBtn.setMaximumSize(fieldSize);
                    formPanel.add(submitBtn);

                    reservationTerrainFoot.this.removeAll();
                    reservationTerrainFoot.this.setLayout(new GridBagLayout());
                    reservationTerrainFoot.this.add(formPanel);
                    reservationTerrainFoot.this.revalidate();
                    reservationTerrainFoot.this.repaint();
                }
            });

            card.add(infoPanel, BorderLayout.CENTER);
            card.add(reserverBtn, BorderLayout.EAST);
        }

        return card;
    }
}
