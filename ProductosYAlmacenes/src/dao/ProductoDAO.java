
package dao;

import database.BaseDeDatos;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class ProductoDAO {
    
    public static DefaultTableModel obtenerProductos() {
        /*DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"Código","Nombre","Descripción","Precio","Stock","Almacén","Creación","Última Mod.","Usuario"}, 0);
        String sql = """
            SELECT p.codigo, p.nombre, p.descripcion, p.precio, p.stock,
                   a.nombre AS almacenNombre,
                   p.fecha_hora_creacion,
                   p.fecha_hora_ultima_modificacion,
                   p.ultimo_usuario_en_modificar
            FROM productos p
            LEFT JOIN almacenes a ON p.almacen = a.id_almacen
            ORDER BY p.nombre;
        """;*/
        
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"Id","Nombre","Precio","Stock","Departamento", "Almacén","Creación","Última Mod.","Usuario"}, 0);
        String sql = """
            SELECT p.id, p.nombre, p.precio, p.cantidad, p.departamento,
                   a.nombre AS almacenNombre,
                   p.fecha_hora_creacion,
                   p.fecha_hora_ultima_modificacion,
                   p.ultimo_usuario_en_modificar
            FROM productos p
            LEFT JOIN almacenes a ON p.almacen = a.id
            ORDER BY p.id;
        """;
        /*try (Connection con = BaseDeDatos.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getInt("stock"),
                    rs.getString("almacenNombre"),
                    rs.getString("fecha_hora_creacion"),
                    rs.getString("fecha_hora_ultima_modificacion"),
                    rs.getString("ultimo_usuario_en_modificar")
                });
            }
        } catch (Exception e) {
            System.err.println("Error obtenerProductos: " + e.getMessage());
        }*/
        
        try (Connection con = BaseDeDatos.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("Id"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("cantidad"),
                    rs.getString("Departamento"),
                    rs.getString("almacenNombre"),
                    rs.getString("fecha_hora_creacion"),
                    rs.getString("fecha_hora_ultima_modificacion"),
                    rs.getString("ultimo_usuario_en_modificar")
                });
            }
        } catch (Exception e) {
            System.err.println("Error obtenerProductos: " + e.getMessage());
        }
        return modelo;
    }

    public static boolean insertarProducto(String codigo, String nombre, String descripcion, double precio, int stock, Integer idAlmacen, String usuario) {
        String sql = "INSERT INTO productos(codigo, nombre, descripcion, precio, stock, almacen, fecha_hora_creacion, ultimo_usuario_en_modificar) VALUES(?,?,?,?,?,?,DATETIME('now'),?)";
        try (Connection con = BaseDeDatos.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ps.setString(2, nombre);
            ps.setString(3, descripcion);
            ps.setDouble(4, precio);
            ps.setInt(5, stock);
            if (idAlmacen == null) ps.setNull(6, Types.INTEGER); else ps.setInt(6, idAlmacen);
            ps.setString(7, usuario);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Error insertarProducto: " + e.getMessage());
            return false;
        }
    }

    public static boolean actualizarProducto(String codigo, String nombre, String descripcion, double precio, int stock, Integer idAlmacen, String usuario) {
        String sql = "UPDATE productos SET nombre=?, descripcion=?, precio=?, stock=?, almacen=?, fecha_hora_ultima_modificacion=DATETIME('now'), ultimo_usuario_en_modificar=? WHERE codigo=?";
        try (Connection con = BaseDeDatos.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setDouble(3, precio);
            ps.setInt(4, stock);
            if (idAlmacen == null) ps.setNull(5, Types.INTEGER); else ps.setInt(5, idAlmacen);
            ps.setString(6, usuario);
            ps.setString(7, codigo);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Error actualizarProducto: " + e.getMessage());
            return false;
        }
    }

    public static boolean eliminarProducto(String codigo) {
        String sql = "DELETE FROM productos WHERE codigo=?";
        try (Connection con = BaseDeDatos.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Error eliminarProducto: " + e.getMessage());
            return false;
        }
    }
}
