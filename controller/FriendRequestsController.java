package com.example.guiex1.controller;

import com.example.guiex1.domain.Prietenie;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.services.UtilizatorService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendRequestsController {
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

    public void setUtilizatorService(UtilizatorService utilizatorService, Stage stage, Long id_user) {
        this.utilizatorService = utilizatorService;
        this.stage = stage;
        this.id_user = id_user;
        initModel();
    }

    public void initModel() {
        Iterable<Utilizator> users = utilizatorService.getFriendRequests(id_user);
        List<Utilizator> usersList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());

        model.setAll(usersList);
    }

    public void handleAcceptRequest(ActionEvent actionEvent) {
        Utilizator user=(Utilizator) tableView.getSelectionModel().getSelectedItem();
        Long idFriend = user.getId();
        Long prietenieId = utilizatorService.getIdOfFriendshipOfTwoUsers(id_user, idFriend);
        utilizatorService.getPrietenieService().getRepo().delete(prietenieId);
        Prietenie prietenie = new Prietenie(id_user, idFriend);
        prietenie.setStatus("accepted");
        utilizatorService.getPrietenieService().getRepo().save(prietenie);
        initModel();
    }

    public void handleDenyRequest(ActionEvent actionEvent) {
        Utilizator user=(Utilizator) tableView.getSelectionModel().getSelectedItem();
        Long idFriend = user.getId();
        Long prietenieId = utilizatorService.getIdOfFriendshipOfTwoUsers(id_user, idFriend);
        utilizatorService.getPrietenieService().getRepo().delete(prietenieId);
        initModel();
    }
}
