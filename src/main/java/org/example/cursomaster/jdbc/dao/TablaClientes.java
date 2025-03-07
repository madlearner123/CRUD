package org.example.cursomaster.jdbc.dao;

import org.example.cursomaster.jdbc.modulos.Cliente;
import org.example.cursomaster.jdbc.utils.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.DriverManager.getConnection;

public class TablaClientes implements Tabla<Cliente>, Tabla2<Cliente> {

    private static final String NOMBRE_TABLA = "CLIENTES";

    private static Connection getConnection() throws SQLException {
        return Conexion.getInstance();
    }

    @Override
    public void mostrar(){
        System.out.println("**" + "*".repeat(NOMBRE_TABLA.length()) + "**");
        System.out.println("* " + NOMBRE_TABLA + " *");
        System.out.println("**" + "*".repeat(NOMBRE_TABLA.length()) + "**");
        System.out.println(Cliente.cabecera());
        this.listar().forEach(System.out::println);
    }

    @Override
    public List<Cliente> listar() {
        List<Cliente> clientes = new ArrayList<>();

        try(
                PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM " + NOMBRE_TABLA);
                ResultSet rs = statement.executeQuery();
        ){
            clientes = this.obtenerClientes(rs);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }

        return clientes;
    }

    @Override
    public void guardar(Cliente cliente) {
        final String updateCliente = "UPDATE " + NOMBRE_TABLA + " SET NOMBRE = ?, EMAIL = ?, TELEFONO = ? WHERE ID = ?";
        final String insertCliente = "INSERT INTO " + NOMBRE_TABLA + " (NOMBRE, EMAIL, TELEFONO, FECHA_REGISTRO) VALUES (?, ?, ?, ?)";
        String sql = cliente.getId() != null ? updateCliente : insertCliente;
        try(
                PreparedStatement statement = getConnection().prepareStatement(sql)
        ) {
            statement.setString(1, cliente.getNombre());
            statement.setString(2, cliente.getEmail());
            statement.setString(3, cliente.getTelefono());
            if(cliente.getId() == null) statement.setTimestamp(4, Timestamp.valueOf(cliente.getFechaRegistro()));
            else statement.setInt(4, cliente.getId());
            statement.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Integer id) {

        try(
                PreparedStatement statement = getConnection().prepareStatement(
                    "DELETE FROM " + NOMBRE_TABLA + " WHERE id = ?"
                )
        ) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Cliente buscarPorId(Integer id) {
        Cliente c = new Cliente();
        try(
                PreparedStatement statement = getConnection().prepareStatement(
                        "SELECT * FROM " + NOMBRE_TABLA +
                            " WHERE id = ?;"
                        );
        ){
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if(rs.next()) c = this.obtenerCliente(rs);
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return c;
    }

    private Cliente obtenerCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getInt("id"));
        c.setNombre(rs.getString("nombre"));
        c.setEmail(rs.getString("email"));
        c.setTelefono(rs.getString("telefono"));
        c.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
        return c;
    }

    private List<Cliente> obtenerClientes(ResultSet rs) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        while(rs.next()) clientes.add(this.obtenerCliente(rs));
        return clientes;
    }

    @Override
    public Optional<Cliente> obtenerClienteConPedidoMasCaro() {
        Optional<Cliente> c = Optional.of(new Cliente());
        try(
                PreparedStatement stmt = getConnection().prepareStatement(
                "SELECT *\n" +
                    "FROM CLIENTES\n" +
                    "WHERE ID IN (\n" +
                    "\tSELECT CLIENTE_ID\n" +
                    "    FROM PEDIDOS\n" +
                    "    WHERE TOTAL = (\n" +
                    "\t\tSELECT MAX(TOTAL)\n" +
                    "        FROM PEDIDOS\n" +
                    "    )\n" +
                    ");"
                )
        ){
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()) c = Optional.of(this.obtenerCliente(rs));
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return c;
    }

    @Override
    public Optional<List<Cliente>> algunClienteConMasDeNPedidos(int n) {
        Optional<List<Cliente>> clientes = Optional.of(new ArrayList<Cliente>());
        try(
                PreparedStatement stmt = getConnection().prepareStatement(
                "SELECT C.ID, C.NOMBRE, C.EMAIL, C.TELEFONO, C.FECHA_REGISTRO\n" +
                    "FROM CLIENTES C\n" +
                    "JOIN PEDIDOS P\n" +
                    "ON C.ID = P.CLIENTE_ID\n" +
                    "GROUP BY C.ID, C.NOMBRE, C.EMAIL, C.TELEFONO, C.FECHA_REGISTRO\n" +
                    "HAVING COUNT(P.ID) > ?;"
                )
        ){
            stmt.setInt(1, n);
            try(ResultSet rs = stmt.executeQuery()){
                clientes = Optional.of(this.obtenerClientes(rs));
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return clientes;
    }

    @Override
    public Optional<Double> obtenerTotalGastadoPorCliente(int id) {
        Optional<Double> gasto = Optional.of(0.0);
        try(
            PreparedStatement stmt = getConnection().prepareStatement(
            "SELECT CLIENTE_ID, SUM(TOTAL) GASTO\n" +
                "FROM PEDIDOS\n" +
                "GROUP BY CLIENTE_ID\n" +
                "HAVING CLIENTE_ID = ?;"
            )
        ){
            stmt.setInt(1, id);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next())
                    gasto = Optional.of(rs.getDouble("gasto"));
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return gasto;
    }

    @Override
    public List<String> nombresQueEmpiezanPorLetra(char letra) {
        return List.of();
    }
}
