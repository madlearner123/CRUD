package org.example.cursomaster.jdbc.dao;

import org.example.cursomaster.jdbc.modulos.Pedido;
import org.example.cursomaster.jdbc.utils.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TablaPedidos implements Tabla<Pedido> {

    private static final String NOMBRE_TABLA = "PEDIDOS";

    private static Connection getConnection() throws SQLException {
        return Conexion.getInstance();
    }

    @Override
    public void mostrar(){
        System.out.println("**" + "*".repeat(NOMBRE_TABLA.length()) + "**");
        System.out.println("* " + NOMBRE_TABLA + " *");
        System.out.println("**" + "*".repeat(NOMBRE_TABLA.length()) + "**");
        System.out.println(Pedido.cabecera());
        this.listar().forEach(System.out::println);
    }

    @Override
    public List<Pedido> listar() {
        List<Pedido> pedidos = new ArrayList<>();

        try(
                PreparedStatement statement = getConnection().prepareStatement(("SELECT * FROM " + NOMBRE_TABLA));
                ResultSet rs = statement.executeQuery();
                ){
            while (rs.next()){
                pedidos.add(this.obtenerPedido(rs));
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }

        return pedidos;
    }

    @Override
    public void guardar(Pedido pedido) {

    }

    @Override
    public void eliminar(Integer id) {

    }

    @Override
    public Pedido buscarPorId(Integer id) {
        return null;
    }

    private Pedido obtenerPedido(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setId(rs.getInt("id"));
        p.setClienteId(rs.getInt("cliente_id"));
        p.setFechaPedido(rs.getTimestamp("fecha_pedido").toLocalDateTime());
        p.setTotal(rs.getDouble("total"));
        return p;
    }
}
