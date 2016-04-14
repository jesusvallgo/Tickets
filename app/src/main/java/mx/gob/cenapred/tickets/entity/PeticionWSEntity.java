package mx.gob.cenapred.tickets.entity;

public class PeticionWSEntity {
    private String metodo;
    private String accion;
    private String tipo;
    private String filtro;
    private CredencialesEntity credencialesEntity;
    private TokenGCMEntity tokenGCMEntity;
    private ReporteEntity reporteEntity;

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public CredencialesEntity getCredencialesEntity() {
        return credencialesEntity;
    }

    public void setCredencialesEntity(CredencialesEntity credencialesEntity) {
        this.credencialesEntity = credencialesEntity;
    }

    public TokenGCMEntity getTokenGCMEntity() {
        return tokenGCMEntity;
    }

    public void setTokenGCMEntity(TokenGCMEntity tokenGCMEntity) {
        this.tokenGCMEntity = tokenGCMEntity;
    }

    public ReporteEntity getReporteEntity() {
        return reporteEntity;
    }

    public void setReporteEntity(ReporteEntity reporteEntity) {
        this.reporteEntity = reporteEntity;
    }
}
