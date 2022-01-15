import java.sql.*;
import java.util.Scanner;

public class sqlsearch {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/mysql?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASS = "20000815mtm";
    String city = null;

    public sqlsearch(String city) {
        this.city = city;
    }

    int get_adcode() {
        int adcode = -1;
        Connection conn = null;
        Statement stmt = null;
        System.out.println("city input");
        String sql = "SELECT * FROM weather WHERE 中文名 like '%" + city + "%'";
        // System.out.println(sql);
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("查询中....");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            // System.out.println("实例化Statement对象...");
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                adcode = rs.getInt("adcode");
            }
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            // System.out.println("驱动加载失败");
            System.out.println("查询失败");
        } catch (SQLException s) {
            // s.printStackTrace();
            System.out.println("查询失败");
        }
        return adcode;
    }
}
