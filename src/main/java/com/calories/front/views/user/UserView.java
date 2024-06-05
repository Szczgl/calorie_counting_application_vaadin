package com.calories.front.views.user;

import com.calories.front.api.UserApiClient;
import com.calories.front.dto.UserDTO;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.router.BeforeEnterEvent;

@Route("user-details/:userId")
@PageTitle("Szczegóły użytkownika")
@CssImport("./styles/styles.css")
public class UserView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    private UserApiClient userApiClient;

    private String userId;

    private final FormLayout userForm = new FormLayout();
    private final TextField username = new TextField("Nazwa użytkownika");
    private final EmailField email = new EmailField("Email");
    private final TextField dailyCalorieIntake = new TextField("Dzienne zapotrzebowanie kaloryczne");

    public UserView() {
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

        H1 title = new H1("Szczegóły użytkownika");
        title.addClassName("title");

        userForm.add(username, email, dailyCalorieIntake);
        userForm.setSizeFull();

        Button editButton = new Button("Edytuj", event -> getUI()
                .ifPresent(ui -> ui.navigate(UserEditView.class, new RouteParameters("userId", userId))));
        editButton.addClassName("black-button");

        Button deleteButton = new Button("Usuń", event -> {
            userApiClient.deleteUser(Long.valueOf(userId));
            getUI().ifPresent(ui -> ui.navigate("users"));
        });
        deleteButton.addClassName("black-button");

        Button undoButton = new Button("Cofnij", event -> UI
                .getCurrent().getPage().executeJs("history.back()"));
        undoButton.addClassName("black-button");

        Button backButton = new Button("Powrót do głównego menu", event -> getUI()
                .ifPresent(ui -> ui.navigate("")));
        backButton.addClassName("black-button");

        HorizontalLayout buttonsLayout = new HorizontalLayout(editButton, deleteButton, undoButton, backButton);
        buttonsLayout.setSpacing(true);
        buttonsLayout.setAlignItems(Alignment.CENTER);

        add(title, formContainer, buttonsLayout);
        getElement().appendChild(background.getElement());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.userId = event.getRouteParameters().get("userId").orElseThrow();
        UserDTO user = userApiClient.getUserById(Long.parseLong(userId));
        username.setValue(user.getUsername());
        email.setValue(user.getEmail());
        dailyCalorieIntake.setValue(String.valueOf(user.getDailyCalorieIntake()));
    }
}