package com.example.guiex1;

import com.example.guiex1.controller.UtilizatorController;
import com.example.guiex1.domain.Prietenie;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.domain.validators.PrietenieValidator;
import com.example.guiex1.domain.validators.UtilizatorValidator;
import com.example.guiex1.repository.Repository;
import com.example.guiex1.repository.dbrepo.PrietenieDbRepository;
import com.example.guiex1.repository.dbrepo.UtilizatorDbRepository;
import com.example.guiex1.services.PrietenieService;
import com.example.guiex1.services.UtilizatorService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    Repository<Long, Utilizator> utilizatorRepository;
    UtilizatorService service;
    Repository<Long, Prietenie> prietenieRepository;
    PrietenieService prietenieService;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        System.out.println("Reading data from file");
        String username="postgres";
        String password="cami";
        String url="jdbc:postgresql://localhost:5432/socialnetworklab4";
        Repository<Long, Utilizator> utilizatorRepository =
                new UtilizatorDbRepository(url,username, password,  new UtilizatorValidator());
        Repository<Long, Prietenie> prietenieRepository =
                new PrietenieDbRepository(url,username, password,  new PrietenieValidator());

        prietenieService = new PrietenieService(prietenieRepository);
        service =new UtilizatorService(utilizatorRepository, prietenieService);
        initView(primaryStage);
        primaryStage.setWidth(667);
        primaryStage.show();


    }

    private void initView(Stage primaryStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/UtilizatorView.fxml"));

        AnchorPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        UtilizatorController userController = fxmlLoader.getController();
        userController.setUtilizatorService(service);

    }
}