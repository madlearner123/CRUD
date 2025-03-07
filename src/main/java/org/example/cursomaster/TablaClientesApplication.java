package org.example.cursomaster;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.cursomaster.jdbc.dao.Tabla;
import org.example.cursomaster.jdbc.dao.TablaClientes;
import org.example.cursomaster.jdbc.modulos.Cliente;

import java.io.IOException;
import java.time.LocalDateTime;

public class TablaClientesApplication extends Application {

    private final Tabla<Cliente> TABLA_CLIENTES_BD = new TablaClientes();

    private ObservableList<Cliente> clientes = FXCollections.observableArrayList(
            TABLA_CLIENTES_BD.listar()
            );

    private final TableView<Cliente> tableView = new TableView<>();

    // Añadir un nuevo cliente
    private Button addButton = new Button("Guardar cliente");
    private TextField campoNombre = new TextField();
    private TextField campoEmail = new TextField();
    private TextField campoTelefono = new TextField();

    // Editar cliente existente
    private Cliente clienteSeleccionado = new Cliente();

    @Override
    public void start(Stage stage) throws IOException {

        TableColumn<Cliente, Integer> idC = new TableColumn<>("Id");
        TableColumn<Cliente, String> nombreC = new TableColumn<>("Nombre");
        TableColumn<Cliente, String> emailC = new TableColumn<>("Email");
        TableColumn<Cliente, String> telefonoC = new TableColumn<>("Teléfono");
        TableColumn<Cliente, LocalDateTime> fechaRegistroC = new TableColumn<>("Fecha de registro");
        TableColumn<Cliente, Void> deleteColumn = new TableColumn<>("");
        TableColumn<Cliente, Void> editColumn = new TableColumn<>("");

        idC.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreC.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        emailC.setCellValueFactory(new PropertyValueFactory<>("email"));
        telefonoC.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        fechaRegistroC.setCellValueFactory(new PropertyValueFactory<>("fechaRegistro"));

        // Borrar cliente
        deleteColumn.setCellFactory(c -> new TableCell<>(){
            private final Button deleteButton = new Button("Eliminar");
            {
                deleteButton.setOnAction(event -> {
                   Cliente cliente = getTableView().getItems().get(getIndex());
                   tableView.getItems().remove(cliente);
                   // Borra de la base de datos
                   TABLA_CLIENTES_BD.eliminar(cliente.getId());
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty){
                super.updateItem(item, empty);
                if(empty) setGraphic(null);
                else setGraphic(deleteButton);
            }
        });

        // Editar cliente
        editColumn.setCellFactory(c -> new TableCell<>(){
            private final Button editButton = new Button("Editar");
            {
                editButton.setOnAction(event -> {
                    clienteSeleccionado = getTableView().getItems().get(getIndex());
                    campoNombre.setText(clienteSeleccionado.getNombre());
                    campoEmail.setText(clienteSeleccionado.getEmail());
                    campoTelefono.setText(clienteSeleccionado.getTelefono());
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty){
                super.updateItem(item, empty);
                if(empty) setGraphic(null);
                else setGraphic(editButton);
            }
        });

        tableView.getColumns().addAll(idC, nombreC, emailC, telefonoC, fechaRegistroC, deleteColumn, editColumn);
        tableView.setItems(this.clientes);

        // Crear un nuevo cliente
        activarBotonAgregarCliente();

        HBox formBox = new HBox(10, campoNombre, campoEmail, campoTelefono, addButton);
        formBox.setPadding(new Insets(10));

        VBox vbox = new VBox(formBox, tableView);
        Scene scene = new Scene(vbox, 820, 640);
        stage.setTitle("Gestión de Clientes");
        stage.setScene(scene);
        stage.show();
    }

    private void activarBotonAgregarCliente(){

        campoNombre.setPromptText("Nombre");
        campoEmail.setPromptText("Dirección de correo electrónico");
        campoTelefono.setPromptText("Número de teléfono");

        addButton.setOnAction(event -> {
            String nombre = campoNombre.getText();
            String email = campoEmail.getText();
            String telefono = campoTelefono.getText();

            if(!nombre.isBlank() && !email.isBlank() && !telefono.isBlank()){
                try{
                    // Guardar cambios
                    Cliente c = new Cliente(clienteSeleccionado.getId(), nombre, email, telefono);
                    if(c.getId() == null) clientes.add(c);
                    TABLA_CLIENTES_BD.guardar(c);

                    // Resetear form
                    campoNombre.clear();
                    campoEmail.clear();
                    campoTelefono.clear();
                    clienteSeleccionado = new Cliente(); // reseteamos Cliente: su atributo id será null por lo que al llamar el método guardar() lo que hará será crear un nuevo registro en la tabla de la BD.
                    clientes = FXCollections.observableArrayList(
                            TABLA_CLIENTES_BD.listar()
                    );
                    tableView.setItems(this.clientes);
                    tableView.refresh();
                } catch (NumberFormatException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Número introducido no válido");
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Rellene todos los campos");
                alert.show();
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}