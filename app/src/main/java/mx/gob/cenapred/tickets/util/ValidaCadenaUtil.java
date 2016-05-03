package mx.gob.cenapred.tickets.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mx.gob.cenapred.tickets.constant.MainConstant;
import mx.gob.cenapred.tickets.exception.BadInputDataException;
import mx.gob.cenapred.tickets.exception.NoInputDataException;
import mx.gob.cenapred.tickets.exception.NoUserLoginException;

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

    private Boolean esNoFolio(String cadena){
        Pattern regExp = Pattern.compile("^[0-9]{9}$");
        Matcher match = regExp.matcher(cadena);
        return match.matches();
    }

    public void validarUsuario(String cadena) throws BadInputDataException, NoInputDataException {
        if (cadena.compareTo("")==0){
            throw new NoInputDataException("Debe especificar el usuario");
        }

        if( !esUser(cadena) && !esEmail(cadena) && !esNoEmpleado(cadena)){
            throw new BadInputDataException("El usuario debe ser una de las siguientes opciones:<ul><li>Correo electrónico institucional (con o sin dominio)</li><li>Número de empleado</li></ul>");
        }
    }

    public void validarEmail(String cadena) throws BadInputDataException, NoInputDataException {
        if (cadena.compareTo("")==0){
            throw new NoInputDataException("Debe especificar el correo electrónico");
        }

        if( !esEmail(cadena) ){
            throw new BadInputDataException("Debe ser el correo electrónico proporcionado por CENAPRED");
        }
    }

    public void validarPassword(String cadena) throws BadInputDataException, NoInputDataException {
        if (cadena.compareTo("")==0){
            throw new NoInputDataException("Debe especificar la contraseña");
        }
        if( !esPassword(cadena) ){
            throw new BadInputDataException("La contraseña debe cumplir con las siguientes caracteristicas:<ul><li>Contener al menos una mayúscula</li><li>Contener al menos una minúscula</li><li>Contener al menos un dígito</li><li>Longitud mínima de 8</li><li>Longitud máxima de 15</li></ul>");
        }
    }

    public void validarFolio(String cadena) throws BadInputDataException, NoInputDataException {
        if (cadena.compareTo("")==0){
            throw new NoInputDataException(MainConstant.MESSAGE_DESCRIPTION_NO_ID_REPORT);
        }

        if (!esNoFolio(cadena)) {
            throw new BadInputDataException("El número de folio debe ser de 9 dígitos");
        }
    }

    public void validarApiKey(String cadena) throws NoUserLoginException {
        if (cadena.compareTo("")==0){
            throw new NoUserLoginException(MainConstant.MESSAGE_DESCRIPTION_EMPTY_API_KEY);
        }
    }
}
