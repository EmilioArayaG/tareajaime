package componentes;

public interface Vulnerable {
    /**
     * Evalúa la debilidad de la entidad frente a un elemento mágico[cite: 174].
     * @param elementoMagia El elemento de la materia utilizada en el ataque[cite: 174].
     * @return El multiplicador de daño (ej. 2.0 para debilidad, 0.5 resistencia, etc.)[cite: 174].
     */
    double evaluarDebilidad(Elemento elementoMagia);
}