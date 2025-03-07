package org.example.cursomaster.jdbc.modulos;

import org.example.cursomaster.jdbc.dao.TablaPedidos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Cliente {

    private Integer id;
    private String nombre;
    private String email;
    private String telefono;
    private LocalDateTime fechaRegistro;
    private List<Pedido> pedidos = new ArrayList<>();

    public Cliente(){}

    public Cliente(Cliente c){
        this(c.getId(), c.getNombre(), c.getEmail(), c.getTelefono());
    }

    public Cliente(String nombre, String email, String telefono){
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.fechaRegistro = LocalDateTime.now();
    }

    public Cliente(Integer id, String nombre, String email, String telefono){
        this(nombre, email, telefono);
        this.id = id;
    }

    public Cliente(Integer id, String nombre, String email, String telefono, LocalDateTime fechaRegistro) {
        this(id, nombre, email, telefono);
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public List<Pedido> getPedidos() { return pedidos; }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString(){
        return String.format("%-5d | %-20s | %-30s | %-10s | %s", id, nombre, email, telefono, fechaRegistro);
    }
    public static String cabecera(){
        return String.format("%-5s | %-20s | %-30s | %-10s | %s",
                "id ", "nombre       ", "email                      ", "telefono", "fecha_registro\n") +
                "___________________________________________________________________________________________________";
    }

    public void agregarPedidos() {
        final TablaPedidos PEDIDOS = new TablaPedidos();
        this.pedidos = PEDIDOS.listar().stream().filter(p -> p.getClienteId().equals(this.id)).toList();
    }
}
