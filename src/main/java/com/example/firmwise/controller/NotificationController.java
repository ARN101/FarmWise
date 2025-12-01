package com.example.firmwise.controller;

import com.example.firmwise.model.Notification;
import com.example.firmwise.service.NotificationService;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class NotificationController {

    @FXML
    private ListView<Notification> notificationList;

    @FXML
    public void initialize() {
        NotificationService service = new NotificationService();
        notificationList.getItems().addAll(service.getNotifications());

        notificationList.setCellFactory(param -> new ListCell<Notification>() {
            @Override
            protected void updateItem(Notification item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Text title = new Text(item.getTitle() + "\n");
                    title.setFont(Font.font("System", FontWeight.BOLD, 14));
                    title.setFill(item.getType() == Notification.NotificationType.WARNING ? Color.RED : Color.BLUE);

                    Text message = new Text(item.getMessage());
                    message.setFont(Font.font("System", 12));

                    TextFlow flow = new TextFlow(title, message);
                    setGraphic(flow);
                }
            }
        });
    }
}
