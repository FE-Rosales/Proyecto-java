package modelo;
/**
 * Estados posibles de una reserva. PENDIENTE hasta que se pague el
 * minimo requerido, CONFIRMADA una vez alcanzado ese minimo, y
 * CANCELADA si se anula manualmente.
 */
public enum EstadoReserva {
    PENDIENTE, CONFIRMADA, CANCELADA
}