package mx.gob.cenapred.tickets.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({ "idReporte", "areaAtencion", "usuario", "empleado", "estatus", "fecha", "descripcion" })
public class ReporteEntity {
    private Integer idReporte;
    private AreaAtencionEntity areaAtencion;
    private UsuarioEntity usuario;
    private EmpleadoEntity empleado;
    private EstatusEntity estatus;
    private List<BitacoraEntity> bitacora;
    private String fecha;
    private String descripcion;

    public Integer getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(Integer idReporte) {
        this.idReporte = idReporte;
    }

    public AreaAtencionEntity getAreaAtencion() {
        return areaAtencion;
    }

    public void setAreaAtencion(AreaAtencionEntity areaAtencion) {
        this.areaAtencion = areaAtencion;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public EmpleadoEntity getEmpleado() {
        return empleado;
    }

    public void setEmpleado(EmpleadoEntity empleado) {
        this.empleado = empleado;
    }

    public EstatusEntity getEstatus() {
        return estatus;
    }

    public void setEstatus(EstatusEntity estatus) {
        this.estatus = estatus;
    }

    public List<BitacoraEntity> getBitacora() {
        return bitacora;
    }

    public void setBitacora(List<BitacoraEntity> bitacora) {
        this.bitacora = bitacora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
