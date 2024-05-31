package com.calories.front.views.user;

import com.calories.front.api.UserApiClient;
import com.calories.front.dto.UserDTO;
import com.calories.front.views.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("user-create")
public class UserCreationView extends VerticalLayout {

    @Autowired
    private UserApiClient userApiClient;

    private final FormLayout userForm = new FormLayout();
    private final TextField username = new TextField("Username");
    private final EmailField email = new EmailField("Email");
    private final TextField dailyCalorieIntake = new TextField("Daily Calorie Intake");
    private final TextField dailyCalorieConsumption = new TextField("Daily Calorie Consumption");

    public UserCreationView() {
        username.setRequired(true);
        email.setRequired(true);
        dailyCalorieIntake.setRequired(true);
        dailyCalorieConsumption.setRequired(true);

        userForm.add(username, email, dailyCalorieIntake, dailyCalorieConsumption);
        userForm.setSizeFull();

        HorizontalLayout buttons = createButtonsLayout();

        add(userForm, buttons);
    }

    private HorizontalLayout createButtonsLayout() {
        HorizontalLayout functions = new HorizontalLayout();

        Button create = new Button("Create ");
        create.addClickListener(event -> {
            String name = username.getValue();
            String mail = email.getValue();
            double intake = Double.parseDouble(dailyCalorieIntake.getValue());
            double consumption = Double.parseDouble(dailyCalorieConsumption.getValue());

            if (name == null || mail == null || name.equals("") || mail.equals("")) {
                Notification fail = new Notification();
                fail.setText("Please fill in all required fields");
                fail.setDuration(3000);
                fail.setPosition(Notification.Position.TOP_CENTER);
                fail.addThemeVariants(NotificationVariant.LUMO_WARNING);
                fail.open();
            } else {
                createUser(name, mail, intake, consumption);
                UI.getCurrent().navigate(UsersView.class);
            }
        });

        Button backToMain = new Button("Main menu");
        backToMain.addClickListener(event -> UI.getCurrent().navigate(MainView.class));

        functions.add(create, backToMain);
        return functions;
    }

    private void createUser(String name, String mail, double intake, double consumption) {
        UserDTO dto = new UserDTO(null, name, mail, intake, consumption);
        userApiClient.createUser(dto);
    }
}
