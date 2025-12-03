package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;
import java.security.MessageDigest;

public class BaseDeDatos {
    
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Error cargando driver SQLite: " + e.getMessage());
        }
    }
    
    private static final String url = "jdbc:sqlite:C:\\Users\\DELL\\Documents\\NetBeansProjects\\ProductosYAlmacenes\\InventarioBD_2.db";

    public static void inicializarBD() {
        try (Connection con = getConnection()) {
            System.out.println("BD en uso: " + url.substring("jdbc:sqlite:".length()) + " en ruta: " + new java.io.File(url.substring("jdbc:sqlite:".length())).getAbsolutePath());
            System.out.println("Conexión establecida con la base de datos.");

            crearTablaUsuarios(con);
            crearTablaAlmacenes(con);
            crearTablaProductos(con); // crea si no existe
            insertarUsuariosBase(con);
            agregarColumna(con, "productos", "almacen INTEGER"); // agregar columna almacen si falta

            // agregar columnas de auditoría
            agregarColumnasProductos(con);
            agregarColumnasAlmacenes(con);

            System.out.println("✓ Base de datos lista.");
        } catch (Exception e) {
            System.err.println("✗ Error inicializando BD: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(url);

            // ACTIVAR WAL Y TIMEOUT (evita database locked)
            try (Statement st = conn.createStatement()) {
                st.execute("PRAGMA journal_mode = WAL;");
                st.execute("PRAGMA busy_timeout = 5000;");
                st.close();
            }

            return conn;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // tablas
    private static void crearTablaProductos(Connection con) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS productos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                cantidad INTEGER NOT NULL,
                precio REAL NOT NULL,
                departamento TEXT NOT NULL,
                almacen INTEGER NOT NULL,
                fecha_hora_creacion TEXT DEFAULT (datetime('now','localtime')),
                fecha_hora_ultima_modificacion TEXT,
                ultimo_usuario_en_modificar TEXT,
                FOREIGN KEY (almacen) REFERENCES almacenes(id_almacen)
            );
            """;
        try (Statement st = con.createStatement()) {
            st.execute(sql);
            System.out.println("✓ Tabla 'productos' creada (si no existía).");
        }
    }

    private static void crearTablaAlmacenes(Connection con) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS almacenes (
                id INTEGER PRIMARY KEY,
                nombre TEXT NOT NULL UNIQUE
            );
            """;
        try (Statement st = con.createStatement()) {
            st.execute(sql);
            System.out.println("✓ Tabla 'almacenes' creada (si no existía).");
        }
    }

    private static void crearTablaUsuarios(Connection con) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS usuarios (
                        nombre TEXT PRIMARY KEY,
                        password TEXT NOT NULL,
                        rol TEXT CHECK(rol IN ('ADMIN','PRODUCTOS','ALMACENES')),
                        fecha_hora_ultimo_inicio TEXT
            );
            """;
        try (Statement st = con.createStatement()) {
            st.execute(sql);
        }
    }
    
    public static boolean insertarProducto(String nombre, int cantidad, double precio, String departamento, int idAlmacen, String usuario) {

    String sql = """
        INSERT INTO productos(
            nombre, cantidad, precio, departamento, almacen,
            fecha_hora_creacion, fecha_hora_ultima_modificacion, ultimo_usuario_en_modificar
        )
        VALUES (?, ?, ?, ?, ?, datetime('now','localtime'), datetime('now','localtime'), ?)
    """;

    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, nombre);
        ps.setInt(2, cantidad);
        ps.setDouble(3, precio);
        ps.setString(4, departamento);
        ps.setInt(5, idAlmacen);
        ps.setString(6, usuario);

        ps.executeUpdate();
        return true;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
    
    public static boolean actualizarProducto(int id, String nombre, int cantidad, double precio, String departamento, int idAlmacen, String usuario) {

    String sql = """
        UPDATE productos SET
            nombre = ?,
            cantidad = ?,
            precio = ?,
            departamento = ?,
            almacen = ?,
            fecha_hora_ultima_modificacion = datetime('now','localtime'),
            ultimo_usuario_en_modificar = ?
        WHERE id = ?
    """;

    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, nombre);
        ps.setInt(2, cantidad);
        ps.setDouble(3, precio);
        ps.setString(4, departamento);
        ps.setInt(5, idAlmacen);
        ps.setString(6, usuario);
        ps.setInt(7, id);

        ps.executeUpdate();
        return true;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
    
    public static boolean eliminarProducto(int idProducto) {
    String sql = "DELETE FROM productos WHERE id = ?";

    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, idProducto);
        ps.executeUpdate();
        return true;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

    private static void insertarUsuariosBase(Connection con) throws Exception {
        if (existeUsuario(con, "ADMIN")) return;
        String sql = "INSERT INTO usuarios(nombre, password, rol) VALUES(?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "ADMIN");
            ps.setString(2, md5("admin23"));
            ps.setString(3, "ADMIN");
            ps.executeUpdate();

            ps.setString(1, "PRODUCTOS");
            ps.setString(2, md5("productos19"));
            ps.setString(3, "PRODUCTOS");
            ps.executeUpdate();

            ps.setString(1, "ALMACENES");
            ps.setString(2, md5("almacenes11"));
            ps.setString(3, "ALMACENES");
            ps.executeUpdate();

            System.out.println("✓ Usuarios base insertados.");
        }
    }

    private static boolean existeUsuario(Connection con, String nombre) throws SQLException {
        String sql = "SELECT nombre FROM usuarios WHERE nombre=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    // columnas de auditoría
    private static void agregarColumnasProductos(Connection con) throws SQLException {
        agregarColumna(con, "productos", "fecha_hora_creacion TEXT "); // MODIFICADO: Eliminado DEFAULT (DATETIME('now'))
        agregarColumna(con, "productos", "fecha_hora_ultima_modificacion TEXT");
        agregarColumna(con, "productos", "ultimo_usuario_en_modificar TEXT");
        System.out.println("✓ Columnas extra añadidas a productos.");
    }

    private static void agregarColumnasAlmacenes(Connection con) throws SQLException {
        agregarColumna(con, "almacenes", "fecha_hora_creacion TEXT "); // MODIFICADO: Eliminado DEFAULT (DATETIME('now'))
        agregarColumna(con, "almacenes", "fecha_hora_ultima_modificacion TEXT");
        agregarColumna(con, "almacenes", "ultimo_usuario_en_modificar TEXT");
        System.out.println("✓ Columnas extra añadidas a almacenes.");
    }

    private static void agregarColumna(Connection con, String tabla, String definicionColumna)
            throws SQLException {
        String nombreColumna = definicionColumna.split(" ")[0].replaceAll("[^a-zA-Z0-9_]", "");
        if (!columnaExiste(con, tabla, nombreColumna)) {
            String sql = "ALTER TABLE " + tabla + " ADD COLUMN " + definicionColumna;
            try (Statement st = con.createStatement()) {
                st.execute(sql);
            }
        }
    }

    private static boolean columnaExiste(Connection con, String tabla, String columna) throws SQLException {
        String sql = "PRAGMA table_info(" + tabla + ")";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                if (rs.getString("name").equalsIgnoreCase(columna)) return true;
            }
        }
        return false;
    }

    // utilidad: insertar datos de prueba
    /*public static void insertarDatosPrueba() {
        try (Connection con = getConnection()) {
            // agrega un par de almacenes si no existen
            String inAlm = "INSERT OR IGNORE INTO almacenes(id_almacen, nombre, ubicacion) VALUES(?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(inAlm)) {
                ps.setInt(1, 1); ps.setString(2, "Almacén Central"); ps.setString(3, "Centro"); ps.executeUpdate();
                ps.setInt(1, 2); ps.setString(2, "Bodega Norte"); ps.setString(3, "Col. Industrial"); ps.executeUpdate();
            }
            // agrega productos de ejemplo
            String inProd = "INSERT OR IGNORE INTO productos(codigo, nombre, descripcion, precio, stock, almacen) VALUES(?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(inProd)) {
                ps.setString(1, "P001"); ps.setString(2, "Laptop Lenovo"); ps.setString(3, "Laptop 14\""); ps.setDouble(4, 14500.0); ps.setInt(5, 10); ps.setInt(6, 1); ps.executeUpdate();
                ps.setString(1, "P002"); ps.setString(2, "Mouse Inalámbrico"); ps.setString(3, "Óptico"); ps.setDouble(4, 250.0); ps.setInt(5, 50); ps.setInt(6, 2); ps.executeUpdate();
            }
            System.out.println("✓ Datos de prueba insertados.");
        } catch (Exception e) {
            System.err.println("Error insertando datos de prueba: " + e.getMessage());
        }
    }*/
    
    public static Usuario buscarUsuario(String nombre, String passwordMd5) {
    String sql = "SELECT nombre, rol FROM usuarios WHERE nombre = ? AND password = ?";

    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, nombre);
        ps.setString(2, passwordMd5);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new Usuario(rs.getString("nombre"), rs.getString("rol"));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}
    
    public static boolean actualizarUltimoInicio(String usuario) {
    String sql = "UPDATE usuarios SET fecha_hora_ultimo_inicio = datetime('now','localtime') WHERE nombre = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
}

    // MD5
    public static String md5(String input) {
        try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    } catch (Exception e) {
        return null;
    }
    }
}
