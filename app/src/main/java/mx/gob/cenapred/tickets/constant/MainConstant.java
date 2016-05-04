package mx.gob.cenapred.tickets.constant;

public class MainConstant {

    public static final String URL_WS = "http://10.2.232.190:8088/WSTickets/webservice/";
    public static final String PASSWORD_CRYPTO = "12345";
    public static final Integer DESCRIPTION_MAX_LENGHT = 160;

    // Constantes para titulos de alertas de error
    public static final String MESSAGE_TITLE_SEND_MAIL_SUCCESS = "E-mail enviado correctamente";

    public static final String MESSAGE_TITLE_BAD_INPUT_DATA = "Datos de entrada no válidos";
    public static final String MESSAGE_TITLE_NO_INPUT_DATA = "Sin datos";

    public static final String MESSAGE_TITLE_NO_SESSION = "Sesión";

    public static final String MESSAGE_TITLE_BUILD_JSON_FAIL = "Error al construir la petición JSON";
    public static final String MESSAGE_TITLE_UNKNOWKN_FAIL = "Error no identificado";

    public static final String MESSAGE_TITLE_NOTIFICATION = "Notificación";

    public static final String MESSAGE_TITLE_WS_REQUEST_FAIL = "Error al realizar la petición al Web Service";
    public static final String MESSAGE_TITLE_WS_COMMUNICATION_FAIL = "Error al consultar Web Service";

    // Constantes para descripcion de alertas de error
    public static final String MESSAGE_DESCRIPTION_INSTRUCTION_REGISTER = "Siga las instrucciones para generar su cuenta de usuario";
    public static final String MESSAGE_DESCRIPTION_INSTRUCTION_PASSWORD = "Siga las instrucciones para actualizar su contraseña";

    public static final String MESSAGE_DESCRIPTION_EMPTY_ATTENTION_AREA = "Debe especificar el Área de Atención";
    public static final String MESSAGE_DESCRIPTION_EMPTY_DESCRIPTION = "Debe especificar la descripción";
    public static final String MESSAGE_DESCRIPTION_EMPTY_STATUS = "Debe especificar un Estatus";

    public static final String MESSAGE_DESCRIPTION_BAD_NUMBER_FORMAT = "El Número de folio no puede ser leido correctamente";

    public static final String MESSAGE_DESCRIPTION_NO_API_KEY = "No fue posible recuperar la sesión de usuario.\nSi el problema persiste, reinstale la aplicación.";
    public static final String MESSAGE_DESCRIPTION_NO_ROL = "No fue posible recuperar el rol de usuario";
    public static final String MESSAGE_DESCRIPTION_NO_LIST_HISTORY = "No fue posible recuperar el historial de acciones";
    public static final String MESSAGE_DESCRIPTION_NO_LIST_ATTENTION_AREA = "No fue posible recuperar la lista de área de atención";
    public static final String MESSAGE_DESCRIPTION_NO_LIST_STATUS = "No fue posible recuperar la lista de estatus";
    public static final String MESSAGE_DESCRIPTION_NO_ID_REPORT = "No fue posible recuperar el número de folio";

    public static final String MESSAGE_DESCRIPTION_NO_PENDINDG_REPORT = "No existen reportes por atender";

    public static final String MESSAGE_DESCRIPTION_WS_NO_VALID_METHOD = "No se ha especificado un método válido";

    // Constantes para botnes de alertas de error
    public static final String MESSAGE_BUTTON_POSITIVE = "De acuerdo";

    // Constantes para titulos de alertas de confirmacion
    public static final String CONFIRMATION_TITLE_NEW_REPORT = "Confirmación";

    // Constantes para descripcion de alertas de confirmacion
    public static final String CONFIRMATION_DESCRIPTION_NEW_REPORT = "¿Desea enviar la Solicitud de Servicio?";

    // Constantes para botnes de alertas de confirmacion
    public static final String CONFIRMATION_BUTTON_POSITIVE = "Aceptar";
    public static final String CONFIRMATION_BUTTON_NEGATIVE = "Cancelar";



    // Constantes para avisos TOAST
    public static final String TOAST_REPORT_CREATE_SUCCESS = "El reporte se ha generado de forma correcta";
    public static final String TOAST_REPORT_UPDATE_SUCCESS = "El reporte se ha actualizado de forma correcta";
    public static final String TOAST_REPORT_DELEGATE_SUCCESS = "El reporte se ha turnado de forma correcta";
}
