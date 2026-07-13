package gestores;

import modelo.Reserva;
import modelo.PaqueteTuristico;
import modelo.AsesorDeViaje;
import modelo.Cliente;
import modelo.Voucher;
import java.time.LocalDate;

/**
 * Administra el arreglo de todas las reservas del sistema. Es la clase
 * que orquesta el proceso completo: valida cupos, crea la reserva,
 * agrega los clientes del grupo, y actualiza el cupo del paquete.
 */
public class GestorReservas {

    private static final int MAX_RESERVAS = 500;

    private Reserva[] reservas;
    private int contador;

    public GestorReservas() {
        reservas = new Reserva[MAX_RESERVAS];
        contador = 0;
    }

    /**
     * Crea una nueva reserva de forma completa y segura:
     * 1. Valida que haya cupo suficiente en el paquete.
     * 2. Crea el objeto Reserva (que calcula su propio precio total).
     * 3. Agrega cada cliente del grupo a la reserva.
     * 4. Reduce el cupo disponible del paquete.
     * 5. Registra la reserva en el arreglo del Gestor.
     *
     * @return la Reserva creada, o null si no habia cupo o espacio disponible
     */
    public Reserva crearReserva(PaqueteTuristico paquete, AsesorDeViaje asesor,
                                 Cliente[] grupo, int pasajerosAdultos, int pasajerosMenores,
                                 LocalDate fechaReserva) {
        int totalPasajeros = pasajerosAdultos + pasajerosMenores;

        if (!paquete.hayCupoDisponible(totalPasajeros)) {
            return null;
        }
        if (contador >= MAX_RESERVAS) {
            return null;
        }

        Reserva nuevaReserva = new Reserva(paquete, asesor, pasajerosAdultos,
                                            pasajerosMenores, fechaReserva);

        for (Cliente c : grupo) {
            nuevaReserva.agregarCliente(c);
        }

        paquete.reducirCupo(totalPasajeros);

        reservas[contador] = nuevaReserva;
        contador++;

        return nuevaReserva;
    }

    //Genera el voucher de una reserva ya creada.
    public Voucher generarVoucher(Reserva reserva) {
        return new Voucher(reserva);
    }

    //Filtra todas las reservas asociadas a un paquete especifico. 
    public Reserva[] listarPorPaquete(String codigoPaquete) {
        int cantidad = 0;
        for (int i = 0; i < contador; i++) {
            if (reservas[i].getPaquete().getCodigo().equalsIgnoreCase(codigoPaquete)) {
                cantidad++;
            }
        }

        Reserva[] resultado = new Reserva[cantidad];
        int j = 0;
        for (int i = 0; i < contador; i++) {
            if (reservas[i].getPaquete().getCodigo().equalsIgnoreCase(codigoPaquete)) {
                resultado[j] = reservas[i];
                j++;
            }
        }
        return resultado;
    }

    //Filtra las reservas hechas dentro de un rango de fechas (inclusive).
    public Reserva[] listarPorRangoFechas(LocalDate desde, LocalDate hasta) {
        int cantidad = 0;
        for (int i = 0; i < contador; i++) {
            LocalDate f = reservas[i].getFechaReserva();
            if (!f.isBefore(desde) && !f.isAfter(hasta)) {
                cantidad++;
            }
        }

        Reserva[] resultado = new Reserva[cantidad];
        int j = 0;
        for (int i = 0; i < contador; i++) {
            LocalDate f = reservas[i].getFechaReserva();
            if (!f.isBefore(desde) && !f.isAfter(hasta)) {
                resultado[j] = reservas[i];
                j++;
            }
        }
        return resultado;
    }

    public Reserva[] listarTodas() {
        Reserva[] copia = new Reserva[contador];
        for (int i = 0; i < contador; i++) {
            copia[i] = reservas[i];
        }
        return copia;
    }

    public int getCantidadReservas() {
        return contador;
    }
}