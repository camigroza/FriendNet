package com.example.guiex1.controller;

import com.example.guiex1.MainApplication;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.services.UtilizatorService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendsController {

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
        Iterable<Utilizator> users = utilizatorService.getFriends(id_user);
        List<Utilizator> usersList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());

        model.setAll(usersList);
    }

    public void handleAddFriend(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/AddFriendView.fxml"));

        AnchorPane userLayout = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(userLayout));

        AddFriendController addFriendController = fxmlLoader.getController();
        addFriendController.setPrietenieService(utilizatorService, stage, id_user);

        stage.show();
    }

    public void handleRefresh(ActionEvent actionEvent) {
        initModel();
    }

    public void handleDeleteFriend(ActionEvent actionEvent) {
        Utilizator user=(Utilizator) tableView.getSelectionModel().getSelectedItem();
        Long idFriend = user.getId();
        Iterable<Long> IdsOfFriendshipsForUser =
                utilizatorService.getPrietenieService().getIdsOfFriendshipsForUser(id_user);
        Long idPrietenie = Long.valueOf(0);
        for(Long id: IdsOfFriendshipsForUser)
        {
            if((Objects.equals(utilizatorService.getPrietenieService().findOne(id).getIdPrieten1(), id_user) &&
                    Objects.equals(utilizatorService.getPrietenieService().findOne(id).getIdPrieten2(), idFriend)) ||
                    (Objects.equals(utilizatorService.getPrietenieService().findOne(id).getIdPrieten1(), idFriend) &&
                            Objects.equals(utilizatorService.getPrietenieService().findOne(id).getIdPrieten2(), id_user)))
            {idPrietenie = id;}
        }
        utilizatorService.getPrietenieService().deletePrietenie(idPrietenie);
    }
}
