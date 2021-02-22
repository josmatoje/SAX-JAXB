import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import errores.*;

public class GestoraBD {

    private ManejadorErrores errorHandler;
    private Incidencias incidenciasSucedidas;

    /*
    Entradas: Un objeto apuesta, una callableStatement al procedimiento de insercion de apuesta y un objeto manejador de errores
    Salida: Un booleano que comprueba si se ha realizado el procedimiento con exito
    Precondiciones: Se espera una llamada a un procedimiento creado para la base de datos de este ejercicio
    Postcondiciones: Deberá insertarse el objeto dado en la bbdd o en su defecto genearse un objeto incidencia
     */
    public static boolean insertarApuestaConTipo (Apuesta apuesta, CallableStatement insertaApuesta, ManejadorErrores me){
        boolean insertado=false;
        GregorianCalendar fechaIncidencia = new GregorianCalendar();

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
            incidencia.setUsuario(apuesta.getUsuario());
            incidencia.setFecha(fechaIncidencia.get(Calendar.HOUR_OF_DAY)+":"+fechaIncidencia.get(Calendar.MINUTE)+":"+fechaIncidencia.get(Calendar.SECOND)+" - "+fechaIncidencia.get(Calendar.DAY_OF_MONTH)+"/"+(fechaIncidencia.get(Calendar.MONTH)+1)+"/"+fechaIncidencia.get(Calendar.YEAR));
            incidencia.setEvento(Integer.toString(apuesta.getCombate()));
            incidencia.setImporte(Float.toString(apuesta.getCantidad()));
            incidencia.setMotivoRechazo(throwables.getMessage());
            me.anhadirIncidencia(incidencia);
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
                                    "SELECT MAX(ID) FROM APUESTAS " +
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