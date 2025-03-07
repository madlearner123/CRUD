package org.example.cursomaster.jdbc.modulos;

import java.time.LocalDateTime;

public class Pedido {

    private Integer id;
    private Integer clienteId;
    private LocalDateTime fechaPedido;
    private Double total;

    public Pedido(){}

    public Pedido(Integer id, Integer clienteId, LocalDateTime fechaPedido, Double total) {
        this.id = id;
        this.clienteId = clienteId;
        this.fechaPedido = fechaPedido;
        this.total = total;
    }

    public Integer getId() {
        return id;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public Double getTotal() {
        return total;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public String toString(){
        return String.format("%-5d | %-10d | %-30s | %-10f", id, clienteId, fechaPedido, total);
    }
    public static String cabecera(){
        return String.format("%-5s | %-10s | %-30s | %s",
                "id ", "cliente_id", "fecha_pedido              ", "total      \n") +
                "___________________________________________________________________";
    }

}
