package src;
import java.util.*;

public class RedEmergencias911 {
    private Map<String, List<String>> grafo = new HashMap<>();

    public RedEmergencias911() {
        // Inicialización básica del grafo
        grafo.put("Estación Central", Arrays.asList("Hospital", "Bomberos"));
    }
}