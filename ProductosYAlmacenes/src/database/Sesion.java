package database;

public class Sesion {
    public static String usuarioActual = null;
    public static String rolActual = null;

    public static void cerrarSesion() {
        usuarioActual = null;
        rolActual = null;
    }
}
