package mx.gob.cenapred.tickets.entity;

import java.util.List;

public class BundleEntity {
    private Integer idReportBundle;
    private Integer idAreaAtencion;
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
