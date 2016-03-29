package mx.gob.cenapred.tickets.entity;

public class AreaAtencionEntity {
    private Integer idAreaAtencion;
    private String areaAtencion;

    public Integer getIdAreaAtencion() {
        return idAreaAtencion;
    }

    public void setIdAreaAtencion(Integer idAreaAtencion) {
        this.idAreaAtencion = idAreaAtencion;
    }

    public String getAreaAtencion() {
        return areaAtencion;
    }

    public void setAreaAtencion(String areaAtencion) {
        this.areaAtencion = areaAtencion;
    }

    @Override
    public String toString() {
        return this.areaAtencion;
    }
}
