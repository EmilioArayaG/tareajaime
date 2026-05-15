package mapa;

import componentes.Elemento;
import componentes.Materia;
import entidades.Enemigo;
import entidades.EnemigoSalvaje;
import entidades.Jugador;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Gongaga extends Zona {
    private List<Materia> poolMaterias;

    public Gongaga(Scanner sc) {
        super("Gongaga", 5, new ArrayList<>(), sc);
        poolMaterias = new ArrayList<>();
        poolMaterias.add(new Materia("Materia de Fuego",  Elemento.FUEGO));
        poolMaterias.add(new Materia("Materia de Hielo",  Elemento.HIELO));
        poolMaterias.add(new Materia("Materia de Rayo",   Elemento.RAYO));
        poolMaterias.add(new Materia("Materia de Cura",   Elemento.CURA));
    }

    // Ejecuta un evento aleatorio en Gongaga: 30% materia o 70% emboscada enemiga.
    // @param cloud - el jugador
    @Override
    public void accionZona(Jugador cloud) {
        System.out.println("\n=== Explorando Gongaga ===");
        if (rand.nextDouble() < 0.30) {
            encontrarMateria(cloud);
        } else {
            List<Enemigo> grupo = generarGrupoEnemigo();
            System.out.println("Emboscada! Los enemigos atacan por sorpresa!");
            boolean gano = combatir(cloud, grupo, true, true);
            if (!gano && cloud.getStats().getHpActual() <= 0) {
                System.out.println("Cloud fue rescatado y transportado al Sector 7.");
            }
        }
    }

    // Selecciona una materia del pool con elemento aleatorio (FUEGO/HIELO/RAYO/CURA) y la da a Cloud.
    // @param cloud - el jugador que recibe la materia
    private void encontrarMateria(Jugador cloud) {
        Materia hallada = poolMaterias.get(rand.nextInt(poolMaterias.size()));
        Materia nueva   = new Materia(hallada.getNombre(), hallada.getElemento());
        cloud.addToMochila(nueva);
        System.out.println("Cloud encontro una " + nueva.getNombre() + "!");
        System.out.println("Elemento: " + nueva.getElemento().name()
            + "  (guardada en la mochila)");
    }

    // Genera un grupo de 1 a 3 EnemigoSalvaje aleatorios para la emboscada.
    // Probabilidades: 60% -> 1 enemigo, 30% -> 2 enemigos, 10% -> 3 enemigos.
    // @return lista de enemigos generados
    public List<Enemigo> generarGrupoEnemigo() {
        double roll = rand.nextDouble();
        int cantidad = (roll < 0.60) ? 1 : (roll < 0.90) ? 2 : 3;

        List<Enemigo> grupo = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            grupo.add(crearEnemigoAleatorio());
        }
        return grupo;
    }

    // Instancia un EnemigoSalvaje aleatorio del catalogo (Planta Carnivora, Sapo, Robot).
    // @return enemigo salvaje instanciado con sus stats y afinidades
    private EnemigoSalvaje crearEnemigoAleatorio() {
        int tipo = rand.nextInt(3);
        switch (tipo) {
            case 0:
                return new EnemigoSalvaje("Planta Carnivora", 80, 15,
                    Arrays.asList(Elemento.FUEGO, Elemento.HIELO),
                    null,
                    Arrays.asList(Elemento.RAYO));
            case 1:
                return new EnemigoSalvaje("Sapo de la Jungla", 60, 12,
                    Arrays.asList(Elemento.RAYO, Elemento.HIELO),
                    Arrays.asList(Elemento.FUEGO),
                    null);
            default:
                return new EnemigoSalvaje("Robot Centinela", 100, 20,
                    Arrays.asList(Elemento.RAYO),
                    Arrays.asList(Elemento.FISICO, Elemento.HIELO),
                    null);
        }
    }
}
