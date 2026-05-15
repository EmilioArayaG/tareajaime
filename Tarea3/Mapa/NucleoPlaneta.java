package mapa;

import entidades.Enemigo;
import entidades.Jugador;
import entidades.Sephiroth;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NucleoPlaneta extends Zona {
    private int materiasMinimasRequeridas;

    public NucleoPlaneta(Scanner sc) {
        super("Nucleo del Planeta", 20, new ArrayList<>(), sc);
        this.materiasMinimasRequeridas = 2;
    }

    // Inicia el combate final contra Sephiroth al explorar la zona.
    // @param cloud - el jugador
    @Override
    public void accionZona(Jugador cloud) {
        iniciarCombate(cloud);
    }

    // Verifica que Cloud sea Nivel 20+ y tenga al menos 2 materias equipadas en el arma.
    // @param cloud - el jugador
    // @return true si cumple ambos requisitos
    @Override
    public boolean validarAcceso(Jugador cloud) {
        boolean nivelOk    = cloud.getNivel() >= nivelRequerido;
        boolean materiasOk = cloud.getBusterSword().getMateriasEquipadas().size()
                             >= materiasMinimasRequeridas;
        if (!nivelOk) {
            System.out.println("Necesitas ser Nivel " + nivelRequerido
                + " para entrar. (Nivel actual: " + cloud.getNivel() + ")");
        }
        if (nivelOk && !materiasOk) {
            System.out.println("Necesitas al menos " + materiasMinimasRequeridas
                + " materias equipadas en el arma.");
        }
        return nivelOk && materiasOk;
    }

    // Inicia el combate contra Sephiroth con las mecanicas especiales de SuperNova y Limite.
    // Si Cloud gana imprime el mensaje de victoria y termina el programa.
    // Si Cloud pierde, Sephiroth se restaura completamente para el proximo intento.
    // @param cloud - el jugador
    public void iniciarCombate(Jugador cloud) {
        System.out.println("\n========================================");
        System.out.println("  NUCLEO DEL PLANETA - COMBATE FINAL");
        System.out.println("  Sephiroth aparece ante ti...");
        System.out.println("========================================");

        Sephiroth seph = new Sephiroth();

        while (seph.getStats().getHpActual() > 0 && cloud.getStats().getHpActual() > 0) {
            mostrarEstadoSeph(cloud, seph);
            int opcion = pedirOpcionSeph(cloud);

            if (opcion == 1) {
                // Ataque fisico
                int dano = cloud.getBusterSword().calcularDanoFisico();
                seph.getStats().recibirDMG(dano);
                cloud.cargarLimiteInfligido(dano);
                System.out.println("Cloud ataca a Sephiroth por " + dano + " de dano fisico!");

            } else if (opcion == 2) {
                // Magia
                boolean usada = procesarMagiaSeph(cloud, seph);
                if (!usada) continue;

            } else if (opcion == 3) {
                // Ataque Limite
                int dano = cloud.getBusterSword().calcularDanoLimite();
                seph.getStats().recibirDMG(dano);
                seph.reiniciarContador();
                System.out.println("*** Cloud usa ATAQUE LIMITE en Sephiroth por " + dano + " de dano! ***");
                System.out.println("El contador de SuperNova fue reiniciado!");
            }

            // Victoria
            if (seph.getStats().getHpActual() <= 0) {
                imprimirVictoria(cloud);
                System.exit(0);
            }

            // Turno de Sephiroth
            seph.atacar(cloud);
            seph.incrementarContador();

            // SuperNova
            if (seph.getContadorSuperNova() >= 10) {
                seph.lanzarSuperNova();
                cloud.getStats().setHpActual(0);
            }
        }

        // Derrota
        System.out.println("Cloud fue derrotado por Sephiroth...");
        cloud.aplicarPenalidadMuerte();
        System.out.println("Sephiroth se restaura. Preparate para el proximo intento.");
    }

    // Muestra el estado de combate especifico contra Sephiroth con el contador de SuperNova.
    // @param cloud - el jugador
    // @param seph  - Sephiroth
    private void mostrarEstadoSeph(Jugador cloud, Sephiroth seph) {
        System.out.println("------------------------------------------");
        System.out.println("Cloud     | HP: " + cloud.getStats().getHpActual()
            + "/" + cloud.getStats().getHpMaximo()
            + "  MP: " + cloud.getStats().getMpActual()
            + "/" + cloud.getStats().getMpMaximo()
            + "  Limite: " + cloud.getLimiteActual() + "/100");
        System.out.println("Sephiroth | HP: " + seph.getStats().getHpActual()
            + "/" + seph.getStats().getHpMaximo()
            + "  [SuperNova: " + seph.getContadorSuperNova() + "/10]");
        System.out.println("------------------------------------------");
    }

    // Muestra el menu de combate contra Sephiroth (sin opcion de huir) y retorna la opcion.
    // @param cloud - el jugador
    // @return opcion valida elegida
    private int pedirOpcionSeph(Jugador cloud) {
        while (true) {
            System.out.println("--- Tu turno ---");
            System.out.println("1. Ataque Fisico");
            System.out.println("2. Magia");
            if (cloud.getLimiteActual() >= 100) {
                System.out.println("3. *** ATAQUE LIMITE *** (reinicia contador SuperNova)");
            } else {
                System.out.println("3. Limite (bloqueado " + cloud.getLimiteActual() + "/100)");
            }
            int op = leerOpcion(1, 3);
            if (op == 3 && cloud.getLimiteActual() < 100) {
                System.out.println("El Ataque Limite todavia no esta disponible!");
                continue;
            }
            return op;
        }
    }

    // Muestra el submenu de magia y aplica el efecto sobre Sephiroth (sin multiplicadores).
    // @param cloud - el jugador
    // @param seph  - Sephiroth
    // @return true si se lanzo la magia, false si se cancelo o no fue posible
    private boolean procesarMagiaSeph(Jugador cloud, Sephiroth seph) {
        List<componentes.Elemento> disponibles = cloud.getBusterSword().getElementosEquipados();
        if (disponibles.isEmpty()) {
            System.out.println("No tienes materias equipadas!");
            return false;
        }
        System.out.println("--- Selecciona elemento ---");
        for (int i = 0; i < disponibles.size(); i++) {
            componentes.Elemento el    = disponibles.get(i);
            int                  costo = cloud.getBusterSword().calcularCostoMP(el);
            String               aviso = (cloud.getStats().getMpActual() >= costo) ? "" : " [Sin MP]";
            System.out.println((i + 1) + ". " + el.name() + " (Costo: " + costo + " MP)" + aviso);
        }
        System.out.println((disponibles.size() + 1) + ". Cancelar");

        int sel = leerOpcion(1, disponibles.size() + 1);
        if (sel == disponibles.size() + 1) return false;

        componentes.Elemento elElegido = disponibles.get(sel - 1);
        int                  costo     = cloud.getBusterSword().calcularCostoMP(elElegido);

        if (cloud.getStats().getMpActual() < costo) {
            System.out.println("MP insuficiente!");
            return false;
        }

        cloud.getStats().setMpActual(cloud.getStats().getMpActual() - costo);
        int dano = cloud.getBusterSword().calcularDanoMagico(elElegido);

        if (elElegido == componentes.Elemento.CURA) {
            componentes.Estadisticas s   = cloud.getStats();
            int                      cura = Math.min(dano, s.getHpMaximo() - s.getHpActual());
            s.setHpActual(s.getHpActual() + cura);
            System.out.println("Cloud usa CURA y recupera " + cura + " HP!");
        } else {
            seph.getStats().recibirDMG(dano);
            cloud.cargarLimiteInfligido(dano);
            System.out.println("Cloud lanza " + elElegido.name()
                + " en Sephiroth por " + dano + " de dano!");
        }
        return true;
    }

    // Imprime el mensaje de victoria con estadisticas finales de Cloud.
    // @param cloud - el jugador victorioso
    private void imprimirVictoria(Jugador cloud) {
        System.out.println("\n==========================================");
        System.out.println("  *** VICTORIA! ***");
        System.out.println("  Cloud ha derrotado a Sephiroth!");
        System.out.println("  El planeta ha sido salvado.");
        System.out.println("==========================================");
        System.out.println("  Estadisticas finales:");
        System.out.println("  Nombre : " + cloud.nombre);
        System.out.println("  Nivel  : " + cloud.getNivel());
        System.out.println("  HP     : " + cloud.getStats().getHpActual()
            + "/" + cloud.getStats().getHpMaximo());
        System.out.println("  MP     : " + cloud.getStats().getMpActual()
            + "/" + cloud.getStats().getMpMaximo());
        System.out.println("  Fuerza : " + cloud.getStats().getFuerza());
        System.out.println("  Magia  : " + cloud.getStats().getMagia());
        System.out.println("==========================================");
    }
}
