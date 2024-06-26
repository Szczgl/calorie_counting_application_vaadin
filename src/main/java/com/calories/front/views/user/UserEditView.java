package com.calories.front.views.user;

import com.calories.front.api.UserApiClient;
import com.calories.front.dto.UserDTO;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.BeforeEnterEvent;
import org.springframework.beans.factory.annotation.Autowired;


@Route("user-edit/:userId?")
@PageTitle("Edytuj użytkownika")
@CssImport("./styles/styles.css")
public class UserEditView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    private UserApiClient userApiClient;

    private String userId;

    private final FormLayout userForm = new FormLayout();
    private final TextField username = new TextField("Nazwa użytkownika");
    private final EmailField email = new EmailField("Email");
    private final TextField dailyCalorieIntake = new TextField("Dzienne zapotrzebowanie kaloryczne");

    public UserEditView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        Div background = new Div();
        background.addClassName("background");
        background.setSizeFull();

        Div formContainer = new Div();
        formContainer.getStyle().set("background-color", "white");
        formContainer.getStyle().set("padding", "20px");
        formContainer.getStyle().set("border-radius", "10px");
        formContainer.add(userForm);

        H1 title = new H1("Edytuj użytkownika");
        title.addClassName("title");

        userForm.add(username, email, dailyCalorieIntake);
        userForm.setSizeFull();

        Button submitButton = new Button("Zatwierdź zmiany", event -> {
            String name = username.getValue();
            String mail = email.getValue();
            double intake = Double.parseDouble(dailyCalorieIntake.getValue());

            updateUserFromForm(name, mail, intake);

            Notification n = new Notification("Użytkownik zaktualizowany pomyślnie");
            n.setPosition(Notification.Position.TOP_CENTER);
            n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            n.setDuration(5000);
            n.open();
            getUI().ifPresent(ui -> ui.navigate(UsersView.class));
        });
        submitButton.addClassName("black-button");

        Button undoButton = new Button("Cofnij", event -> UI.getCurrent().getPage().executeJs("history.back()"));
        undoButton.addClassName("black-button");

        Button backButton = new Button("Powrót do głównego menu", event -> getUI().ifPresent(ui -> ui.navigate("")));
        backButton.addClassName("black-button");

        HorizontalLayout buttonsLayout = new HorizontalLayout(submitButton, undoButton, backButton);
        buttonsLayout.setSpacing(true);
        buttonsLayout.setAlignItems(Alignment.CENTER);

        add(title, formContainer, buttonsLayout);
        getElement().appendChild(background.getElement());
    }

    private void updateUserFromForm(String name, String mail, double intake) {
        UserDTO dto = new UserDTO(Long.parseLong(userId), name, mail, intake, 0);
        userApiClient.updateUser(dto, Long.parseLong(userId));
    }

    @Override
    protected void onAttach(com.vaadin.flow.component.AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        if (userId != null) {
            UserDTO user = userApiClient.getUserById(Long.parseLong(userId));
            username.setValue(user.getUsername());
            email.setValue(user.getEmail());
            dailyCalorieIntake.setValue(String.valueOf(user.getDailyCalorieIntake()));
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.userId = event.getRouteParameters().get("userId").orElse(null);
        if (this.userId != null) {
            UserDTO user = userApiClient.getUserById(Long.parseLong(userId));
            username.setValue(user.getUsername());
            email.setValue(user.getEmail());
            dailyCalorieIntake.setValue(String.valueOf(user.getDailyCalorieIntake()));
        }
    }
}