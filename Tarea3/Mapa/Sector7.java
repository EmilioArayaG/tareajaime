package mapa;

import java.util.ArrayList;
import java.util.List;
import entidades.Jugador;
import componentes.Mejora;

public class Sector7 extends Zona {
    private List<Mejora> tiendaLocal;

    public Sector7() {
        super("Sector 7", 1, new ArrayList<>()); 
    }

    public void iniciarSimulador(Jugador cloud) { 
    }

    public void abrirTienda(Jugador cloud) { 
    }
}