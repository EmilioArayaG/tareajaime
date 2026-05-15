package entidades;

import componentes.Elemento;
import componentes.Vulnerable;
import java.util.ArrayList;
import java.util.List;

public class EnemigoSalvaje extends Enemigo implements Vulnerable {
    private List<Elemento> debilidades;
    private List<Elemento> resistencias;
    private List<Elemento> inmunidades;

    public EnemigoSalvaje(String nombre, int hp, int fuerza,
                          List<Elemento> debilidades,
                          List<Elemento> resistencias,
                          List<Elemento> inmunidades) {
        super(nombre, hp, fuerza, 0.85);
        this.xpRecompensa       = 80 + rand.nextInt(21);  // 80 a 100
        this.chatarraRecompensa = 50 + rand.nextInt(26);  // 50 a 75
        this.debilidades  = (debilidades  != null) ? debilidades  : new ArrayList<>();
        this.resistencias = (resistencias != null) ? resistencias : new ArrayList<>();
        this.inmunidades  = (inmunidades  != null) ? inmunidades  : new ArrayList<>();
    }

    // Otorga chatarra al jugador como recompensa por haber derrotado al enemigo.
    // @param cloud - jugador que recibe la chatarra
    public void giveChatarraRecompensa(Jugador cloud) {
        System.out.println(nombre + " dejo caer " + chatarraRecompensa + " de chatarra!");
        cloud.addChatarra(chatarraRecompensa);
    }

    // Evalua el multiplicador elemental del enemigo frente al elemento recibido.
    // @param elementoMagia - elemento del ataque magico de Cloud
    // @return 2.0 debilidad, 0.5 resistencia, 0.0 inmunidad, 1.0 neutro
    @Override
    public double evaluarDebilidad(Elemento elementoMagia) {
        if (inmunidades.contains(elementoMagia))  return 0.0;
        if (debilidades.contains(elementoMagia))  return 2.0;
        if (resistencias.contains(elementoMagia)) return 0.5;
        return 1.0;
    }
}
