package com.calories.front.views.user;

import com.calories.front.api.UserApiClient;
import com.calories.front.dto.UserDTO;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("user-details/:userId/select")
@PageTitle("Szczegóły użytkownika")
@CssImport("./styles/styles.css")
public class UserDetailsView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    private UserApiClient userApiClient;

    private String userId;
    private UserDTO selectedUser;

    private final TextField username = new TextField("Nazwa użytkownika");
    private final EmailField email = new EmailField("Email");
    private final TextField dailyCalorieIntake = new TextField("Dzienne zapotrzebowanie kaloryczne");
    private final TextField dailyCalorieConsumption = new TextField("Dzienne spalanie kalorii");

    public UserDetailsView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        Div background = new Div();
        background.addClassName("background");
        background.setSizeFull();

        Div userDetailContainer = new Div();
        userDetailContainer.getStyle().set("background-color", "white");
        userDetailContainer.getStyle().set("padding", "20px");
        userDetailContainer.getStyle().set("border-radius", "10px");
        userDetailContainer.getStyle().set("margin", "50px auto 0 auto");

        HorizontalLayout userDetailLayout = new HorizontalLayout(username, email, dailyCalorieIntake, dailyCalorieConsumption);
        userDetailLayout.setSpacing(true);

        Button changeUserButton = new Button("Zmień", event -> getUI().ifPresent(ui -> ui.navigate("users")));
        changeUserButton.addClassName("black-button");

        VerticalLayout userInfoLayout = new VerticalLayout(userDetailLayout, changeUserButton);
        userInfoLayout.setSpacing(true);
        userDetailContainer.add(userInfoLayout);

        VerticalLayout buttonsLayout = createButtonsLayout();
        buttonsLayout.getStyle().set("margin", "auto");

        Button backButton = new Button("Powrót do głównego menu", event -> getUI().ifPresent(ui -> ui.navigate("")));
        backButton.addClassName("black-button");

        Button undoButton = new Button("Cofnij", event -> UI.getCurrent().getPage().executeJs("history.back()"));
        undoButton.addClassName("black-button");

        HorizontalLayout footerButtons = new HorizontalLayout(undoButton, backButton);
        footerButtons.setSpacing(true);
        footerButtons.getStyle().set("position", "absolute");
        footerButtons.getStyle().set("bottom", "50px");

        add(userDetailContainer, buttonsLayout, footerButtons);
        getElement().appendChild(background.getElement());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.userId = event.getRouteParameters().get("userId").orElseThrow();
        this.selectedUser = userApiClient.getUserById(Long.parseLong(userId));
        updateUserDetails();
    }

    private void updateUserDetails() {
        username.setValue(selectedUser.getUsername());
        email.setValue(selectedUser.getEmail());
        dailyCalorieIntake.setValue(String.valueOf(selectedUser.getDailyCalorieIntake()));
        dailyCalorieConsumption.setValue(String.valueOf(selectedUser.getDailyCalorieConsumption()));
    }

    private void refreshUserDetails() {
        this.selectedUser = userApiClient.getUserById(Long.parseLong(userId));
        updateUserDetails();
    }

    private VerticalLayout createButtonsLayout() {
        VerticalLayout buttonsLayout = new VerticalLayout();

        Button viewIngredientsButton = new Button("Wyświetl składniki", event -> getUI().ifPresent(ui -> ui.navigate("view-ingredients/" + userId)));
        viewIngredientsButton.addClassName("large-button");

        Button viewRecipesButton = new Button("Wyświetl listę przepisów", event -> getUI().ifPresent(ui -> ui.navigate("view-recipes/" + userId)));
        viewRecipesButton.addClassName("large-button");

        Button viewActivitiesButton = new Button("Wyświetl listę aktywności", event -> getUI().ifPresent(ui -> ui.navigate("view-activities/" + userId)));
        viewActivitiesButton.addClassName("large-button");

        buttonsLayout.add(viewIngredientsButton, viewRecipesButton, viewActivitiesButton);
        buttonsLayout.setAlignItems(Alignment.CENTER);
        buttonsLayout.setSpacing(true);

        return buttonsLayout;
    }
}
