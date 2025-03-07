package org.example.cursomaster.jdbc.dao;

import java.util.List;

// CRUD
public interface Tabla<T> {

    List<T> listar();

    void guardar(T t);

    void eliminar(Integer id);

    T buscarPorId(Integer id);

    /**
     * Muestra por consola la tabla.
     */
    void mostrar();

}
