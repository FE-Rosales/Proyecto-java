
package modelo;
/**
 * Clase abstracta que representa a cualquier trabajador de la agencia.
 * Es la clase padre de Administrador, AsesorDeViaje y Operador.
 * No se puede instanciar directamente: siempre se crea una de sus subclases.
 * Implementa IAutenticable porque todo empleado debe poder iniciar sesion.
 */

import interfaces.IAutenticable;

public abstract class Empleado implements IAutenticable {

    protected String dni;
    protected String nombres;
    protected String apellidos;
    protected String usuario;
    protected String contrasena;
    protected String rol;

    public Empleado(String dni, String nombres, String apellidos,
                     String usuario, String contrasena, String rol) {
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    @Override
    public boolean login(String usuario, String contrasena) {
        return this.usuario.equals(usuario) && this.contrasena.equals(contrasena);
    }

    public abstract String mostrarPermisos();

    public String getDni() {
        return dni; 
    }
    public String getNombres() {
        return nombres; 
    }
    public String getApellidos() {
        return apellidos; 
    }
    public String getUsuario() {
        return usuario; 
    }
    public String getRol() {
        return rol; 
    }

    public void setNombres(String nombres) {
        this.nombres = nombres; 
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos; 
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena; 
    }

    @Override
    public String toString() {
        return nombres + " " + apellidos + " (" + rol + ")";
    }
}
