package mapa;

import componentes.Elemento;
import componentes.Estadisticas;
import entidades.Enemigo;
import entidades.EnemigoSalvaje;
import entidades.Jugador;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public abstract class Zona {
    public    String        nombre;
    protected int           nivelRequerido;
    protected List<Enemigo> enemigosDisponibles;
    protected Scanner       scanner;
    protected Random        rand;

    public Zona(String nombre, int nivelRequerido, List<Enemigo> enemigosDisponibles, Scanner sc) {
        this.nombre              = nombre;
        this.nivelRequerido      = nivelRequerido;
        this.enemigosDisponibles = enemigosDisponibles;
        this.scanner             = sc;
        this.rand                = new Random();
    }

    // Ejecuta la accion principal de la zona (explorar).
    // @param cloud - el jugador
    public abstract void accionZona(Jugador cloud);

    // Verifica si el jugador cumple el nivel minimo para acceder a la zona.
    // @param cloud - el jugador
    // @return true si el nivel de Cloud es mayor o igual al requerido
    public boolean validarAcceso(Jugador cloud) {
        return cloud.getNivel() >= nivelRequerido;
    }

    // Ejecuta un combate por turnos entre Cloud y un grupo de enemigos.
    // Gestiona menus, acciones, turnos enemigos, recompensas y condicion de victoria/derrota.
    // @param cloud        - el jugador
    // @param enemigos     - lista de enemigos a combatir (se modifica al morir cada uno)
    // @param esZonaPeligro - si true aplica penalizacion al morir
    // @param puedeHuir    - si true Cloud puede intentar huir (50% exito)
    // @return true si Cloud gano, false si perdio o huyo
    protected boolean combatir(Jugador cloud, List<Enemigo> enemigos,
                               boolean esZonaPeligro, boolean puedeHuir) {
        System.out.println("\n========== COMBATE INICIADO ==========");
        for (Enemigo e : enemigos) {
            System.out.println("  Aparece: " + e.nombre
                + " (HP: " + e.getStats().getHpActual() + ")");
        }

        while (!enemigos.isEmpty() && cloud.getStats().getHpActual() > 0) {
            mostrarEstadoCombate(cloud, enemigos);

            boolean turnoValido = false;
            while (!turnoValido) {
                int opcion = pedirOpcionCombate(cloud, puedeHuir);

                if (opcion == 1) {
                    // Ataque fisico
                    Enemigo objetivo = primerVivo(enemigos);
                    int dano = cloud.getBusterSword().calcularDanoFisico();
                    objetivo.getStats().recibirDMG(dano);
                    cloud.cargarLimiteInfligido(dano);
                    System.out.println("Cloud ataca a " + objetivo.nombre
                        + " por " + dano + " de dano fisico!");
                    if (objetivo.getStats().getHpActual() <= 0) {
                        manejarMuerteEnemigo(objetivo, cloud);
                        enemigos.remove(objetivo);
                    }
                    turnoValido = true;

                } else if (opcion == 2) {
                    // Ataque magico
                    turnoValido = procesarMagia(cloud, enemigos);

                } else if (opcion == 3) {
                    // Ataque Limite (ya validado >= 100 en pedirOpcionCombate)
                    Enemigo objetivo = primerVivo(enemigos);
                    int dano = cloud.getBusterSword().calcularDanoLimite();
                    objetivo.getStats().recibirDMG(dano);
                    System.out.println("*** Cloud usa ATAQUE LIMITE en " + objetivo.nombre
                        + " por " + dano + " de dano! ***");
                    if (objetivo.getStats().getHpActual() <= 0) {
                        manejarMuerteEnemigo(objetivo, cloud);
                        enemigos.remove(objetivo);
                    }
                    turnoValido = true;

                } else if (opcion == 4) {
                    // Huir
                    if (rand.nextDouble() < 0.5) {
                        System.out.println("Cloud escapa exitosamente!");
                        return false;
                    } else {
                        System.out.println("Cloud no pudo huir y pierde el turno!");
                        turnoValido = true;
                    }
                }
            }

            if (enemigos.isEmpty()) break;

            // Turno de los enemigos
            turnoEnemigos(cloud, enemigos);
        }

        if (cloud.getStats().getHpActual() <= 0) {
            System.out.println("Cloud fue derrotado...");
            if (esZonaPeligro) {
                cloud.aplicarPenalidadMuerte();
            }
            return false;
        }

        System.out.println("========== VICTORIA! ==========\n");
        return true;
    }

    // Muestra el menu de magia, solicita el elemento y aplica el efecto.
    // @param cloud    - el jugador
    // @param enemigos - enemigos activos
    // @return true si se lanzo la magia, false si se cancelo o no fue posible
    private boolean procesarMagia(Jugador cloud, List<Enemigo> enemigos) {
        List<Elemento> disponibles = cloud.getBusterSword().getElementosEquipados();
        if (disponibles.isEmpty()) {
            System.out.println("No tienes materias equipadas en " + cloud.getBusterSword().nombre + "!");
            return false;
        }

        System.out.println("--- Selecciona elemento ---");
        for (int i = 0; i < disponibles.size(); i++) {
            Elemento el   = disponibles.get(i);
            int      costo = cloud.getBusterSword().calcularCostoMP(el);
            String   aviso = (cloud.getStats().getMpActual() >= costo) ? "" : " [Sin MP]";
            System.out.println((i + 1) + ". " + el.name() + " (Costo: " + costo + " MP)" + aviso);
        }
        System.out.println((disponibles.size() + 1) + ". Cancelar");

        int sel = leerOpcion(1, disponibles.size() + 1);
        if (sel == disponibles.size() + 1) return false;

        Elemento elElegido = disponibles.get(sel - 1);
        int      costo     = cloud.getBusterSword().calcularCostoMP(elElegido);

        if (cloud.getStats().getMpActual() < costo) {
            System.out.println("MP insuficiente para lanzar " + elElegido.name() + "!");
            return false;
        }

        cloud.getStats().setMpActual(cloud.getStats().getMpActual() - costo);
        int danoBase = cloud.getBusterSword().calcularDanoMagico(elElegido);

        if (elElegido == Elemento.CURA) {
            Estadisticas s   = cloud.getStats();
            int          cura = Math.min(danoBase, s.getHpMaximo() - s.getHpActual());
            s.setHpActual(s.getHpActual() + cura);
            System.out.println("Cloud usa CURA y recupera " + cura + " HP!  ("
                + s.getHpActual() + "/" + s.getHpMaximo() + ")");
        } else {
            Enemigo objetivo = primerVivo(enemigos);
            double  multi    = 1.0;
            if (objetivo instanceof EnemigoSalvaje) {
                multi = ((EnemigoSalvaje) objetivo).evaluarDebilidad(elElegido);
            }
            int    danoFinal = (int)(danoBase * multi);
            String efecto    = (multi == 2.0) ? " [DEBILIDAD!]"
                             : (multi == 0.5) ? " [Resistencia]"
                             : (multi == 0.0) ? " [INMUNE]" : "";
            objetivo.getStats().recibirDMG(danoFinal);
            cloud.cargarLimiteInfligido(danoFinal);
            System.out.println("Cloud lanza " + elElegido.name() + " en "
                + objetivo.nombre + " por " + danoFinal + " de dano!" + efecto);
            if (objetivo.getStats().getHpActual() <= 0) {
                manejarMuerteEnemigo(objetivo, cloud);
                enemigos.remove(objetivo);
            }
        }
        return true;
    }

    // Ejecuta el ataque del grupo enemigo siguiendo las probabilidades de ataque conjunto.
    // @param cloud    - el jugador objetivo
    // @param enemigos - lista de enemigos vivos
    private void turnoEnemigos(Jugador cloud, List<Enemigo> enemigos) {
        int n = enemigos.size();
        if (n == 0) return;
        if (n == 1) {
            enemigos.get(0).atacar(cloud);
        } else if (n == 2) {
            if (rand.nextDouble() < 0.5) {
                for (Enemigo e : enemigos) e.atacar(cloud);
            } else {
                enemigos.get(rand.nextInt(2)).atacar(cloud);
            }
        } else {
            if (rand.nextDouble() < 0.333) {
                for (Enemigo e : enemigos) e.atacar(cloud);
            } else {
                enemigos.get(rand.nextInt(3)).atacar(cloud);
            }
        }
    }

    // Otorga XP y chatarra al jugador cuando un enemigo muere.
    // @param e     - enemigo derrotado
    // @param cloud - jugador que recibe las recompensas
    private void manejarMuerteEnemigo(Enemigo e, Jugador cloud) {
        System.out.println(e.nombre + " ha sido derrotado!");
        e.giveXpRecompensa(cloud);
        if (e instanceof EnemigoSalvaje) {
            ((EnemigoSalvaje) e).giveChatarraRecompensa(cloud);
        }
    }

    // Imprime el estado actual de HP/MP de Cloud y HP de los enemigos vivos.
    // @param cloud    - el jugador
    // @param enemigos - lista de enemigos activos
    private void mostrarEstadoCombate(Jugador cloud, List<Enemigo> enemigos) {
        System.out.println("------------------------------------------");
        System.out.println("Cloud | HP: " + cloud.getStats().getHpActual()
            + "/" + cloud.getStats().getHpMaximo()
            + "  MP: " + cloud.getStats().getMpActual()
            + "/" + cloud.getStats().getMpMaximo()
            + "  Limite: " + cloud.getLimiteActual() + "/100");
        for (Enemigo e : enemigos) {
            System.out.println("  " + e.nombre + " | HP: "
                + e.getStats().getHpActual() + "/" + e.getStats().getHpMaximo());
        }
        System.out.println("------------------------------------------");
    }

    // Muestra el menu de combate y retorna la opcion valida elegida por el jugador.
    // Si el Limite no esta disponible (< 100), rechaza la opcion 3 y pide de nuevo.
    // @param cloud     - el jugador
    // @param puedeHuir - si true se muestra la opcion de huir como opcion 4
    // @return opcion numerica elegida
    private int pedirOpcionCombate(Jugador cloud, boolean puedeHuir) {
        int maxOp = puedeHuir ? 4 : 3;
        while (true) {
            System.out.println("--- Tu turno ---");
            System.out.println("1. Ataque Fisico");
            System.out.println("2. Magia");
            if (cloud.getLimiteActual() >= 100) {
                System.out.println("3. *** ATAQUE LIMITE ***");
            } else {
                System.out.println("3. Limite (bloqueado " + cloud.getLimiteActual() + "/100)");
            }
            if (puedeHuir) System.out.println("4. Huir");

            int op = leerOpcion(1, maxOp);
            if (op == 3 && cloud.getLimiteActual() < 100) {
                System.out.println("El Ataque Limite todavia no esta disponible!");
                continue;
            }
            return op;
        }
    }

    // Lee un entero en el rango [min, max] desde consola, repite si la entrada es invalida.
    // @param min - valor minimo aceptado
    // @param max - valor maximo aceptado
    // @return entero valido ingresado por el jugador
    protected int leerOpcion(int min, int max) {
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

    // Retorna el primer enemigo con HP > 0 de la lista.
    // @param enemigos - lista de enemigos activos
    // @return primer enemigo vivo
    protected Enemigo primerVivo(List<Enemigo> enemigos) {
        for (Enemigo e : enemigos) {
            if (e.getStats().getHpActual() > 0) return e;
        }
        return null;
    }
}
