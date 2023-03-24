package com.example.guiex1.controller;

import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.services.UtilizatorService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AddUserController {

    @FXML
    TextField textFieldFirstName;

    @FXML
    TextField textFieldLastName;

    Stage stage;

    public void setUtilizatorService(UtilizatorService utilizatorService, Stage stage) {
        this.utilizatorService = utilizatorService;
        this.stage = stage;
    }

    UtilizatorService utilizatorService;


    public void handleAddUserPls(ActionEvent actionEvent) {
        String firstName = textFieldFirstName.getText();
        String lastName = textFieldLastName.getText();

        Utilizator user = new Utilizator(firstName, lastName);
        utilizatorService.addUtilizator(user);
        stage.close();
    }
}
