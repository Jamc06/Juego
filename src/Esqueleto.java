public class Esqueleto extends Enemigo {
    public Esqueleto(boolean jefe) {
        super(jefe ? "Esqueleto Jefe" : "Esqueleto", jefe ? 70 : 45, jefe ? 18 : 9, jefe);
    }
}

