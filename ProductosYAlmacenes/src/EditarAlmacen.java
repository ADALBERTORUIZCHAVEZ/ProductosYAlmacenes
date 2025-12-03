
import database.BaseDeDatos;
import database.Sesion;
import java.sql.*;
import javax.swing.*;

public class EditarAlmacen extends javax.swing.JPanel {

    public static final int MODO_AGREGAR = 1;
    public static final int MODO_MODIFICAR = 2;
    public static final int MODO_ELIMINAR = 3;

    private int modo;
    private Integer idAlmacen;  // null si es agregar

    public EditarAlmacen(int modo, Integer idAlmacen) {
        this.modo = modo;
        this.idAlmacen = idAlmacen;

        initComponents();
        configurarSegunModo();

        if ((modo == MODO_MODIFICAR || modo == MODO_ELIMINAR) && idAlmacen != null) {
            cargarDatosAlmacen(idAlmacen);
        }
    }

    public EditarAlmacen() { initComponents(); }

    private void configurarSegunModo() {
        switch (modo) {
            case MODO_AGREGAR -> {
                lblTitulo.setText("Agregar Almacén");
                btnGuardar.setText("Guardar");
            }
            case MODO_MODIFICAR -> {
                lblTitulo.setText("Modificar Almacén");
                btnGuardar.setText("Actualizar");
            }
            case MODO_ELIMINAR -> {
                lblTitulo.setText("Eliminar Almacén");
                btnGuardar.setText("Eliminar");

                txtId.setEnabled(false);
                txtNombre.setEnabled(false);
            }
        }
    }

    private void cargarDatosAlmacen(int id) {
        try (Connection con = BaseDeDatos.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM almacenes WHERE id=?")) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtId.setText(rs.getString("id"));
                txtNombre.setText(rs.getString("nombre"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        lblTitulo = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(0, 82, 158));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(248, 187, 0));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Id");

        txtId.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Nombre");

        txtNombre.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });

        btnGuardar.setBackground(new java.awt.Color(0, 192, 248));
        btnGuardar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setBackground(new java.awt.Color(0, 192, 248));
        btnCancelar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitulo.setText("Editar Almacen");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(156, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblTitulo)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(142, 142, 142))
            .addGroup(layout.createSequentialGroup()
                .addGap(197, 197, 197)
                .addComponent(btnGuardar)
                .addGap(65, 65, 65)
                .addComponent(btnCancelar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(99, 99, 99)
                .addComponent(lblTitulo)
                .addGap(91, 91, 91)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(115, 115, 115)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 152, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        VentanaPrincipal ventana = (VentanaPrincipal) SwingUtilities.getWindowAncestor(this);

        // ELIMINAR
        if (modo == MODO_ELIMINAR) {
            int opcion = JOptionPane.showConfirmDialog(this,
                    "¿Seguro que deseas eliminar este almacén?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {
                try (Connection con = BaseDeDatos.getConnection();
                    PreparedStatement ps = con.prepareStatement("DELETE FROM almacenes WHERE id=?")) {

                    ps.setInt(1, idAlmacen);
                    ps.executeUpdate();
                } catch (Exception e) { e.printStackTrace(); }

                JOptionPane.showMessageDialog(this, "Almacén eliminado.");
                ventana.mostrarPanel(new FormularioAlmacenes(ventana));
            }
            return;
        }

        // VALIDACIONES
        if (txtId.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
            return;
        }

        String id = txtId.getText().trim();
        String nombre = txtNombre.getText();
        String usuario = Sesion.usuarioActual;

        // AGREGAR
        if (modo == MODO_AGREGAR) {
            try (Connection con = BaseDeDatos.getConnection();
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO almacenes(nombre, fecha_hora_creacion, fecha_hora_ultima_modificacion, ultimo_usuario_en_modificar) " +
                    "VALUES (?, datetime('now','localtime'), datetime('now','localtime'), ?)")) {

                ps.setString(1, nombre);
                ps.setString(2, usuario);
                ps.executeUpdate();

            } catch (Exception e) { e.printStackTrace(); }

            JOptionPane.showMessageDialog(this, "Almacén agregado.");
        }

        // MODIFICAR
        else if (modo == MODO_MODIFICAR) {
            try (Connection con = BaseDeDatos.getConnection();
                PreparedStatement ps = con.prepareStatement(
                    "UPDATE almacenes SET nombre=?, fecha_hora_ultima_modificacion=datetime('now','localtime'), ultimo_usuario_en_modificar=? WHERE id=?")) {

                ps.setString(1, nombre);
                ps.setString(2, usuario);
                ps.setInt(3, idAlmacen);
                ps.executeUpdate();

            } catch (Exception e) { e.printStackTrace(); }

            JOptionPane.showMessageDialog(this, "Almacén actualizado.");
        }

        ventana.mostrarPanel(new FormularioAlmacenes(ventana));
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        VentanaPrincipal ventana = (VentanaPrincipal) SwingUtilities.getWindowAncestor(this);
        ventana.mostrarPanel(new FormularioAlmacenes(ventana));
    }//GEN-LAST:event_btnCancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
