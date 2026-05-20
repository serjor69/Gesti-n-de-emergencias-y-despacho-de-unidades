package src;

public class InventarioMedicinas {
    public NodoMedicina raiz;

    public void insertar(int id, String nombre) {
        raiz = insertarRecursivo(raiz, id, nombre);
    }

    private NodoMedicina insertarRecursivo(NodoMedicina actual, int id, String nombre) {
        if (actual == null) return new NodoMedicina(id, nombre);
        if (id < actual.idMedicina) {
            actual.izq = insertarRecursivo(actual.izq, id, nombre);
        } else if (id > actual.idMedicina) {
            actual.der = insertarRecursivo(actual.der, id, nombre);
        }
        return actual;
    }

    public String buscarConPasos(NodoMedicina actual, int id, String log) {
        if (actual == null) return log + "ID #" + id + " no encontrado en bodega.\n";
        if (id == actual.idMedicina) return log + "ENCONTRADO: " + actual.nombre + " (ID #" + id + ")\n";
        
        if (id < actual.idMedicina) {
            return buscarConPasos(actual.izq, id, log + "Buscando en " + actual.idMedicina + " (" + actual.nombre + ") -> Izquierda\n");
        } else {
            return buscarConPasos(actual.der, id, log + "Buscando en " + actual.idMedicina + " (" + actual.nombre + ") -> Derecha\n");
        }
    }
}