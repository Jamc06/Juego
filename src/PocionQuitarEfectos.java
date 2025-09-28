public class PocionQuitarEfectos extends Item {
    public PocionQuitarEfectos(String nombre) {
        super(nombre, "Elimina todos los efectos negativos en el objetivo");
    }

    @Override
    public void aplicar(Personaje objetivo) {
        
        objetivo.curarVeneno(); // Elimina el efecto de veneno
        System.out.println(nombre + " usada en " + objetivo.getNombre() +
                           " → Se han eliminado los efectos negativos (veneno)");
    }
}
