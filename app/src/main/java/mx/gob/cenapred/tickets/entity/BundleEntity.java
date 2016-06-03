package mx.gob.cenapred.tickets.entity;

import java.util.List;

public class BundleEntity {
    private Integer idReportBundle;
    private Integer idAreaAtencion;
    private String filtro;
    private Boolean sendMail;
    private Boolean addToBackStack;
    private List<BitacoraEntity> listHistoryAction;
    private List<AreaAtencionEntity> listAreaAtencion;
    private List<EstatusEntity> listEstatus;

    public Integer getIdReportBundle() {
        return idReportBundle;
    }

    public void setIdReportBundle(Integer idReportBundle) {
        this.idReportBundle = idReportBundle;
    }

    public Integer getIdAreaAtencion() {
        return idAreaAtencion;
    }

    public void setIdAreaAtencion(Integer idAreaAtencion) {
        this.idAreaAtencion = idAreaAtencion;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public Boolean getSendMail() {
        return sendMail;
    }

    public void setSendMail(Boolean sendMail) {
        this.sendMail = sendMail;
    }

    public Boolean getAddToBackStack() {
        return addToBackStack;
    }

    public void setAddToBackStack(Boolean addToBackStack) {
        this.addToBackStack = addToBackStack;
    }

    public List<AreaAtencionEntity> getListAreaAtencion() {
        return listAreaAtencion;
    }

    public void setListAreaAtencion(List<AreaAtencionEntity> listAreaAtencion) {
        this.listAreaAtencion = listAreaAtencion;
    }

    public List<BitacoraEntity> getListHistoryAction() {
        return listHistoryAction;
    }

    public void setListHistoryAction(List<BitacoraEntity> listHistoryAction) {
        this.listHistoryAction = listHistoryAction;
    }

    public List<EstatusEntity> getListEstatus() {
        return listEstatus;
    }

    public void setListEstatus(List<EstatusEntity> listEstatus) {
        this.listEstatus = listEstatus;
    }
}
