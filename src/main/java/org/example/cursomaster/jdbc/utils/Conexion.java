package org.example.cursomaster.jdbc.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexion {

    private static final String url;
    private static final String username;
    private static final String password;
    private static Connection connection;

    static {
        // Leer parámetros de configuración desde un fichero de texto
        try {
            Properties properties = new Properties();
            InputStream input = Conexion.class.getClassLoader().getResourceAsStream("config.properties");
            properties.load(input);
            url = properties.getProperty("db.url");
            username = properties.getProperty("db.username");
            password = properties.getProperty("db.password");
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getInstance() throws SQLException {
        if(connection == null){
            connection = DriverManager.getConnection(url, username, password);
        }
        return connection;
    }

}
