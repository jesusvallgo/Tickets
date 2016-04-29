package mx.gob.cenapred.tickets.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mx.gob.cenapred.tickets.exception.BadInputDataException;

public class ValidaCadenaUtil {
    private Boolean esUser(String cadena){
        Pattern regExp = Pattern.compile("^[a-z]{5,15}$");
        Matcher match = regExp.matcher(cadena);
        return match.matches();
    }

    private Boolean esEmail(String cadena){
        Pattern regExp = Pattern.compile("^[a-z]{5,15}@cenapred.unam.mx$");
        Matcher match = regExp.matcher(cadena);
        return match.matches();
    }

    private Boolean esNoEmpleado(String cadena){
        Pattern regExp = Pattern.compile("^[0-9]{5,7}$");
        Matcher match = regExp.matcher(cadena);
        return match.matches();
    }

    private Boolean esPassword(String cadena){
        Pattern regExp = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,15}$");
        Matcher match = regExp.matcher(cadena);
        return match.matches();
    }

    public void validarUsuario(String cadena) throws BadInputDataException {
        if( !esUser(cadena) && !esEmail(cadena) && !esNoEmpleado(cadena)){
            throw new BadInputDataException("El usuario debe ser una de las siguientes opciones:<ul><li>Correo electrónico institucional (con o sin dominio)</li><li>Número de empleado</li></ul>");
        }
    }

    public void validarEmail(String cadena) throws BadInputDataException{
        if( !esEmail(cadena) ){
            throw new BadInputDataException("Debe especificar el correo electrónico institucional");
        }
    }

    public void validarPassword(String cadena) throws BadInputDataException{
        if( !esPassword(cadena) ){
            throw new BadInputDataException("La contraseña debe cumplir con las siguientes caracteristicas:<ul><li>Ser alfanumérica</li><li>Longitud mínima de 8</li><li>Longitud máxima de 15</li></ul>");
        }
    }
}
