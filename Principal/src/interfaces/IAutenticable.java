// ===== interfaces/IAutenticable.java =====
package interfaces;

public interface IAutenticable {
    boolean login(String usuario, String contrasena);
}


/*Porque el enunciado pide explícitamente el uso de interfaces como concepto de POO independiente. 
Al declarar login() en una interfaz, Empleado se compromete por contrato a implementarlo. Si más adelante quisieras 
que Cliente también pueda autenticarse de otra forma, podría implementar la misma interfaz sin heredar de Empleado.*/