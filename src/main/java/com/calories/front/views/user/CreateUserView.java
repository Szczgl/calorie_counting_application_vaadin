package com.calories.front.views.user;

import com.calories.front.api.UserApiClient;
import com.calories.front.dto.UserDTO;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("create-user")
@PageTitle("Utwórz użytkownika")
@CssImport("./styles/styles.css")
public class CreateUserView extends VerticalLayout {

    @Autowired
    private UserApiClient userApiClient;

    private final FormLayout userForm = new FormLayout();
    private final TextField username = new TextField("Nazwa użytkownika");
    private final EmailField email = new EmailField("Email");
    private final TextField dailyCalorieIntake = new TextField("Dzienne zapotrzebowanie kaloryczne");

    public CreateUserView() {
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

        H1 title = new H1("Utwórz użytkownika");
        title.addClassName("title");

        userForm.add(username, email, dailyCalorieIntake);
        userForm.setSizeFull();

        Button createButton = new Button("Utwórz", event -> {
            String name = username.getValue();
            String mail = email.getValue();
            double intake = Double.parseDouble(dailyCalorieIntake.getValue());

            if (name.isEmpty() || mail.isEmpty()) {
                Notification fail = new Notification("Proszę wypełnić wszystkie wymagane pola");
                fail.setDuration(3000);
                fail.setPosition(Notification.Position.TOP_CENTER);
                fail.addThemeVariants(NotificationVariant.LUMO_WARNING);
                fail.open();
            } else {
                UserDTO dto = new UserDTO(null, name, mail, intake, 0);
                userApiClient.createUser(dto);
                getUI().ifPresent(ui -> ui.navigate("users"));
            }
        });
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createButton.addClassName("black-button");

        Button undoButton = new Button("Cofnij", event -> UI.getCurrent().getPage().executeJs("history.back()"));
        undoButton.addClassName("black-button");

        Button backButton = new Button("Powrót do głównego menu", event -> getUI().ifPresent(ui -> ui.navigate("")));
        backButton.addClassName("black-button");

        HorizontalLayout buttonsLayout = new HorizontalLayout(createButton, undoButton, backButton);
        buttonsLayout.setSpacing(true);
        buttonsLayout.setAlignItems(Alignment.CENTER);

        add(title, formContainer, buttonsLayout);
        getElement().appendChild(background.getElement());
    }
}