package mx.gob.cenapred.tickets.entity;

public class PeticionWSEntity {
    private String metodo;
    private CredencialesEntity credencialesEntity;
    private ReporteEntity reporteEntity;

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public CredencialesEntity getCredencialesEntity() {
        return credencialesEntity;
    }

    public void setCredencialesEntity(CredencialesEntity credencialesEntity) {
        this.credencialesEntity = credencialesEntity;
    }

    public ReporteEntity getReporteEntity() {
        return reporteEntity;
    }

    public void setReporteEntity(ReporteEntity reporteEntity) {
        this.reporteEntity = reporteEntity;
    }
}
