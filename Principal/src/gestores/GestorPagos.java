/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestores;
import modelo.Pago;
/**
 *
 * @author FABIAN
 */
public class GestorPagos {
    
    private static final int MAX_PAGOS = 500;
    private Pago[] pagos;
    private int contador;

    public GestorPagos() {
        pagos = new Pago[MAX_PAGOS];
        contador = 0;
    }

    public boolean registrar(Pago p) {
        if (contador >= MAX_PAGOS) return false;
        pagos[contador] = p;
        contador++;
        return true;
    }

    public Pago[] listarTodos() {
        Pago[] copia = new Pago[contador];
        for (int i = 0; i < contador; i++) {
            copia[i] = pagos[i];
        }
        return copia;
    }
}
