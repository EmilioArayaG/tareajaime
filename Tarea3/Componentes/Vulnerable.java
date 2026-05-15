package componentes;

public interface Vulnerable {
    // Evalua la debilidad de la entidad frente a un elemento magico.
    // @param elementoMagia - el elemento del ataque magico recibido
    // @return multiplicador de dano: 2.0 debilidad, 0.5 resistencia, 0.0 inmunidad, 1.0 neutro
    double evaluarDebilidad(Elemento elementoMagia);
}
