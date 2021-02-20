/*
@author Javi Ruiz y Jose Maria Mata
*
 */
import errores.ManejadorErrores;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.sql.*;

public class ParseadorApuestas extends DefaultHandler {

    //Attributes
    private int combate, tipoApuesta, puntuacion;
    private float cuota, cantidad;
    private String cadena;
    private String usuario;
    private String ganador;
    private TipoViiictoria tipoVictoria;
    private final Connection conexionbbdd;
    private CallableStatement statementApuesta;
    private final ManejadorErrores me;
    private final File incidencias;


    //Constructor
    public ParseadorApuestas(){
        super();
        conexionbbdd = Conexion.abrirConexion();
        me = new ManejadorErrores();
        //Creamos el fichero en el que se almacenarán las incidencias que ocurran
        incidencias = new File("src\\incidencias.xml");
    }
    //Get conexion
    public Connection getConexionbbdd() {
        return conexionbbdd;
    }

    @Override
    public void startDocument(){
        try {
            String sentPrepApuesta = "EXECUTE dbo.InstertarApuesta ?,?,?,?,?,?,?,?";
            statementApuesta = conexionbbdd.prepareCall(sentPrepApuesta);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        me.abrirIncidenciasJAXB(incidencias);

    }
    @Override
    public void endDocument(){
        try {
            getConexionbbdd().close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        me.guardarIncidencias(incidencias);
    }
    @Override
    public void startElement(String uri, String nombre, String nombreC, Attributes att){

    }
    @Override
    public void endElement(String uri, String nombre, String nombreC){
        switch(nombreC) {

            case "usuario":
                usuario=cadena;
            break;

            case "combate":
                combate= Integer.parseInt(cadena);
            break;

            case "cantidad":
                cantidad= Float.parseFloat(cadena);
            break;

            case "cuota":
                cuota= Float.parseFloat(cadena);
            break;

            case "ganador":
                ganador = cadena;
                tipoApuesta=1;
            break;

            case "puntuacion":
                puntuacion = Integer.parseInt(cadena);
                tipoApuesta=2;
            break;

            case "tipoVictoria":
                tipoVictoria = (cadena.equals("KO"))? TipoViiictoria.KO : TipoViiictoria.PUNTUACION;
                //En cualquier caso q no sea victoria por KO se considerara apuesta a victoria por puntuacion
                tipoApuesta=3;
            break;

            case "apuesta":
                Apuesta apuestaActual = new Apuesta(usuario,combate,cantidad,cuota);

                switch (tipoApuesta){
                    case 1:
                        apuestaActual.setGanador(ganador);
                    break;

                    case 2:
                        apuestaActual.setPuntuacion(puntuacion);
                    break;

                    case 3:
                        apuestaActual.setTipoVictoria(tipoVictoria);
                    break;

                }
                GestoraBD.insertarApuestaConTipo(apuestaActual,statementApuesta, me);
                //La fecha no se registra en el xml, se introduce en la insercion de datos en la BBDD
            break;
        }
    }
    @Override
    public void characters (char[] ch, int inicio, int longitud){
        cadena = new String(ch, inicio, longitud);
        cadena=cadena.replaceAll("[\t\n]","");
    }
}
// FIN GestionContenido
