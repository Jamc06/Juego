public class App {
    public static void main(String[] args) {

        //Creacion de jugadores y enemigos
        Jugador j1 = new Guerrero("Arthas");
        Jugador j2 = new Explorador("Lara");
        Jugador j3 = new Explorador("Ezio");

       
        Enemigo e1 = new Orco(false);
        Enemigo e2 = new Esqueleto(false);
        Enemigo e3 = new Orco(true);

        
        java.util.List<Jugador> jugadores = java.util.Arrays.asList(j1, j2, j3);
        java.util.List<Enemigo> enemigos = java.util.Arrays.asList(e1, e2, e3);

        
        ControladorBatalla controlador = new ControladorBatalla(jugadores, enemigos);

        
        controlador.iniciarBatalla();

        // === Fin del juego ===
        System.out.println("Termino el juego. Gracias por jugar");
    }
}
