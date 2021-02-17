import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    public static Connection abrirConexion(){

         Connection conexion = null;

        try {
            conexion = DriverManager.getConnection(
                    "jdbc:sqlserver://localhost",//:1433;database=APUESTAS",
                    "apuestas",
                    "apuestas"
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return conexion;
    }

}
