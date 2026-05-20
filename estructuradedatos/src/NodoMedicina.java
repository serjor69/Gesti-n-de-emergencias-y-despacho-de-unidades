package src;

public class NodoMedicina {
    int idMedicina;
    String nombre; // Nuevo campo
    NodoMedicina izq, der;

    public NodoMedicina(int id, String nombre) {
        this.idMedicina = id;
        this.nombre = nombre;
        this.izq = this.der = null;
    }
}