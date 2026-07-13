package gestores;

import modelo.Promocion;
import java.time.LocalDate;

/**
 * Administra el arreglo de todas las promociones y descuentos del sistema.
 * Se identifica cada promocion por su codigo.
 */
public class GestorPromociones {

    private static final int MAX_PROMOCIONES = 100;

    private Promocion[] promociones;
    private int contador;

    public GestorPromociones() {
        promociones = new Promocion[MAX_PROMOCIONES];
        contador = 0;
    }

    // Registra una nueva promocion si aun hay espacio disponible. 
    public boolean registrar(Promocion p) {
        if (contador >= MAX_PROMOCIONES) return false;
        promociones[contador] = p;
        contador++;
        return true;
    }

    private int buscarIndicePorCodigo(String codigo) {
        for (int i = 0; i < contador; i++) {
            if (promociones[i].getCodigo().equalsIgnoreCase(codigo)) {
                return i;
            }
        }
        return -1;
    }

    public Promocion buscarPorCodigo(String codigo) {
        int indice = buscarIndicePorCodigo(codigo);
        if (indice == -1) return null;
        return promociones[indice];
    }

    //Elimina una promocion desplazando el resto del arreglo una posicion. 
    public boolean eliminar(String codigo) {
        int indice = buscarIndicePorCodigo(codigo);
        if (indice == -1) return false;

        for (int i = indice; i < contador - 1; i++) {
            promociones[i] = promociones[i + 1];
        }
        promociones[contador - 1] = null;
        contador--;
        return true;
    }

    public Promocion[] listarTodas() {
        Promocion[] copia = new Promocion[contador];
        for (int i = 0; i < contador; i++) {
            copia[i] = promociones[i];
        }
        return copia;
    }

    /**
     * Recorre todas las promociones vigentes en la fecha dada y devuelve
     * el mayor descuento aplicable a un precio especifico. No importa
     * si la promocion vigente es por porcentaje, monto fijo o temporada:
     * al llamar calcularDescuento(), Java ejecuta automaticamente la
     * version correcta segun el tipo real de cada objeto (polimorfismo).
     * @return el monto del mejor descuento encontrado, o 0 si ninguna aplica
     */
    public double obtenerMejorDescuento(double precio, LocalDate fecha) {
        double mejorDescuento = 0;

        for (int i = 0; i < contador; i++) {
            if (promociones[i].estaVigente(fecha)) {
                double descuento = promociones[i].calcularDescuento(precio);
                if (descuento > mejorDescuento) {
                    mejorDescuento = descuento;
                }
            }
        }

        return mejorDescuento;
    }

    public int getCantidadPromociones() {
        return contador;
    }
}