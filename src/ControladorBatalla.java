import java.util.*;
import java.util.stream.Collectors;

public class ControladorBatalla {

    private List<Jugador> jugadores;
    private List<Enemigo> enemigos;
    private Map<Jugador, List<Item>> inventarioJugador;
    private Map<Enemigo, List<Item>> inventarioEnemigos;

    // Registro de eventos por turno y global
    private List<String> eventosTurno = new ArrayList<>();
    private LinkedList<String> registroAcciones = new LinkedList<>();

    private Scanner sc = new Scanner(System.in);

    public ControladorBatalla(List<Jugador> jugadores, List<Enemigo> enemigos) {
        this.jugadores = jugadores;
        this.enemigos = enemigos;

        inventarioJugador = new HashMap<>();
        inventarioEnemigos = new HashMap<>();

        // Pociones para jugadores
        for (Jugador j : jugadores) {
            List<Item> items = new ArrayList<>();
            if (j instanceof Explorador) {
                // Explorador tiene más pociones
                items.add(new PocionVida("Poción Vida", 30));
                items.add(new PocionVida("Poción Vida Extra", 30));
                items.add(new PocionDamage("Poción Daño", 15));
                items.add(new PocionDamage("Poción Daño Extra", 15));
                items.add(new PocionQuitarEfectos("Poción Antídoto"));
            } else {
                items.add(new PocionVida("Poción Vida", 30));
                items.add(new PocionDamage("Poción Daño", 15));
                items.add(new PocionQuitarEfectos("Poción Antídoto"));
            }
            inventarioJugador.put(j, items);
        }

        // Pociones para enemigos
        for (Enemigo e : enemigos) {
            List<Item> items = new ArrayList<>();
            items.add(new PocionVida("Poción Vida", 20));
            items.add(new PocionDamage("Poción Daño", 10));
            items.add(new PocionQuitarEfectos("Poción Antídoto"));
            inventarioEnemigos.put(e, items);
        }
    }

    // === MÉTODO PRINCIPAL ===
    public void iniciarBatalla() {
        System.out.println("=== ¡Comienza la batalla 3v3! ===");
        for (Personaje p : jugadores) p.mensajeInicio();
        for (Personaje p : enemigos) p.mensajeInicio();

        while (hayJugadoresVivos() && hayEnemigosVivos()) {
            aplicarVeneno();
            mostrarEstado();

            // Turno intercalado jugador-enemigo
            for (int i = 0; i < 3; i++) {
                if (i < jugadores.size()) {
                    Jugador j = jugadores.get(i);
                    if (j.estaVivo()) turnoJugador(j);
                }
                if (i < enemigos.size()) {
                    Enemigo e = enemigos.get(i);
                    if (e.estaVivo()) turnoEnemigo(e);
                }
                limpiarMuertos();
            }

            finTurno();
        }

        System.out.println("\n=== Resultado Final ===");
        mostrarEstado();
        if (hayJugadoresVivos()) System.out.println("¡Los jugadores han ganado!");
        else System.out.println("Los enemigos han ganado...");
    }

    // === ESTADO Y TURNOS ===
    private boolean hayJugadoresVivos() {
        return jugadores.stream().anyMatch(Personaje::estaVivo);
    }

    private boolean hayEnemigosVivos() {
        return enemigos.stream().anyMatch(Personaje::estaVivo);
    }

    private void mostrarEstado() {
        System.out.println("\n--- Estado Actual ---");
        System.out.println("Jugadores:");
        for (Jugador j : jugadores) System.out.println(j);
        System.out.println("Enemigos:");
        for (Enemigo e : enemigos) System.out.println(e);
        System.out.println("\nÚltimas acciones:");
        for (String a : registroAcciones) System.out.println(a);
        System.out.println("----------------------");
    }

    // === TURNOS ===
    private void turnoJugador(Jugador j) {
        System.out.println("\nTurno de " + j.getNombre());
        System.out.println("1. Atacar");
        System.out.println("2. Usar Poción");
        System.out.println("3. Retirarse");
        int op = leerOpcion(1, 3);

        switch (op) {
            case 1:
                atacar(j, enemigos);
                break;
            case 2:
                usarPocionJugador(j);
                break;
            case 3:
                j.recibirDanio(j.getVida()); // HP 0
                registrarEventoTurno(j.getNombre() + " se retira del combate");
                break;
        }
    }

    private void turnoEnemigo(Enemigo e) {
        double porcentajeVida = (double) e.getVida() / e.getVidaMax();
        if (porcentajeVida > 0.40) {
            atacar(e, jugadores);
        } else {
            List<Item> items = inventarioEnemigos.get(e);
            boolean tienePocionVida = items.stream().anyMatch(it -> it instanceof PocionVida);
            if (tienePocionVida) {
                usarPocionEnemigo(e);
            } else {
                habilidadEspecial(e);
            }
        }
    }

    // === ACCIONES ===
    private void atacar(Personaje atacante, List<? extends Personaje> objetivos) {
        List<Personaje> vivos = objetivos.stream()
                .filter(Personaje::estaVivo)
                .collect(Collectors.toList());

        if (vivos.isEmpty()) return;

        Personaje objetivo;
        if (atacante instanceof Jugador) {
            System.out.println("Elige objetivo:");
            for (int i = 0; i < vivos.size(); i++) {
                System.out.println((i + 1) + ". " + vivos.get(i));
            }
            int sel = leerOpcion(1, vivos.size());
            objetivo = vivos.get(sel - 1);
        } else {
            objetivo = vivos.get(new Random().nextInt(vivos.size()));
        }

        int vidaAntes = objetivo.getVida();
        objetivo.recibirDanio(atacante.getAtaque());

        // Veneno de Orco (50%)
        if (atacante instanceof Orco) {
            if (new Random().nextBoolean()) {
                objetivo.envenenar();
                registrarEventoTurno(atacante.getNombre() + " ha envenenado a " + objetivo.getNombre());
            }
        }

        registrarEventoTurno(atacante.getNombre() + " ataca a " + objetivo.getNombre() +
                " [" + vidaAntes + " → " + objetivo.getVida() + "]");

        if (!objetivo.estaVivo()) {
            registrarEventoTurno(objetivo.getNombre() + " ha caído en batalla.");
            objetivo.mensajeFinal(false);
        }
    }

    private void usarPocionJugador(Jugador j) {
        List<Item> items = inventarioJugador.get(j);
        if (items.isEmpty()) {
            System.out.println("Sin pociones disponibles.");
            return;
        }
        System.out.println("Elige poción:");
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ". " + items.get(i).getNombre());
        }
        int sel = leerOpcion(1, items.size());
        Item pocion = items.remove(sel - 1);

        // Elegir objetivo (jugador o enemigo)
        List<Personaje> todos = new ArrayList<>();
        todos.addAll(jugadores);
        todos.addAll(enemigos);
        System.out.println("Elige objetivo para " + pocion.getNombre() + ":");
        for (int i = 0; i < todos.size(); i++) {
            System.out.println((i + 1) + ". " + todos.get(i));
        }
        int tar = leerOpcion(1, todos.size());
        Personaje objetivo = todos.get(tar - 1);

        pocion.aplicar(objetivo);
        registrarEventoTurno(j.getNombre() + " usa " + pocion.getNombre() + " en " + objetivo.getNombre());
    }

    private void usarPocionEnemigo(Enemigo e) {
        List<Item> items = inventarioEnemigos.get(e);
        if (items.isEmpty()) {
            habilidadEspecial(e);
            return;
        }

        // Buscar poción de vida primero
        Item pocionVida = null;
        for (Item it : items) {
            if (it instanceof PocionVida) {
                pocionVida = it;
                break;
            }
        }

        if (pocionVida != null) {
            items.remove(pocionVida);
            pocionVida.aplicar(e);
            registrarEventoTurno(e.getNombre() + " usa " + pocionVida.getNombre() + " en sí mismo");
        } else {
            Item pocion = items.remove(0);
            pocion.aplicar(e);
            registrarEventoTurno(e.getNombre() + " usa " + pocion.getNombre() + " en sí mismo");
        }
    }

    private void habilidadEspecial(Enemigo e) {
        // placeholder habilidad especial
        registrarEventoTurno(e.getNombre() + " usa su habilidad especial (placeholder)");
    }

    private void limpiarMuertos() {
        for (Personaje p : jugadores) {
            if (!p.estaVivo()) p.mensajeFinal(false);
        }
        for (Personaje p : enemigos) {
            if (!p.estaVivo()) p.mensajeFinal(false);
        }
    }

    // === VENENO ===
    private void aplicarVeneno() {
        for (Jugador j : jugadores) {
            if (j.estaVivo() && j.isEnvenenado()) {
                int danio = Math.max(1, (int)(j.getVida() * 0.25));
                int vidaAntes = j.getVida();
                j.recibirDanio(danio);
                registrarEventoTurno(j.getNombre() + " sufre " + danio +
                        " de daño por VENENO [" + vidaAntes + " → " + j.getVida() + "]");
            }
        }

        for (Enemigo e : enemigos) {
            if (e.estaVivo() && e.isEnvenenado()) {
                int danio = Math.max(1, (int)(e.getVida() * 0.25));
                int vidaAntes = e.getVida();
                e.recibirDanio(danio);
                registrarEventoTurno(e.getNombre() + " sufre " + danio +
                        " de daño por VENENO [" + vidaAntes + " → " + e.getVida() + "]");
            }
        }
    }

    // === REGISTROS ===
    private void registrarEventoTurno(String texto) {
        eventosTurno.add(texto);
    }

    private void finTurno() {
        System.out.println("\n=== Eventos del turno ===");
        for (String ev : eventosTurno) {
            System.out.println(ev);
            registroAcciones.add(ev);
            if (registroAcciones.size() > 10)
                registroAcciones.removeFirst();
        }
        System.out.println("=========================\n");
        eventosTurno.clear();
    }

    // === INPUT ===
    private int leerOpcion(int min, int max) {
        int op;
        while (true) {
            try {
                System.out.print("Opción: ");
                op = Integer.parseInt(sc.nextLine());
                if (op >= min && op <= max) break;
            } catch (Exception ignored) {}
        }
        return op;
    }
}
