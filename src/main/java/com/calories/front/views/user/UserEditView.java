package com.calories.front.views.user;

import com.calories.front.api.UserApiClient;
import com.calories.front.dto.UserDTO;
import com.calories.front.views.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("user-edit/:userId?")
public class UserEditView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    private UserApiClient userApiClient;

    private String userId;

    private final FormLayout userForm = new FormLayout();
    private final TextField username = new TextField("Username");
    private final EmailField email = new EmailField("Email");
    private final TextField dailyCalorieIntake = new TextField("Daily Calorie Intake");
    private final TextField dailyCalorieConsumption = new TextField("Daily Calorie Consumption");

    public UserEditView() {
        userForm.add(username, email, dailyCalorieIntake, dailyCalorieConsumption);
        userForm.setSizeFull();
        HorizontalLayout save = createSubmitLayout();
        add(userForm, save);
    }

    private HorizontalLayout createSubmitLayout() {
        HorizontalLayout save = new HorizontalLayout();

        Button submit = new Button("Submit changes");
        submit.addClickListener(event -> {
            String name = username.getValue();
            String mail = email.getValue();
            double intake = Double.parseDouble(dailyCalorieIntake.getValue());
            double consumption = Double.parseDouble(dailyCalorieConsumption.getValue());

            updateUserFromForm(name, mail, intake, consumption);

            Notification n = new Notification("User updated successfully");
            n.setPosition(Notification.Position.TOP_CENTER);
            n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            n.setDuration(5000);
            n.open();
            UI.getCurrent().navigate(UsersView.class);
        });

        Button backToMain = new Button("Main menu");
        backToMain.addClickListener(event -> UI.getCurrent().navigate(MainView.class));

        save.add(submit, backToMain);
        return save;
    }

    private void updateUserFromForm(String name, String mail, double intake, double consumption) {
        UserDTO dto = new UserDTO(Long.parseLong(userId), name, mail, intake, consumption);
        userApiClient.updateUser(dto, Long.parseLong(userId));
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