package mx.gob.cenapred.tickets.entity;

public class EstatusEntity {
    private Integer idEstatus;
    private String estatus;
    private Boolean editable;

    public Integer getIdEstatus() {
        return idEstatus;
    }

    public void setIdEstatus(Integer idEstatus) {
        this.idEstatus = idEstatus;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    @Override
    public String toString() {
        return this.estatus;
    }
}
