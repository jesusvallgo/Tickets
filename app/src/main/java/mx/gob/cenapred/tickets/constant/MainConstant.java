package mx.gob.cenapred.tickets.constant;

public class MainConstant {

    private final String urlWS = "http://10.2.232.190:8088/WSTickets/webservice/";
    private final String passwordCrypto="12345";
    private final Integer descriptionNewReportMaxLenght = 160;

    public String getUrlWS() {
        return urlWS;
    }

    public String getPasswordCrypto() {
        return passwordCrypto;
    }

    public Integer getDescriptionNewReportMaxLenght() {
        return descriptionNewReportMaxLenght;
    }
}
