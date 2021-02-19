import java.sql.*;
import errores.*;

public class GestoraBD {

    private ManejadorErrores errorHandler;
    private Incidencias incidenciasSucedidas;

    public static boolean insertarApuestaConTipo (Apuesta apuesta, CallableStatement insertaApuesta){
        boolean insertado=false;

        try {

            insertaApuesta.setString(1,apuesta.getUsuario());
            insertaApuesta.setInt(2,apuesta.getCombate());
            insertaApuesta.setFloat(3,apuesta.getCantidad());
            insertaApuesta.setFloat(4,apuesta.getCuota());
            insertaApuesta.setString(5,apuesta.getGanador());
            insertaApuesta.setInt(6,apuesta.getPuntuacion());
            insertaApuesta.setString(7,apuesta.getTipoVictoria().toString());

            insertado=insertaApuesta.execute();

        } catch (SQLException throwables) {
            Incidencia incidencia=new Incidencia();
                incidencia.setEvento(Integer.toString(apuesta.getCombate()));
        }

        return insertado;
    }

    /*
        Entradas: Un objeto Apuesta con todos los parametros necesarios necesarios para registrar una apuesta y la conexion con la BBDD
        Salida: un boleano que devuelve true si se han realizado las operaciones con exito
     */
    @Deprecated
    public static boolean insertarApuesta(Apuesta apuesta, Connection conexion){
        
        String sentPrepApuesta="INSERT INTO APUESTAS(NOMBRE_USUARIO,ID_COMBATE,CANTIDAD,CUOTA) VALUES (?,?,?,?) " +
                                    "SELECT TOP(1) ID FROM APUESTAS " +
                                    /*"WHERE NOMBRE_USUARIO LIKE ? AND " +//Con el WHERE comprobamos que es la misma apuesta
                                            "ID_COMBATE = ? AND " +
                                            "CANTIDAD = ? AND " +
                                            "CUOTA = ? " +*/
                                    "ORDER BY ID DESC"; //El ID es identity, podemos usar solo order by para obtener la ultima apuesta
        //Hemos decidido usar TOP(1) ya que la seleccion
        String sentPrepTipo;
        int tipoApuesta;
        boolean filasInsertadas=false;
        ResultSet IdApuesta;
        PreparedStatement statementApuesta;
        PreparedStatement statementTipo;

        try{

            statementApuesta =conexion.prepareStatement(sentPrepApuesta);

            statementApuesta.setString(1,apuesta.getUsuario());
            statementApuesta.setInt(2,apuesta.getCombate());
            statementApuesta.setFloat(3,apuesta.getCantidad());
            statementApuesta.setFloat(4,apuesta.getCuota());
            /*
            //sets del SELECT
            statementApuesta.setString(5,apuesta.getUsuario());
            statementApuesta.setInt(6,apuesta.getCombate());
            statementApuesta.setFloat(7,apuesta.getCantidad());
            statementApuesta.setFloat(8,apuesta.getCuota());
            */

            //Inserta la apuesta y obtiene el ID de la apuesta insertada
            IdApuesta=statementApuesta.executeQuery();

            //Prepara la sentencia en funcion del tipo de apuesta
            if(apuesta.getPuntuacion()==-1 && apuesta.getTipoVictoria()!= TipoViiictoria.JAVI){
                sentPrepTipo="INSERT INTO APUESTAS_POR_GANADOR(ID_APUESTA, NOMBRE_GANADOR) VALUES (?,?)";
                tipoApuesta=1;
            }else if(apuesta.getGanador()==null && apuesta.getTipoVictoria()!= TipoViiictoria.JAVI){
                sentPrepTipo="INSERT INTO APUESTAS_POR_PUNTUACION (ID_APUESTA, PUNTUACION) VALUES (?,?)";
                tipoApuesta=2;
            }else{
                sentPrepTipo="INSERT INTO APUESTAS_POR_TIPO_DE_VICTORIA(ID_APUESTA, TIPO_DE_VICTORIA) VALUES (?,?)";
                tipoApuesta=3;
            }

            statementTipo = conexion.prepareStatement(sentPrepTipo);
            statementTipo.setInt(1,IdApuesta.getInt(1));
            switch (tipoApuesta){ //Switch para insertar en funcion del tipo
                case 1 : statementTipo.setString(2,apuesta.getUsuario());
                break;
                case 2 : statementTipo.setDouble(2,apuesta.getPuntuacion());
                break;
                case 3: statementTipo.setString(2,apuesta.getTipoVictoria().toString());
                break;
            }

            filasInsertadas=statementTipo.execute();
            //Si se ha insertado esta fila es que se ha insertado la apuesta y se ha seleccionado su ID correctamente

        }catch (SQLException e){
            e.printStackTrace();
        }

        return filasInsertadas;
    }
}