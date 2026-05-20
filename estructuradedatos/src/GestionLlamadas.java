package src;
import java.util.LinkedList;
import java.util.Queue;

public class GestionLlamadas {
    private Queue<String> colaLlamadas = new LinkedList<>();

    public void recibirLlamada(String descripcion) {
        colaLlamadas.add(descripcion);
    }

    public String atenderLlamada() {
        return colaLlamadas.isEmpty() ? "Sin llamadas" : colaLlamadas.poll();
    }

    public String mostrarCola() {
        if (colaLlamadas.isEmpty()) return "Cola vacía";
        StringBuilder sb = new StringBuilder("Próximas emergencias:\n");
        for (String s : colaLlamadas) sb.append("- ").append(s).append("\n");
        return sb.toString();
    }
}