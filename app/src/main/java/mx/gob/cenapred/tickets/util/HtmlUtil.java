package mx.gob.cenapred.tickets.util;

public class HtmlUtil {
    public String parseUL(String cadena){
        cadena = cadena.replace("<ul>","\n").replace("</ul>","");
        cadena = cadena.replace("<li>","\n- ").replace("</li>","");
        return cadena;
    }
}
