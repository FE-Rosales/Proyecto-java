/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestores;

import modelo.Resena;
/**
 *
 * @author FABIAN
 */
public class GestorResenas {
    
    private static final int MAX_RESENAS = 500;
    private Resena[] resenas;
    private int contador;

    public GestorResenas() {
        resenas = new Resena[MAX_RESENAS];
        contador = 0;
    }

    public boolean registrar(Resena r) {
        if (contador >= MAX_RESENAS) return false;
        resenas[contador] = r;
        contador++;
        return true;
    }

    public Resena[] listarTodas() {
        Resena[] copia = new Resena[contador];
        for (int i = 0; i < contador; i++) {
            copia[i] = resenas[i];
        }
        return copia;
    }
    
    public boolean eliminarPorReserva(modelo.Reserva reserva) {
        for (int i = 0; i < contador; i++) {
            if (resenas[i].getReserva() == reserva) {
                for (int j = i; j < contador - 1; j++) {
                    resenas[j] = resenas[j + 1];
                }
                resenas[contador - 1] = null;
                contador--;
                return true;
            }
        }
        return false;
    }
}
