/*
@author Javi Ruiz y Jose Maria Mata
*
 */

public class MainInsercionApuestas {

    public static void main(String[] args) {

        String nombreArchivo = "src\\apuestas.xml";
        GestoraSAX gessax = new GestoraSAX(nombreArchivo);
        gessax.parseaApuestas();

    }

}
