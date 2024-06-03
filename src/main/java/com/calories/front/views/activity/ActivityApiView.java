package com.calories.front.views.activity;

import com.calories.front.api.UserApiClient;
import com.calories.front.dto.ActivityDTO;
import com.calories.front.views.user.UserView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

//@Route("add-activity")
//public class ActivityApiView extends VerticalLayout {

//    private UserApiClient userApiClient;
//    private Long userId;
//
//    private final TextField activityName = new TextField("Nazwa aktywności");
//    private final TextArea activityDescription = new TextArea("Opis aktywności");
//    private final TextField consumedCalories = new TextField("Spalone kalorie");
//
//    @Autowired
//    public ActivityApiView(UserApiClient userApiClient) {
//        this.userApiClient = userApiClient;
//        userId = Long.parseLong(VaadinSession.getCurrent().getAttribute("userId").toString());
//
//        add(createFormLayout(), createButtonsLayout());
//    }
//
//    private FormLayout createFormLayout() {
//        FormLayout formLayout = new FormLayout();
//        formLayout.add(activityName, activityDescription, consumedCalories);
//        return formLayout;
//    }

//    private HorizontalLayout createButtonsLayout() {
//        Button saveButton = new Button("Zapisz");
//        saveButton.addClickListener(event -> saveActivity());
//
//        Button cancelButton = new Button("Anuluj");
//        cancelButton.addClickListener(event -> UI.getCurrent().navigate(UserView.class, new RouteParameters("userId", userId.toString())));
//
//        return new HorizontalLayout(saveButton, cancelButton);
//    }

//    private void saveActivity() {
//        String name = activityName.getValue();
//        String description = activityDescription.getValue();
//        double calories = Double.parseDouble(consumedCalories.getValue());
//        ActivityDTO activityDTO = new ActivityDTO(null, name, description, calories, userId);
//
//        userApiClient.addActivityToUser(userId, activityDTO);
//
//        Notification.show("Aktywność dodana pomyślnie", 3000, Notification.Position.TOP_CENTER);
//        UI.getCurrent().navigate(UserView.class, new RouteParameters("userId", userId.toString()));
//    }
//}
