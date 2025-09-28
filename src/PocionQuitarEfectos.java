public class PocionQuitarEfectos extends Item {
    public PocionQuitarEfectos(String nombre) {
        super(nombre, "Elimina todos los efectos negativos en el objetivo");
    }

    @Override
    public void aplicar(Personaje objetivo) {
        // Ahora sí quita veneno real
        objetivo.curarVeneno(); // método nuevo en Personaje
        System.out.println(nombre + " usada en " + objetivo.getNombre() +
                           " → Se han eliminado los efectos negativos (veneno)");
    }
}
