package mx.gob.cenapred.tickets.constant;

public class MainConstant {

    public static final String URL_WS = "http://10.2.232.190:8088/WSTickets/webservice/";
    public static final String PASSWORD_CRYPTO = "12345";
    public static final Integer DESCRIPTION_MAX_LENGHT = 160;

    public static final String MESSAGE_TITLE_SEND_MAIL_SUCCESS = "E-mail enviado correctamente";
    public static final String MESSAGE_DESCRIPTION_INSTRUCTION_REGISTER = "Siga las instrucciones para generar su cuenta de usuario";
    public static final String MESSAGE_DESCRIPTION_INSTRUCTION_PASSWORD = "Siga las instrucciones para actualizar su contraseña";

    public static final String MESSAGE_TITLE_BAD_INPUT_DATA = "Datos de entrada no válidos";
    public static final String MESSAGE_DESCRIPTION_NO_ATENTION_AREA = "Debe especificar el Área de Atención";
    public static final String MESSAGE_DESCRIPTION_NO_DESCRIPTION = "Debe especificar la descripción";
    public static final String MESSAGE_DESCRIPTION_NO_STATUS = "Debe especificar un Estatus";

    public static final String MESSAGE_TITLE_BUIL_JSON_FAIL = "Error al construir la petición JSON";

    public static final String MESSAGE_TITLE_WS_REQUEST_FAIL = "Error al realizar la petición al Web Service";
    public static final String MESSAGE_TITLE_WS_COMMUNICATION_FAIL = "Error al consultar Web Service";
    public static final String MESSAGE_DESCRIPTION_WS_NO_VALID_METHOD = "No se ha especificado un método válido";
}
