public class App {
    public static void main(String[] args) {

        //  Crear Jugadores 
        Jugador j1 = new Guerrero("Arthas");
        Jugador j2 = new Explorador("Lara");
        Jugador j3 = new Explorador("Ezio");

        //  Crear Enemigos 
        Enemigo e1 = new Orco(false);
        Enemigo e2 = new Esqueleto(false);
        Enemigo e3 = new Orco(true);

        //  Listas 
        java.util.List<Jugador> jugadores = java.util.Arrays.asList(j1, j2, j3);
        java.util.List<Enemigo> enemigos = java.util.Arrays.asList(e1, e2, e3);

        //  Controlador ==
        ControladorBatalla controlador = new ControladorBatalla(jugadores, enemigos);

        //  Arrancar batalla 
        controlador.iniciarBatalla();

        //  Fin del juego 
        System.out.println("Juego terminado. ¡Gracias por jugar!");
    }
}
