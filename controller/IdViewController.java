package com.example.guiex1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class IdViewController {

    @FXML
    Label labelId;

    Long idUser;
    Stage stage;

    public void setIdUser(Stage stage, Long id)
    {
        this.stage = stage;
        this.idUser = id;
        this.labelId.setText(String.valueOf(idUser));
    }


}
