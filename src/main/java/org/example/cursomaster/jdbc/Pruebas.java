package org.example.cursomaster.jdbc;

import org.example.cursomaster.jdbc.dao.Tabla;
import org.example.cursomaster.jdbc.dao.Tabla2;
import org.example.cursomaster.jdbc.dao.TablaClientes;
import org.example.cursomaster.jdbc.dao.TablaPedidos;
import org.example.cursomaster.jdbc.modulos.Cliente;
import org.example.cursomaster.jdbc.modulos.Pedido;

import java.sql.SQLOutput;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Pruebas {

    public static void main(String[] args){

        testMetodosTabla();

    }

    private static List<Cliente> obtenerClientesConMasDeNPedidos(List<Cliente> clientes, int n) {
        return clientes.stream().filter(c -> c.getPedidos().size() > n).toList();
    }

    private static Optional<Cliente> obtenerClienteConPedidoMasCaro(List<Cliente> clientes) {
        // Mi solución (no funca)
//        return clientes.stream().max((c1, c2) -> (int) Math.max(
//                c1.getPedidos().stream().mapToDouble(Pedido::getTotal).max().orElse(0),
//                c2.getPedidos().stream().mapToDouble(Pedido::getTotal).peek(System.out::println).max().orElse(0)
//                )
//        );
        // Solución propuesta
        return clientes.stream().filter(c -> !c.getPedidos().isEmpty())
                .max(Comparator.comparingDouble(c ->
                        c.getPedidos().stream()
                                .mapToDouble(Pedido::getTotal)
                                .max()
                                .orElse(0))
                );
    }

    private static double obtenerTotalGastadoPorCliente(List<Cliente> clientes, int clienteId) {
        return clientes.stream().filter(c -> c.getId() == clienteId).findFirst().orElse(new Cliente())
                .getPedidos().stream().mapToDouble(Pedido::getTotal).sum();
    }

    private static List<String> filtrarNombresEnMayusculasPorLetra(List<Cliente> clientes, char letra) {
        return clientes.stream().map(c -> c.getNombre().toUpperCase()).filter(s -> s.charAt(0) == letra).toList();
    }

    private static void testMetodosTabla(){
        System.out.println("\nMétodos generales de Tabla");

        final Tabla<Cliente> CLIENTES = new TablaClientes();
        CLIENTES.mostrar();

        final Tabla<Pedido> PEDIDOS = new TablaPedidos();
        PEDIDOS.mostrar();

        System.out.println("\n*******************  Pruebas buscarPorID  *******************");
        System.out.println(CLIENTES.buscarPorId(1));
        System.out.println(CLIENTES.buscarPorId(4));
        System.out.println(CLIENTES.buscarPorId(3));
    }

    private static void testMetodosTabla2JAVA(){
        System.out.println("\nMétodos específicos de Tabla de Clientes");
        System.out.println("Implementados con Java");

        final Tabla<Cliente> CLIENTES = new TablaClientes();

        List<Cliente> listaClientes = CLIENTES.listar();
        listaClientes.forEach(Cliente::agregarPedidos);

        System.out.println("\n***************** CLIENTE CON EL PEDIDO MÁS CARO *****************");
        Optional<Cliente> c = obtenerClienteConPedidoMasCaro(listaClientes);
        c.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("No se encontró el cliente con el pedido más caro")
        );

        System.out.println("\n***************** CLIENTES CON MÁS DE 1 PEDIDO *****************");
        obtenerClientesConMasDeNPedidos(listaClientes, 1).forEach(System.out::println);

        System.out.println("\n***************** CLIENTES CON MÁS DE 5 PEDIDOS *****************");
        obtenerClientesConMasDeNPedidos(listaClientes, 5).forEach(System.out::println);

        System.out.println("\n***************** CLIENTES CON MÁS DE 8 PEDIDOS *****************");
        obtenerClientesConMasDeNPedidos(listaClientes, 8).forEach(System.out::println);

        System.out.println("\n***************** OBTENER TOTAL GASTADO POR ALGUNOS CLIENTES *****************");
        System.out.println(CLIENTES.buscarPorId(1));
        System.out.println("Total gastado: " + obtenerTotalGastadoPorCliente(listaClientes, 1));
        System.out.println(CLIENTES.buscarPorId(5));
        System.out.println("Total gastado: " + obtenerTotalGastadoPorCliente(listaClientes, 5));
        System.out.println(CLIENTES.buscarPorId(31));
        System.out.println("Total gastado: " + obtenerTotalGastadoPorCliente(listaClientes, 31));

        System.out.println("\n***************** OBTENER NOMBRES EN MAYÚSCULAS QUE EMPIECEN POR A *****************");
        System.out.println(filtrarNombresEnMayusculasPorLetra(listaClientes, 'A'));
    }

    private static void testMetodosTabla2SQL(){
        System.out.println("\nMétodos específicos de Tabla de Clientes");
        System.out.println("Implementados con SQL");

        final Tabla2<Cliente> CLIENTES = new TablaClientes();

        System.out.println("\n********* Cliente con pedido más caro *********");
        Optional<Cliente> clientePedidoMasCaro = CLIENTES.obtenerClienteConPedidoMasCaro();
        clientePedidoMasCaro.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("No se encontró el cliente con el pedido más caro")
        );

        int n = 3;
        System.out.println("\n*********** Clientes con más de " + n + " pedidos.");
        Optional<List<Cliente>> clientes = CLIENTES.algunClienteConMasDeNPedidos(n);
        clientes.ifPresentOrElse(
                c -> c.forEach(System.out::println),
                        () -> System.out.println("No hay clientes con más de " + n + " pedidos.")
        );

        int idCliente = 2;
        System.out.println("\n************ Obtener total gastado por cliente " + idCliente + " ************");
        Optional<Double> gastoTotal = CLIENTES.obtenerTotalGastadoPorCliente(idCliente);
        gastoTotal.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("No se pudo encontrar el total gastado por el cliente " + idCliente)
        );

        char letra = 'a';
        System.out.println("\n************ Filtrado de nombres en mayúsculas que comience por " + letra + " ************");
        System.out.println(CLIENTES.nombresQueEmpiezanPorLetra(letra));
    }
}

