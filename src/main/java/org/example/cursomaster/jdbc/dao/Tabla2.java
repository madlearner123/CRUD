package org.example.cursomaster.jdbc.dao;

import java.util.Optional;
import java.util.List;

// CRUD 2
public interface Tabla2<T> {

    Optional<T> obtenerClienteConPedidoMasCaro();

    Optional<List<T>> algunClienteConMasDeNPedidos(int n);

    Optional<Double> obtenerTotalGastadoPorCliente(int id);

    List<String> nombresQueEmpiezanPorLetra(char letra);

}
