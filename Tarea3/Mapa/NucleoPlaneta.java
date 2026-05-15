package mapa;

import java.util.ArrayList;
import entidades.Jugador;

public class NucleoPlaneta extends Zona {
    private int materiasMinimasRequeridas; 

    public NucleoPlaneta() {
        super("Núcleo del Planeta", 20, new ArrayList<>());
        this.materiasMinimasRequeridas = 2; 
    }

    public void iniciarCombate(Jugador cloud) {
    }
}
