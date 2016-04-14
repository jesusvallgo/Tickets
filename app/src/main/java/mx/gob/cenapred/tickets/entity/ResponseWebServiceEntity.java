package mx.gob.cenapred.tickets.entity;

import java.util.List;

public class ResponseWebServiceEntity {
    private EmpleadoEntity empleado;
    private UsuarioEntity usuario;
    private ReporteEntity reporte;
    private List<ReporteEntity> listaReporte;
    private List<AreaAtencionEntity> listaAreaAtencion;
    private List<EstatusEntity> listaEstatus;
    private List<MensajeEntity> listaMensajes;

    public EmpleadoEntity getEmpleado() {
        return empleado;
    }

    public void setEmpleado(EmpleadoEntity empleado) {
        this.empleado = empleado;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public ReporteEntity getReporte() {
        return reporte;
    }

    public void setReporte(ReporteEntity reporte) {
        this.reporte = reporte;
    }

    public List<ReporteEntity> getListaReporte() {
        return listaReporte;
    }

    public void setListaReporte(List<ReporteEntity> listaReporte) {
        this.listaReporte = listaReporte;
    }

    public List<AreaAtencionEntity> getListaAreaAtencion() {
        return listaAreaAtencion;
    }

    public void setListaAreaAtencion(List<AreaAtencionEntity> listaAreaAtencion) {
        this.listaAreaAtencion = listaAreaAtencion;
    }

    public List<EstatusEntity> getListaEstatus() {
        return listaEstatus;
    }

    public void setListaEstatus(List<EstatusEntity> listaEstatus) {
        this.listaEstatus = listaEstatus;
    }

    public List<MensajeEntity> getListaMensajes() {
        return listaMensajes;
    }

    public void setListaMensajes(List<MensajeEntity> listaMensajes) {
        this.listaMensajes = listaMensajes;
    }
}
