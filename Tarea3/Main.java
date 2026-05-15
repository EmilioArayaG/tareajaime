import componentes.Materia;
import entidades.Jugador;
import mapa.Gongaga;
import mapa.NucleoPlaneta;
import mapa.Sector7;
import mapa.Zona;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner  scanner;
    private static Jugador  cloud;
    private static Sector7  sector7;
    private static Gongaga  gongaga;
    private static NucleoPlaneta nucleo;
    private static Zona     zonaActual;

    public static void main(String[] args) {
        scanner  = new Scanner(System.in);
        cloud    = new Jugador();
        sector7  = new Sector7(scanner);
        gongaga  = new Gongaga(scanner);
        nucleo   = new NucleoPlaneta(scanner);
        zonaActual = sector7;

        System.out.println("==========================================");
        System.out.println("  La Amenaza de Sephiroth");
        System.out.println("  El destino del planeta esta en tus manos");
        System.out.println("==========================================");

        while (true) {
            mostrarMenuPrincipal();
            int op = leerOpcion(1, 4);
            switch (op) {
                case 1: zonaActual.accionZona(cloud); break;
                case 2: menuViajar();                 break;
                case 3: menuEquipo();                 break;
                case 4: mostrarStats();               break;
                default: break;
            }
        }
    }

    // Imprime el encabezado del menu principal con el estado de Cloud y la zona actual.
    private static void mostrarMenuPrincipal() {
        System.out.println("\n==========================================");
        System.out.println("| " + cloud.nombre + " - Nivel " + cloud.getNivel()
            + " | XP: " + cloud.getXpActual() + "/" + (10 * cloud.getNivel()) + " |");
        System.out.println("| HP: " + cloud.getStats().getHpActual()
            + "/" + cloud.getStats().getHpMaximo()
            + "  MP: " + cloud.getStats().getMpActual()
            + "/" + cloud.getStats().getMpMaximo() + " |");
        System.out.println("| Chatarra: " + cloud.getChatarra()
            + "  Limite: " + cloud.getLimiteActual() + "/100 |");
        System.out.println("| Zona: " + zonaActual.nombre + " |");
        System.out.println("==========================================");
        System.out.println("1. Explorar zona actual");
        System.out.println("2. Viajar a otra zona");
        System.out.println("3. Gestionar equipo (materias)");
        System.out.println("4. Ver estadisticas completas");
    }

    // Muestra el submenu de viaje entre zonas, validando acceso con validarAcceso.
    private static void menuViajar() {
        System.out.println("\n--- Viajar ---");
        System.out.println("1. Sector 7      (Nivel requerido: 1)");
        System.out.println("2. Gongaga       (Nivel requerido: 5)");
        System.out.println("3. Nucleo del Planeta (Nivel 20 + 2 materias equipadas)");
        System.out.println("4. Cancelar");

        int op = leerOpcion(1, 4);
        Zona destino = null;
        switch (op) {
            case 1: destino = sector7; break;
            case 2: destino = gongaga; break;
            case 3: destino = nucleo;  break;
            default: return;
        }

        if (destino.validarAcceso(cloud)) {
            zonaActual = destino;
            System.out.println("Viajando a " + zonaActual.nombre + "...");
        } else {
            System.out.println("No puedes acceder a " + destino.nombre + " todavia.");
        }
    }

    // Muestra el submenu de gestion de materias: equipar desde mochila o desequipar del arma.
    private static void menuEquipo() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- Gestionar Equipo ---");
            System.out.println("Arma: " + cloud.getBusterSword().nombre
                + " [" + cloud.getBusterSword().getMateriasEquipadas().size()
                + "/" + cloud.getBusterSword().getMaxRanuras() + " ranuras]");

            List<Materia> equipadas = cloud.getBusterSword().getMateriasEquipadas();
            System.out.println("Materias equipadas:");
            if (equipadas.isEmpty()) {
                System.out.println("  (ninguna)");
            } else {
                for (int i = 0; i < equipadas.size(); i++) {
                    System.out.println("  " + (i + 1) + ". "
                        + equipadas.get(i).getNombre()
                        + " [" + equipadas.get(i).getElemento().name() + "]");
                }
            }

            List<Materia> mochila = cloud.getMochila();
            System.out.println("Mochila (" + mochila.size() + " materias):");
            if (mochila.isEmpty()) {
                System.out.println("  (vacia)");
            } else {
                for (int i = 0; i < mochila.size(); i++) {
                    System.out.println("  " + (i + 1) + ". "
                        + mochila.get(i).getNombre()
                        + " [" + mochila.get(i).getElemento().name() + "]");
                }
            }

            System.out.println("1. Equipar materia de la mochila");
            System.out.println("2. Desequipar materia del arma");
            System.out.println("3. Volver");

            int op = leerOpcion(1, 3);
            if (op == 1) {
                equiparDesde(mochila);
            } else if (op == 2) {
                desequiparDesde(equipadas);
            } else {
                salir = true;
            }
        }
    }

    // Pide al jugador que elija una materia de la mochila para equiparla en el arma.
    // @param mochila - lista actual de materias en la mochila
    private static void equiparDesde(List<Materia> mochila) {
        if (mochila.isEmpty()) {
            System.out.println("La mochila esta vacia.");
            return;
        }
        if (cloud.getBusterSword().getMateriasEquipadas().size()
                >= cloud.getBusterSword().getMaxRanuras()) {
            System.out.println("El arma ya tiene todas las ranuras ocupadas.");
            return;
        }
        System.out.println("Elige materia a equipar (0 = cancelar):");
        int op = leerOpcion(0, mochila.size());
        if (op == 0) return;
        Materia elegida = mochila.get(op - 1);
        cloud.getBusterSword().equiparMateria(elegida);
        System.out.println(elegida.getNombre() + " equipada en " + cloud.getBusterSword().nombre + ".");
    }

    // Pide al jugador que elija una materia equipada para desequiparla a la mochila.
    // @param equipadas - lista actual de materias equipadas en el arma
    private static void desequiparDesde(List<Materia> equipadas) {
        if (equipadas.isEmpty()) {
            System.out.println("No hay materias equipadas.");
            return;
        }
        System.out.println("Elige materia a desequipar (0 = cancelar):");
        int op = leerOpcion(0, equipadas.size());
        if (op == 0) return;
        String nombre = equipadas.get(op - 1).getNombre();
        cloud.getBusterSword().desequiparMateria(op - 1);
        System.out.println(nombre + " guardada en la mochila.");
    }

    // Muestra las estadisticas completas de Cloud y las materias equipadas.
    private static void mostrarStats() {
        System.out.println("\n=== Estadisticas de " + cloud.nombre + " ===");
        System.out.println("  Nivel   : " + cloud.getNivel());
        System.out.println("  XP      : " + cloud.getXpActual()
            + " / " + (10 * cloud.getNivel()));
        System.out.println("  HP      : " + cloud.getStats().getHpActual()
            + " / " + cloud.getStats().getHpMaximo());
        System.out.println("  MP      : " + cloud.getStats().getMpActual()
            + " / " + cloud.getStats().getMpMaximo());
        System.out.println("  Fuerza  : " + cloud.getStats().getFuerza());
        System.out.println("  Magia   : " + cloud.getStats().getMagia());
        System.out.println("  Chatarra: " + cloud.getChatarra());
        System.out.println("  Limite  : " + cloud.getLimiteActual() + "/100");
        System.out.println("  Arma    : " + cloud.getBusterSword().nombre
            + " [" + cloud.getBusterSword().getMateriasEquipadas().size()
            + "/" + cloud.getBusterSword().getMaxRanuras() + "]");
        List<Materia> equipadas = cloud.getBusterSword().getMateriasEquipadas();
        if (!equipadas.isEmpty()) {
            System.out.println("  Materias equipadas:");
            for (Materia m : equipadas) {
                System.out.println("    - " + m.getNombre()
                    + " [" + m.getElemento().name() + "]");
            }
        }
    }

    // Lee un entero en el rango [min, max] desde consola, repite si la entrada es invalida.
    // @param min - valor minimo aceptado
    // @param max - valor maximo aceptado
    // @return entero valido ingresado por el jugador
    private static int leerOpcion(int min, int max) {
        while (true) {
            System.out.print("Opcion [" + min + "-" + max + "]: ");
            try {
                int op = Integer.parseInt(scanner.nextLine().trim());
                if (op >= min && op <= max) return op;
                System.out.println("Opcion invalida.");
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un numero.");
            }
        }
    }
}
