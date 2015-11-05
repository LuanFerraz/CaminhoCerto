package bancodedados;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class criarConexao {
    public static Connection getConexao() throws SQLException{
        try{
            
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost/caminhocerto_bd","root","321");
            
        }catch(ClassNotFoundException e){
            throw new SQLException(e.getMessage());
        }
        
    }
}
