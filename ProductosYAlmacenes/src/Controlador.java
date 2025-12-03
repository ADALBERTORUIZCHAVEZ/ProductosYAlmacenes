import database.BaseDeDatos;
import database.Sesion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Controlador {
    public static boolean iniciarSesion(String usuario, String password) {
        try (Connection con = BaseDeDatos.getConnection()) {

            String sql = "SELECT nombre, password, rol FROM usuarios WHERE nombre = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, usuario);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String passwordEnBD = rs.getString("password");
                String rol = rs.getString("rol");

                // Encriptar la contraseña ingresada
                String passwordEncriptada = BaseDeDatos.md5(password);

                // Comparar contraseñas
                if (passwordEncriptada.equals(passwordEnBD)) {

                    // Guardar usuario y rol en la sesión
                    Sesion.usuarioActual = usuario;
                    Sesion.rolActual = rol;

                    // Actualizar fecha y hora del último inicio
                    actualizarFechaInicioSesion(usuario);

                    return true; // Inicio de sesión correcto
                }
            }

        } catch (Exception e) {
            System.err.println("Error en iniciarSesion(): " + e.getMessage());
        }

        return false; // Usuario o contraseña inválidos
    }


    /**
     * Actualiza la fecha y hora del último inicio de sesión.
     */
    private static void actualizarFechaInicioSesion(String usuario) {
        try (Connection con = BaseDeDatos.getConnection()) {

            String sql = "UPDATE usuarios SET fecha_hora_ultimo_inicio = DATETIME('now') WHERE nombre = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("Error al actualizar fecha_hora_ultimo_inicio: " + e.getMessage());
        }
    }
}
