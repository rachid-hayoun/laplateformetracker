package include;

import com.exemple.Db;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ShowPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public ShowPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(13, 17, 23)); 

        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(33, 136, 255), 2), 
            "Liste des Étudiants",
            0, 0, new Font("Segoe UI", Font.BOLD, 16),
            new Color(33, 136, 255)
        ));

        String[] columns = {"ID", "Prénom", "Nom", "Classe", "Âge"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(33, 136, 255));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(new Color(22, 27, 34)); 
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(48, 54, 61), 1)); 
        add(scrollPane, BorderLayout.CENTER);

        loadStudentData();
    }

    private void loadStudentData() {
        tableModel.setRowCount(0);

        try (Connection con = Db.getConnection()) {
            String sql = "SELECT id, first_name, last_name, grade, age FROM student";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("grade"),
                    rs.getInt("age")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des étudiants: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getStudentDataAsString() throws SQLException {
        StringBuilder sb = new StringBuilder();
        try (Connection con = Db.getConnection()) {
            String sql = "SELECT id, first_name, last_name, grade, age FROM student";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            sb.append(String.format("%-5s %-15s %-15s %-10s %-5s\n", 
                "ID", "Prénom", "Nom", "Classe", "Âge"));
            sb.append("------------------------------------------------\n");

            while (rs.next()) {
                sb.append(String.format("%-5d %-15s %-15s %-10s %-5d\n",
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("grade"),
                    rs.getInt("age")));
            }
        }
        return sb.toString();
    }
}