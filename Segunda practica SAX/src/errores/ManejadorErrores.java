package errores;
import javax.xml.bind.*;
import java.io.File;

public class ManejadorErrores {
    Incidencias listaIncidencias;

    public ManejadorErrores(){
        listaIncidencias=new Incidencias();
    }

    public void abrirIncidenciasJAXB (File archivoXML){
        JAXBContext contexto;
        try {
            contexto = JAXBContext.newInstance(Incidencias.class);
            Unmarshaller u = contexto.createUnmarshaller();
            listaIncidencias = (Incidencias) u.unmarshal(archivoXML);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void anhadirIncidencia(Incidencia incidencia){
        listaIncidencias.getIncidencia().add(incidencia);
    }

    public void guardarIncidencias(File archivoXML) {
        ObjectFactory of = new ObjectFactory();
        JAXBContext contexto;
        try {
            contexto = JAXBContext.newInstance(Incidencias.class);
            Marshaller marshalero = contexto.createMarshaller();
            marshalero.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshalero.marshal(of.createIncidencias(listaIncidencias),archivoXML);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
