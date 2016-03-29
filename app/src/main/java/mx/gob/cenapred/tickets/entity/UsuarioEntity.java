package mx.gob.cenapred.tickets.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({ "noEmpleado", "username", "password", "rol", "areaAtencion", "activo" })
public class UsuarioEntity {
    private Integer noEmpleado;
    private String username;
    private String password;
    private RolEntity rol;
    private List<AreaAtencionEntity> areaAtencion;
    private Boolean activo;

    public Integer getNoEmpleado() {
        return noEmpleado;
    }

    public void setNoEmpleado(Integer noEmpleado) {
        this.noEmpleado = noEmpleado;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RolEntity getRol() {
        return rol;
    }

    public void setRol(RolEntity rol) {
        this.rol = rol;
    }

    public List<AreaAtencionEntity> getAreaAtencion() {
        return areaAtencion;
    }

    public void setAreaAtencion(List<AreaAtencionEntity> areaAtencion) {
        this.areaAtencion = areaAtencion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
