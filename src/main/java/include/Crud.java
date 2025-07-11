package include;

import com.exemple.Db;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Crud extends JPanel {

    private JComboBox<String> roleCombo;
    private JTextField txtNom, txtPrenom, txtClasse, txtAge, txtId;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnActualiser;
    private JTextField txtRecherche;

    public Crud() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.4);
        splitPane.setDividerLocation(400);

        JPanel formPanel = createFormPanel();
        splitPane.setLeftComponent(formPanel);

        JPanel listPanel = createListPanel();
        splitPane.setRightComponent(listPanel);

        add(splitPane, BorderLayout.CENTER);

        loadTableData();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(30, 136, 229), 2),
            "Gestion des Utilisateurs",
            0, 0, new Font("Segoe UI", Font.BOLD, 16),
            new Color(30, 136, 229)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        txtId = new JTextField(20);
        txtId.setVisible(false);
        
        roleCombo = new JComboBox<>(new String[]{"Étudiant", "Professeur"});
        roleCombo.setPreferredSize(new Dimension(200, 30));
        styleComboBox(roleCombo);
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createLabel("Rôle :"), gbc);
        gbc.gridx = 1;
        panel.add(roleCombo, gbc);

        txtPrenom = addField(panel, gbc, 1, "Prénom :");
        txtNom = addField(panel, gbc, 2, "Nom :");
        txtClasse = addField(panel, gbc, 3, "Classe/Matière :");
        txtAge = addField(panel, gbc, 4, "Âge :");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(245, 245, 245));
        
        btnAjouter = new JButton("AJOUTER");
        btnModifier = new JButton("MODIFIER");
        btnSupprimer = new JButton("SUPPRIMER");
        btnActualiser = new JButton("ACTUALISER");

        styleButton(btnAjouter, new Color(46, 204, 113)); 
        styleButton(btnModifier, new Color(241, 196, 15)); 
        styleButton(btnSupprimer, new Color(231, 76, 60));
        styleButton(btnActualiser, new Color(30, 136, 229));
        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);
        buttonPanel.add(btnActualiser);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        btnAjouter.addActionListener(e -> ajouterUtilisateur());
        btnModifier.addActionListener(e -> modifierUtilisateur());
        btnSupprimer.addActionListener(e -> supprimerUtilisateur());
        btnActualiser.addActionListener(e -> {
            loadTableData();
            clearFields();
        });

        return panel;
    }

    private JPanel createListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(30, 136, 229), 2),
            "Liste des Utilisateurs",
            0, 0, new Font("Segoe UI", Font.BOLD, 16),
            new Color(30, 136, 229)
        ));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(245, 245, 245));
        searchPanel.add(new JLabel("Rechercher:"));
        txtRecherche = new JTextField(20);
        styleTextField(txtRecherche);
        searchPanel.add(txtRecherche);
        
        JButton btnRechercher = new JButton("Rechercher");
        styleButton(btnRechercher, new Color(30, 136, 229));
        searchPanel.add(btnRechercher);

        String[] columns = {"ID", "Type", "Prénom", "Nom", "Classe/Matière", "Âge"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(30, 136, 229));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(25);
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    loadSelectedRowData(selectedRow);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        btnRechercher.addActionListener(e -> rechercherUtilisateur());
        txtRecherche.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                if (txtRecherche.getText().trim().isEmpty()) {
                    loadTableData();
                }
            }
        });

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.DARK_GRAY);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return label;
    }

    private JTextField addField(JPanel panel, GridBagConstraints gbc, int row, String label) {
        JLabel jLabel = createLabel(label);
        JTextField textField = new JTextField(20);
        styleTextField(textField);
        
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(jLabel, gbc);
        gbc.gridx = 1;
        panel.add(textField, gbc);
        return textField;
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 35));
        
        // Effet hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(color.darker());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(color);
            }
        });
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        
        try (Connection con = Db.getConnection()) {
            // Charger les étudiants
            String sqlStudents = "SELECT id, first_name, last_name, grade, age FROM student";
            PreparedStatement pstStudents = con.prepareStatement(sqlStudents);
            ResultSet rsStudents = pstStudents.executeQuery();
            
            while (rsStudents.next()) {
                tableModel.addRow(new Object[]{
                    rsStudents.getInt("id"),
                    "Étudiant",
                    rsStudents.getString("first_name"),
                    rsStudents.getString("last_name"),
                    rsStudents.getString("grade"),
                    rsStudents.getInt("age")
                });
            }
            
            // Charger les professeurs
            String sqlTeachers = "SELECT id, first_name, last_name, grade, age FROM teacher";
            PreparedStatement pstTeachers = con.prepareStatement(sqlTeachers);
            ResultSet rsTeachers = pstTeachers.executeQuery();
            
            while (rsTeachers.next()) {
                tableModel.addRow(new Object[]{
                    rsTeachers.getInt("id"),
                    "Professeur",
                    rsTeachers.getString("first_name"),
                    rsTeachers.getString("last_name"),
                    rsTeachers.getString("grade"),
                    rsTeachers.getInt("age")
                });
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des données: " + e.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSelectedRowData(int selectedRow) {
        txtId.setText(tableModel.getValueAt(selectedRow, 0).toString());
        roleCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 1));
        txtPrenom.setText(tableModel.getValueAt(selectedRow, 2).toString());
        txtNom.setText(tableModel.getValueAt(selectedRow, 3).toString());
        txtClasse.setText(tableModel.getValueAt(selectedRow, 4).toString());
        txtAge.setText(tableModel.getValueAt(selectedRow, 5).toString());
    }

    private void ajouterUtilisateur() {
        if (!validateFields()) return;

        String nom = txtNom.getText().trim();
        String prenom = txtPrenom.getText().trim();
        String classe = txtClasse.getText().trim();
        int age = Integer.parseInt(txtAge.getText().trim());
        String role = (String) roleCombo.getSelectedItem();

        String sql;
        if (role.equals("Étudiant")) {
            sql = "INSERT INTO student (first_name, last_name, grade, age) VALUES (?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO teacher (first_name, last_name, grade, age) VALUES (?, ?, ?, ?)";
        }

        try (Connection con = Db.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, prenom);
            pst.setString(2, nom);
            pst.setString(3, classe);
            pst.setInt(4, age);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, role + " ajouté avec succès !", 
                "Succès", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadTableData();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout : " + ex.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierUtilisateur() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur à modifier.", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!validateFields()) return;

        String nom = txtNom.getText().trim();
        String prenom = txtPrenom.getText().trim();
        String classe = txtClasse.getText().trim();
        int age = Integer.parseInt(txtAge.getText().trim());
        String role = (String) roleCombo.getSelectedItem();
        int id = Integer.parseInt(txtId.getText());

        String sql;
        if (role.equals("Étudiant")) {
            sql = "UPDATE student SET first_name = ?, last_name = ?, grade = ?, age = ? WHERE id = ?";
        } else {
            sql = "UPDATE teacher SET first_name = ?, last_name = ?, grade = ?, age = ? WHERE id = ?";
        }

        try (Connection con = Db.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, prenom);
            pst.setString(2, nom);
            pst.setString(3, classe);
            pst.setInt(4, age);
            pst.setInt(5, id);
            
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, role + " modifié avec succès !", 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Aucune modification effectuée.", 
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification : " + ex.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerUtilisateur() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur à supprimer.", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Êtes-vous sûr de vouloir supprimer cet utilisateur ?", 
            "Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) return;

        String role = (String) roleCombo.getSelectedItem();
        int id = Integer.parseInt(txtId.getText());

        String sql;
        if (role.equals("Étudiant")) {
            sql = "DELETE FROM student WHERE id = ?";
        } else {
            sql = "DELETE FROM teacher WHERE id = ?";
        }

        try (Connection con = Db.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            int rowsDeleted = pst.executeUpdate();
            
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, role + " supprimé avec succès !", 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Aucune suppression effectuée.", 
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression : " + ex.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rechercherUtilisateur() {
        String recherche = txtRecherche.getText().trim();
        if (recherche.isEmpty()) {
            loadTableData();
            return;
        }

        tableModel.setRowCount(0);
        
        try (Connection con = Db.getConnection()) {
            String sqlStudents = "SELECT id, first_name, last_name, grade, age FROM student " +
                               "WHERE first_name ILIKE ? OR last_name ILIKE ? OR grade ILIKE ?";
            PreparedStatement pstStudents = con.prepareStatement(sqlStudents);
            pstStudents.setString(1, "%" + recherche + "%");
            pstStudents.setString(2, "%" + recherche + "%");
            pstStudents.setString(3, "%" + recherche + "%");
            ResultSet rsStudents = pstStudents.executeQuery();
            
            while (rsStudents.next()) {
                tableModel.addRow(new Object[]{
                    rsStudents.getInt("id"),
                    "Étudiant",
                    rsStudents.getString("first_name"),
                    rsStudents.getString("last_name"),
                    rsStudents.getString("grade"),
                    rsStudents.getInt("age")
                });
            }
            String sqlTeachers = "SELECT id, first_name, last_name, grade, age FROM teacher " +
                               "WHERE first_name ILIKE ? OR last_name ILIKE ? OR grade ILIKE ?";
            PreparedStatement pstTeachers = con.prepareStatement(sqlTeachers);
            pstTeachers.setString(1, "%" + recherche + "%");
            pstTeachers.setString(2, "%" + recherche + "%");
            pstTeachers.setString(3, "%" + recherche + "%");
            ResultSet rsTeachers = pstTeachers.executeQuery();
            
            while (rsTeachers.next()) {
                tableModel.addRow(new Object[]{
                    rsTeachers.getInt("id"),
                    "Professeur",
                    rsTeachers.getString("first_name"),
                    rsTeachers.getString("last_name"),
                    rsTeachers.getString("grade"),
                    rsTeachers.getInt("age")
                });
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la recherche: " + e.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateFields() {
        if (txtNom.getText().trim().isEmpty() || txtPrenom.getText().trim().isEmpty() || 
            txtClasse.getText().trim().isEmpty() || txtAge.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            int age = Integer.parseInt(txtAge.getText().trim());
            if (age <= 0) {
                JOptionPane.showMessageDialog(this, "L'âge doit être un nombre positif.", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "L'âge doit être un nombre valide.", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void clearFields() {
        txtId.setText("");
        txtNom.setText("");
        txtPrenom.setText("");
        txtClasse.setText("");
        txtAge.setText("");
        roleCombo.setSelectedIndex(0);
        table.clearSelection();
    }
}