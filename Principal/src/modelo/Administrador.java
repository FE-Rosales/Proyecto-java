package modelo;
/**
 * Empleado con permisos totales de gestion: empleados, destinos,
 * paquetes y promociones. Corresponde al rol "Administrador" del enunciado.
 */

public class Administrador extends Empleado {

    public Administrador(String dni, String nombres, String apellidos,
                          String usuario, String contrasena) {
        super(dni, nombres, apellidos, usuario, contrasena, "Administrador");
    }

    @Override
    public String mostrarPermisos() {
        return "Gestiona empleados, destinos, paquetes y promociones";
    }
}
