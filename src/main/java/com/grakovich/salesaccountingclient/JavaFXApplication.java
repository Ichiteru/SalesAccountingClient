package com.grakovich.salesaccountingclient;

import com.grakovich.salesaccountingclient.controller.Controller;
import com.grakovich.salesaccountingclient.controller.LoginController;
import com.grakovich.salesaccountingclient.controller.MainPageController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaFXApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() throws Exception {
        String[] args = getParameters().getRaw().toArray(new String[0]);
        this.applicationContext = new SpringApplicationBuilder()
                .sources(SalesAccountingClientApplication.class)
                .run(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(LoginController.class);
//        Parent root = fxWeaver.loadView(MainPageController.class);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        Controller.primaryStage = stage;
//        applicationContext.getBean(LoginController.class).setStage(stage);
    }

    @Override
    public void stop() throws Exception {
        this.applicationContext.close();
        Platform.exit();
    }
}
