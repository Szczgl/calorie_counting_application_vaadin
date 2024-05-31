package com.calories.front.views.user;

import com.calories.front.api.UserApiClient;
import com.calories.front.dto.UserDTO;
import com.calories.front.views.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import org.springframework.beans.factory.annotation.Autowired;

@Route("user-details/:userId")
public class UserView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    private UserApiClient userApiClient;

    private String userId;

    private final FormLayout userForm = new FormLayout();
    private final TextField username = new TextField("Username");
    private final EmailField email = new EmailField("Email");
    private final TextField dailyCalorieIntake = new TextField("Daily Calorie Intake");
    private final TextField dailyCalorieConsumption = new TextField("Daily Calorie Consumption");

    public UserView() {
        HorizontalLayout buttons = createButtonsLayout();
        userForm.add(username, email, dailyCalorieIntake, dailyCalorieConsumption);
        userForm.setSizeFull();
        add(buttons, userForm);
    }

    private HorizontalLayout createButtonsLayout() {
        HorizontalLayout functions = new HorizontalLayout();

        Button edit = new Button("Edit");
        edit.addClickListener(event -> UI.getCurrent().navigate(UserEditView.class, new RouteParameters("userId", userId)));

        Button delete = new Button("Delete");
        delete.addClickListener(event -> {
            deleteUserById(Long.valueOf(userId));
            UI.getCurrent().navigate(UsersView.class);
        });

        Button backToMain = new Button("Main menu");
        backToMain.addClickListener(event -> UI.getCurrent().navigate(MainView.class));

        functions.add(edit, delete, backToMain);
        return functions;
    }

    private void deleteUserById(Long id) {
        userApiClient.deleteUser(id);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        UserDTO user = userApiClient.getUserById(Long.parseLong(userId));
        username.setValue(user.getUsername());
        email.setValue(user.getEmail());
        dailyCalorieIntake.setValue(String.valueOf(user.getDailyCalorieIntake()));
        dailyCalorieConsumption.setValue(String.valueOf(user.getDailyCalorieConsumption()));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.userId = event.getRouteParameters().get("userId").orElseThrow();
    }
}