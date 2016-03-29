package mx.gob.cenapred.tickets.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "noEmpleado", "nombres", "apellidoPaterno", "apellidoMaterno", "nombreCompletoPorApellido", "nombreCompletoPorNombre", "email", "area", "usuario" })
public class EmpleadoEntity {
    private Integer noEmpleado;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombreCompletoPorApellido;
    private String nombreCompletoPorNombre;
    private String email;
    private AreaEntity area;
    private UsuarioEntity usuario;

    public Integer getNoEmpleado() {
        return noEmpleado;
    }

    public void setNoEmpleado(Integer noEmpleado) {
        this.noEmpleado = noEmpleado;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getNombreCompletoPorApellido() {
        return nombreCompletoPorApellido;
    }

    public void setNombreCompletoPorApellido(String nombreCompletoPorApellido) {
        this.nombreCompletoPorApellido = nombreCompletoPorApellido;
    }

    public String getNombreCompletoPorNombre() {
        return nombreCompletoPorNombre;
    }

    public void setNombreCompletoPorNombre(String nombreCompletoPorNombre) {
        this.nombreCompletoPorNombre = nombreCompletoPorNombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AreaEntity getArea() {
        return area;
    }

    public void setArea(AreaEntity area) {
        this.area = area;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }
}
