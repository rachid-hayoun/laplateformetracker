package com.exemple;

import include.Login;
import include.Crud;
import include.ShowPanel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class app {
    private static final Color DARK_BG = new Color(13, 17, 23);
    private static final Color CARD_BG = new Color(22, 27, 34);
    private static final Color PRIMARY_BLUE = new Color(33, 136, 255);
    private static final Color SECONDARY_BLUE = new Color(79, 172, 254);
    private static final Color SUCCESS_GREEN = new Color(46, 160, 67);
    private static final Color SUCCESS_GREEN_HOVER = new Color(56, 180, 74);
    private static final Color ACCENT_PURPLE = new Color(138, 43, 226);
    private static final Color ACCENT_PURPLE_HOVER = new Color(153, 50, 204);
    private static final Color ACCENT_ORANGE = new Color(255, 140, 0);
    private static final Color ACCENT_ORANGE_HOVER = new Color(255, 165, 0);
    private static final Color TEXT_WHITE = new Color(248, 248, 242);
    private static final Color TEXT_GRAY = new Color(139, 148, 158);
    private static final Color BORDER_COLOR = new Color(48, 54, 61);
    private static final Color HOVER_COLOR = new Color(58, 175, 255);
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Inscription / Connexion");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1400, 900);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(DARK_BG);
            
            JPanel headerPanel = createHeaderPanel();
            
            JPanel centerPanel = new JPanel(new GridBagLayout());
            centerPanel.setBackground(DARK_BG);
            centerPanel.setBorder(new EmptyBorder(30, 50, 50, 50));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(20, 20, 20, 20);
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            
            JPanel inscriptionCard = createInscriptionCard(frame);
            gbc.gridx = 0;
            gbc.gridy = 0;
            centerPanel.add(inscriptionCard, gbc);
            
            JPanel connexionCard = createConnexionCard(frame);
            gbc.gridx = 1;
            gbc.gridy = 0;
            centerPanel.add(connexionCard, gbc);
            
            mainPanel.add(headerPanel, BorderLayout.NORTH);
            mainPanel.add(centerPanel, BorderLayout.CENTER);
            
            frame.add(mainPanel);
            frame.setVisible(true);
        });
    }
    
    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_BG);
        headerPanel.setBorder(new EmptyBorder(30, 0, 30, 0));
        
        JLabel titleLabel = new JLabel("GESTION DES UTILISATEURS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_WHITE);
        
        JLabel subtitleLabel = new JLabel("Inscrivez-vous ou connectez-vous pour continuer", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_GRAY);
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(CARD_BG);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private static JPanel createInscriptionCard(JFrame parentFrame) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(30, 30, 30, 30)
        ));
        card.setPreferredSize(new Dimension(450, 550));
        card.setMinimumSize(new Dimension(450, 550));
        card.setMaximumSize(new Dimension(450, 550));
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(CARD_BG);
        JLabel titleLabel = new JLabel("📝 INSCRIPTION");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_BLUE);
        titlePanel.add(titleLabel);
        
        JPanel separator = new JPanel();
        separator.setBackground(PRIMARY_BLUE);
        separator.setPreferredSize(new Dimension(390, 2));
        separator.setMaximumSize(new Dimension(390, 2));
        
        JComboBox<String> cbGrade = createStyledComboBox(new String[]{"Étudiant", "Professeur"});
        JTextField tfFirstName = createStyledTextField("Prénom");
        JTextField tfLastName = createStyledTextField("Nom");
        JTextField tfAge = createStyledTextField("Âge");
        JPasswordField pfPassword = createStyledPasswordField("Mot de passe");
        
        JButton btnInscrire = createStyledButton("S'INSCRIRE", ACCENT_PURPLE, ACCENT_PURPLE_HOVER);
        btnInscrire.setPreferredSize(new Dimension(200, 35));
        btnInscrire.setMaximumSize(new Dimension(200, 35));
        
        card.add(titlePanel);
        card.add(Box.createVerticalStrut(5));
        card.add(separator);
        card.add(Box.createVerticalStrut(30));
        card.add(createFieldPanel("Grade:", cbGrade));
        card.add(Box.createVerticalStrut(20));
        card.add(createFieldPanel("Prénom:", tfFirstName));
        card.add(Box.createVerticalStrut(20));
        card.add(createFieldPanel("Nom:", tfLastName));
        card.add(Box.createVerticalStrut(20));
        card.add(createFieldPanel("Âge:", tfAge));
        card.add(Box.createVerticalStrut(20));
        card.add(createFieldPanel("Mot de passe:", pfPassword));
        card.add(Box.createVerticalStrut(30));
        
        // Panel pour centrer le bouton plus petit
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(CARD_BG);
        buttonPanel.add(btnInscrire);
        card.add(buttonPanel);
        
        card.add(Box.createVerticalGlue()); // Remplit l'espace restant
        
        // Action du bouton inscription
        btnInscrire.addActionListener(e -> {
            try {
                // Validation des champs
                if (tfFirstName.getText().trim().isEmpty() || 
                    tfLastName.getText().trim().isEmpty() || 
                    tfAge.getText().trim().isEmpty() || 
                    new String(pfPassword.getPassword()).trim().isEmpty()) {
                    showStyledMessage(parentFrame, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Validation de l'âge
                int age;
                try {
                    age = Integer.parseInt(tfAge.getText().trim());
                    if (age <= 0) {
                        showStyledMessage(parentFrame, "L'âge doit être un nombre positif.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    showStyledMessage(parentFrame, "L'âge doit être un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String grade = (String) cbGrade.getSelectedItem();
                Login login = new Login();
                boolean success = login.createAccount(
                    tfFirstName.getText().trim(),
                    tfLastName.getText().trim(),
                    new String(pfPassword.getPassword()),
                    grade,
                    age
                );
                if (success) {
                    showStyledMessage(parentFrame, "Inscription réussie ! Veuillez vous connecter.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    // Effacer les champs
                    tfFirstName.setText("");
                    tfLastName.setText("");
                    tfAge.setText("");
                    pfPassword.setText("");
                    cbGrade.setSelectedIndex(0);
                } else {
                    showStyledMessage(parentFrame, "Erreur lors de l'inscription.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showStyledMessage(parentFrame, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        return card;
    }
    
    private static JPanel createConnexionCard(JFrame parentFrame) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(30, 30, 30, 30)
        ));
        card.setPreferredSize(new Dimension(450, 550));
        card.setMinimumSize(new Dimension(450, 550));
        card.setMaximumSize(new Dimension(450, 550));
        
        // Titre avec icône
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(CARD_BG);
        JLabel titleLabel = new JLabel("🔐 CONNEXION");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(SECONDARY_BLUE);
        titlePanel.add(titleLabel);
        
        // Ligne de séparation
        JPanel separator = new JPanel();
        separator.setBackground(SECONDARY_BLUE);
        separator.setPreferredSize(new Dimension(390, 2));
        separator.setMaximumSize(new Dimension(390, 2));
        
        // Composants du formulaire
        JComboBox<String> cbGrade = createStyledComboBox(new String[]{"Étudiant", "Professeur"});
        JTextField tfFirstName = createStyledTextField("Prénom");
        JPasswordField pfPassword = createStyledPasswordField("Mot de passe");
        
        // Bouton connexion maintenant orange avec texte blanc - plus petit
        JButton btnConnexion = createStyledButton("SE CONNECTER", ACCENT_ORANGE, ACCENT_ORANGE_HOVER);
        btnConnexion.setPreferredSize(new Dimension(200, 35));
        btnConnexion.setMaximumSize(new Dimension(200, 35));
        
        // Ajout des composants avec espacement uniforme
        card.add(titlePanel);
        card.add(Box.createVerticalStrut(5));
        card.add(separator);
        card.add(Box.createVerticalStrut(30));
        card.add(createFieldPanel("Grade:", cbGrade));
        card.add(Box.createVerticalStrut(20));
        card.add(createFieldPanel("Prénom:", tfFirstName));
        card.add(Box.createVerticalStrut(20));
        card.add(createFieldPanel("Mot de passe:", pfPassword));
        card.add(Box.createVerticalStrut(30));
        
        // Panel pour centrer le bouton plus petit
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(CARD_BG);
        buttonPanel.add(btnConnexion);
        card.add(buttonPanel);
        
        card.add(Box.createVerticalGlue()); // Remplit l'espace restant
        
        // Action du bouton connexion
        btnConnexion.addActionListener(e -> {
            try {
                String grade = (String) cbGrade.getSelectedItem();
                Login login = new Login();
                boolean auth = login.authenticate(
                    tfFirstName.getText(),
                    new String(pfPassword.getPassword()),
                    grade
                );
                if (auth) {
                    showStyledMessage(parentFrame, "Connexion réussie !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    parentFrame.dispose();
                    
                    // Redirection selon le grade
                    if (grade.equals("Étudiant")) {
                        openStudentListFrame();
                    } else {
                        openCrudFrame();
                    }
                } else {
                    showStyledMessage(parentFrame, "Identifiants incorrects ou grade non correspondant.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showStyledMessage(parentFrame, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        return card;
    }
    
    private static JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_BG);
        panel.setMaximumSize(new Dimension(390, 60));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(TEXT_GRAY);
        label.setBorder(new EmptyBorder(0, 0, 5, 0));
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        
        return panel;
    }
    
    private static JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(DARK_BG);
        field.setForeground(TEXT_WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        field.setCaretColor(PRIMARY_BLUE);
        field.setPreferredSize(new Dimension(390, 40));
        
        // Effet de focus
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_BLUE, 2),
                    new EmptyBorder(7, 11, 7, 11)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    new EmptyBorder(8, 12, 8, 12)
                ));
            }
        });
        
        return field;
    }
    
    private static JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(DARK_BG);
        field.setForeground(TEXT_WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        field.setCaretColor(PRIMARY_BLUE);
        field.setPreferredSize(new Dimension(390, 40));
        
        // Effet de focus
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_BLUE, 2),
                    new EmptyBorder(7, 11, 7, 11)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    new EmptyBorder(8, 12, 8, 12)
                ));
            }
        });
        
        return field;
    }
    
    private static JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(DARK_BG);
        comboBox.setForeground(TEXT_WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        comboBox.setPreferredSize(new Dimension(390, 40));
        
        return comboBox;
    }
    
    private static JButton createStyledButton(String text, Color color, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setBorder(new EmptyBorder(12, 20, 12, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(390, 45));
        button.setMaximumSize(new Dimension(390, 45));
        
        // Effet de hover
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private static void showStyledMessage(JFrame parent, String message, String title, int type) {
        JOptionPane.showMessageDialog(parent, message, title, type);
    }
    
    private static void openStudentListFrame() {
        SwingUtilities.invokeLater(() -> {
            JFrame studentFrame = new JFrame("Liste des Étudiants");
            studentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            studentFrame.setSize(800, 600);
            studentFrame.setLocationRelativeTo(null);
            
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(DARK_BG);
            
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(CARD_BG);
            headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            JLabel titleLabel = new JLabel("📚 LISTE DES ÉTUDIANTS");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            titleLabel.setForeground(PRIMARY_BLUE);
            headerPanel.add(titleLabel, BorderLayout.WEST);
            
            ShowPanel showPanel = new ShowPanel();
            showPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            mainPanel.add(headerPanel, BorderLayout.NORTH);
            mainPanel.add(showPanel, BorderLayout.CENTER);
            
            studentFrame.add(mainPanel);
            studentFrame.setVisible(true);
        });
    }
    
    private static void openCrudFrame() {
        SwingUtilities.invokeLater(() -> {
            JFrame crudFrame = new JFrame("Gestion des Utilisateurs");
            crudFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            crudFrame.setSize(900, 700);
            crudFrame.setLocationRelativeTo(null);
            
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(DARK_BG);
            
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(CARD_BG);
            headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            JLabel titleLabel = new JLabel("⚙️ GESTION DES UTILISATEURS");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            titleLabel.setForeground(SECONDARY_BLUE);
            headerPanel.add(titleLabel, BorderLayout.WEST);
            
            mainPanel.add(headerPanel, BorderLayout.NORTH);
            
            Crud crudPanel = new Crud();
            crudPanel.setBackground(DARK_BG);
            mainPanel.add(crudPanel, BorderLayout.CENTER);
            
            crudFrame.add(mainPanel);
            crudFrame.setVisible(true);
        });
    }
}