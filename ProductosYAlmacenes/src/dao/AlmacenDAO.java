
package dao;

import database.BaseDeDatos;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class AlmacenDAO {
    public static DefaultTableModel obtenerAlmacenes() {

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Creación");
        modelo.addColumn("Última Modificación");
        modelo.addColumn("Modificado por");

        String sql = """
            SELECT id, nombre,
                   fecha_hora_creacion,
                   fecha_hora_ultima_modificacion,
                   ultimo_usuario_en_modificar
            FROM almacenes;
        """;

        try (Connection con = BaseDeDatos.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("fecha_hora_creacion"),
                    rs.getString("fecha_hora_ultima_modificacion"),
                    rs.getString("ultimo_usuario_en_modificar")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelo;
    }
    
}
