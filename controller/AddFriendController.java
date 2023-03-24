package com.example.guiex1.controller;

import com.example.guiex1.domain.Prietenie;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.services.PrietenieService;
import com.example.guiex1.services.UtilizatorService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AddFriendController {

    UtilizatorService utilizatorService;
    Long id_user;
    Stage stage;
    ObservableList<Utilizator> model = FXCollections.observableArrayList();
    @FXML
    TableView<Utilizator> tableView;
    @FXML
    TableColumn<Utilizator,String> tableColumnFirstName;
    @FXML
    TableColumn<Utilizator,String> tableColumnLastName;


    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("lastName"));
        tableView.setItems(model);
    }

    public void setPrietenieService(UtilizatorService utilizatorService, Stage stage, Long id) {
        this.utilizatorService = utilizatorService;
        this.stage = stage;
        this.id_user = id;
        initModel();
    }

    public void initModel() {
        Iterable<Utilizator> users = utilizatorService.getPossibleFriends(id_user);
        List<Utilizator> usersList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());

        model.setAll(usersList);
    }

    public void handleSendFriendRequest(ActionEvent actionEvent) {
        Utilizator user=(Utilizator) tableView.getSelectionModel().getSelectedItem();
        Long idFriend = user.getId();
        Prietenie p = new Prietenie(id_user, idFriend);
        utilizatorService.getPrietenieService().addPrietenie(p);
        initModel();
    }

    public void handleRefresh(ActionEvent actionEvent) {
        initModel();
    }
}
