package componentes;

public class Materia {
    private String nombre;
    private Elemento elemento;

    public Materia(String nombre, Elemento elemento) {
        this.nombre   = nombre;
        this.elemento = elemento;
    }

    public String   getNombre()   { return nombre; }
    public Elemento getElemento() { return elemento; }
}
