public class Orco extends Enemigo {
    public Orco(boolean jefe) {
        super(jefe ? "Orco Jefe" : "Orco", jefe ? 90 : 60, jefe ? 25 : 12, jefe);
    }
}
