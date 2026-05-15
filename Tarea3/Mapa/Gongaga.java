package mapa;

import java.util.ArrayList;
import java.util.List;
import entidades.Enemigo;
import componentes.Materia;

public class Gongaga extends Zona {
    private List<Materia> poolMaterias; 

    public Gongaga() {
        super("Gongaga", 5, new ArrayList<>());
    }

    public List<Enemigo> generarGrupoEnemigo() { 
        return new ArrayList<>();
    }
}