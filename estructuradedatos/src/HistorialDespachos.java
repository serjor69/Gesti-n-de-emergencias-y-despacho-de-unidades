package src;
import java.util.Stack;

public class HistorialDespachos {
    private Stack<String> pilaDespachos = new Stack<>();

    public void registrarDespacho(String detalle) {
        pilaDespachos.push(detalle);
    }

    public String deshacerUltimo() {
        if (pilaDespachos.isEmpty()) return "Nada que deshacer";
        return "Acción revertida: " + pilaDespachos.pop();
    }
}