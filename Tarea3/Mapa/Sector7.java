package mapa;

import componentes.Mejora;
import componentes.TipoStat;
import entidades.Enemigo;
import entidades.EnemigoSimulador;
import entidades.Jugador;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Sector7 extends Zona {
    private List<Mejora> tiendaLocal;

    public Sector7(Scanner sc) {
        super("Sector 7", 1, new ArrayList<>(), sc);
        tiendaLocal = new ArrayList<>();
        tiendaLocal.add(new Mejora("Mejora de Vitalidad (+20 HP Max)", 100, TipoStat.HP_MAX,  20));
        tiendaLocal.add(new Mejora("Mejora de Eter (+10 MP Max)",      120, TipoStat.MP_MAX,  10));
        tiendaLocal.add(new Mejora("Mejora Fisica (+10 Fuerza)",       150, TipoStat.FUERZA,  10));
    }

    // Muestra el submenu del Sector 7: simulador de combate o tienda de chatarra.
    // @param cloud - el jugador
    @Override
    public void accionZona(Jugador cloud) {
        System.out.println("\n=== Sector 7 - Zona Segura ===");
        System.out.println("1. Simulador de Combate");
        System.out.println("2. Tienda de Chatarra");
        System.out.println("3. Volver");
        int op = leerOpcion(1, 3);
        if      (op == 1) iniciarSimulador(cloud);
        else if (op == 2) abrirTienda(cloud);
    }

    // Valida si el jugador puede acceder al Sector 7 (siempre accesible desde Nivel 1).
    // @param cloud - el jugador
    // @return true siempre (nivel requerido = 1)
    @Override
    public boolean validarAcceso(Jugador cloud) {
        return true;
    }

    // Inicia un combate de entrenamiento contra 1 o 2 EnemigoSimulador (zona segura).
    // Si Cloud pierde su HP queda en 1 sin penalizacion adicional.
    // @param cloud - el jugador
    public void iniciarSimulador(Jugador cloud) {
        System.out.println("\n--- Simulador de Combate ---");
        List<Enemigo> grupo = new ArrayList<>();
        grupo.add(new EnemigoSimulador());
        if (rand.nextDouble() < 0.30) {
            grupo.add(new EnemigoSimulador());
            System.out.println("El simulador genera 2 soldados de entrenamiento!");
        } else {
            System.out.println("El simulador genera 1 soldado de entrenamiento.");
        }

        boolean gano = combatir(cloud, grupo, false, true);
        if (!gano && cloud.getStats().getHpActual() <= 0) {
            cloud.getStats().setHpActual(1);
            System.out.println("Simulacion terminada. Cloud queda con 1 HP (zona segura).");
        }
        cloud.getStats().setMpActual(cloud.getStats().getMpMaximo());
    }

    // Abre la tienda donde Cloud puede gastar chatarra en mejoras permanentes.
    // @param cloud - el jugador
    public void abrirTienda(Jugador cloud) {
        boolean enTienda = true;
        while (enTienda) {
            System.out.println("\n=== Tienda de Chatarra ===");
            System.out.println("Chatarra disponible: " + cloud.getChatarra());
            for (int i = 0; i < tiendaLocal.size(); i++) {
                Mejora m = tiendaLocal.get(i);
                System.out.println((i + 1) + ". " + m.getNombre()
                    + "  [Costo: " + m.getCostoChatarra() + "]");
            }
            System.out.println((tiendaLocal.size() + 1) + ". Salir de la tienda");

            int op = leerOpcion(1, tiendaLocal.size() + 1);
            if (op == tiendaLocal.size() + 1) {
                enTienda = false;
            } else {
                Mejora elegida = tiendaLocal.get(op - 1);
                comprarMejora(cloud, elegida);
            }
        }
    }

    // Intenta comprar una mejora: descuenta chatarra y aplica el bono de forma permanente.
    // @param cloud  - el jugador comprador
    // @param mejora - la mejora a adquirir
    private void comprarMejora(Jugador cloud, Mejora mejora) {
        if (cloud.getChatarra() < mejora.getCostoChatarra()) {
            System.out.println("Chatarra insuficiente. Necesitas "
                + mejora.getCostoChatarra() + " y tienes " + cloud.getChatarra() + ".");
            return;
        }
        cloud.setChatarra(cloud.getChatarra() - mejora.getCostoChatarra());
        aplicarMejora(cloud, mejora);
        System.out.println("Compraste: " + mejora.getNombre()
            + "  Chatarra restante: " + cloud.getChatarra());
    }

    // Aplica el bono de una mejora al componente Estadisticas del jugador.
    // @param cloud  - el jugador
    // @param mejora - mejora cuyo bono se aplica
    private void aplicarMejora(Jugador cloud, Mejora mejora) {
        switch (mejora.getStatAfectado()) {
            case HP_MAX:
                cloud.getStats().setHpMaximo(cloud.getStats().getHpMaximo() + mejora.getValorBono());
                cloud.getStats().setHpActual(cloud.getStats().getHpActual() + mejora.getValorBono());
                break;
            case MP_MAX:
                cloud.getStats().setMpMaximo(cloud.getStats().getMpMaximo() + mejora.getValorBono());
                cloud.getStats().setMpActual(cloud.getStats().getMpActual() + mejora.getValorBono());
                break;
            case FUERZA:
                cloud.getStats().setFuerza(cloud.getStats().getFuerza() + mejora.getValorBono());
                break;
            default:
                break;
        }
    }
}
