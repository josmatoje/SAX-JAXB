/*
@author Javi Ruiz y Jose Maria Mata
*
 */

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GestoraSAX {

    XMLReader procesadorXML;
    ParseadorApuestas gestor;
    InputSource archivoXML;

    public GestoraSAX(String nombreArchivo){
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();
            procesadorXML = parser.getXMLReader();
        } catch (SAXException | ParserConfigurationException ex) {
            Logger.getLogger(GestoraSAX.class.getName()).log(Level.SEVERE, null, ex);
        }
        gestor = new ParseadorApuestas();
        procesadorXML.setContentHandler(gestor);
        archivoXML = new InputSource(nombreArchivo);
    }
    void parseaApuestas(){
        try {
            procesadorXML.parse(archivoXML);
        } catch (IOException | SAXException ex) {
            Logger.getLogger(GestoraSAX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
