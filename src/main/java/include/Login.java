package include;

import com.exemple.Db;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends Db {
    public Login() throws SQLException {
        super();
    }

    public boolean createAccount(String firstName, String lastName, String password, String grade, int age) {
        try {
            String hashedPassword = PassWord.hashPassword(password);
            
            String sqlLogin = "INSERT INTO login (first_name, last_name, password, grade) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmtLogin = con.prepareStatement(sqlLogin);
            pstmtLogin.setString(1, firstName);
            pstmtLogin.setString(2, lastName);
            pstmtLogin.setString(3, hashedPassword);
            pstmtLogin.setString(4, grade);
            pstmtLogin.executeUpdate();
            pstmtLogin.close();
            
            String tableName = grade.equals("Étudiant") ? "student" : "teacher";
            String sqlGrade = "INSERT INTO " + tableName + " (first_name, last_name, grade, age) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmtGrade = con.prepareStatement(sqlGrade);
            pstmtGrade.setString(1, firstName);
            pstmtGrade.setString(2, lastName);
            pstmtGrade.setString(3, grade);
            pstmtGrade.setInt(4, age);
            pstmtGrade.executeUpdate();
            pstmtGrade.close();
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createAccount(String firstName, String lastName, String password, String grade) {
        return createAccount(firstName, lastName, password, grade, 0);
    }

    public boolean authenticate(String firstName, String password, String grade) {
        try {
            String sql = "SELECT password FROM login WHERE first_name = ? AND grade = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, grade);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String storedHash = rs.getString("password");
                boolean isValid = PassWord.verifyPassword(password, storedHash);
                rs.close();
                pstmt.close();
                return isValid;
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean createAccount(String firstName, String lastName, String password) {
        return createAccount(firstName, lastName, password, "Étudiant");
    }
    
    public boolean authenticate(String firstName, String password) {
        return authenticate(firstName, password, "Étudiant");
    }
}