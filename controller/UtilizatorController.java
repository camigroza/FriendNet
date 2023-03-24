package com.example.guiex1.controller;

import com.example.guiex1.MainApplication;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.services.UtilizatorService;
import com.example.guiex1.utils.events.UtilizatorEntityChangeEvent;
import com.example.guiex1.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UtilizatorController implements Observer<UtilizatorEntityChangeEvent> {
    UtilizatorService utilizatorService;
    ObservableList<Utilizator> model = FXCollections.observableArrayList();


    @FXML
    TableView<Utilizator> tableView;
    @FXML
    TableColumn<Utilizator,String> tableColumnFirstName;
    @FXML
    TableColumn<Utilizator,String> tableColumnLastName;



    public void setUtilizatorService(UtilizatorService service) {
        this.utilizatorService = service;
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("lastName"));
        tableView.setItems(model);
    }

    private void initModel() {
        Iterable<Utilizator> users = utilizatorService.getAll();
        List<Utilizator> usersList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());

        model.setAll(usersList);
    }

    @Override
    public void update(UtilizatorEntityChangeEvent utilizatorEntityChangeEvent) {

        initModel();
    }

    public void handleAddUtilizator(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/AddUserView.fxml"));

        AnchorPane userLayout = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(userLayout));

        AddUserController addUserController = fxmlLoader.getController();
        addUserController.setUtilizatorService(utilizatorService, stage);

        stage.show();

    }

    public void handleDeleteUtilizator(ActionEvent actionEvent) {
        Utilizator user=(Utilizator) tableView.getSelectionModel().getSelectedItem();
        if (user!=null) {
            Utilizator deleted= utilizatorService.deleteUtilizator(user.getId());
        }
    }

    public void handleUpdateUtilizator(ActionEvent actionEvent) {
    }

    public void handleButton(ActionEvent actionEvent) {
        System.out.println("Hello :)");
    }

    public void onSayHello(ActionEvent actionEvent) {
        System.out.println("Hello ...");
    }

    public void plsRefresh(ActionEvent actionEvent) {
        initModel();
    }

    public void handleShowFriends(ActionEvent actionEvent) throws IOException {
        Utilizator user=(Utilizator) tableView.getSelectionModel().getSelectedItem();
        Long id_user = user.getId();

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/FriendsView.fxml"));

        AnchorPane userLayout = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(userLayout));

        FriendsController friendsController = fxmlLoader.getController();
        friendsController.setUtilizatorService(utilizatorService, stage, id_user);

        stage.show();

        friendsController.initModel();
    }

    public void handleShowFriendRequests(ActionEvent actionEvent) throws IOException {
        Utilizator user=(Utilizator) tableView.getSelectionModel().getSelectedItem();
        Long id_user = user.getId();

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/FriendRequestsView.fxml"));

        AnchorPane userLayout = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(userLayout));

        FriendRequestsController friendRequestsController = fxmlLoader.getController();
        friendRequestsController.setUtilizatorService(utilizatorService, stage, id_user);

        stage.show();
    }
}
