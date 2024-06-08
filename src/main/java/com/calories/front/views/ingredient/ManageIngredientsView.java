package com.calories.front.views.ingredient;

import com.calories.front.api.IngredientApiClient;
import com.calories.front.api.UserApiClient;
import com.calories.front.dto.IngredientDTO;
import com.calories.front.dto.UserDTO;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Route("manage-ingredients/:userId")
@PageTitle("Dodawanie, usuwanie i modyfikowanie składników")
@CssImport("./styles/styles.css")
public class ManageIngredientsView extends VerticalLayout implements BeforeEnterObserver {

    private final IngredientApiClient ingredientApiClient;
    private final UserApiClient userApiClient;

    private final Grid<IngredientDTO> grid = new Grid<>(IngredientDTO.class);
    private final TextField filter = new TextField();

    private String userId;
    private UserDTO selectedUser;

    private final TextField username = new TextField("Nazwa użytkownika");
    private final EmailField email = new EmailField("Email");
    private final TextField dailyCalorieIntake = new TextField("Dzienne zapotrzebowanie kaloryczne");
    private final TextField dailyCalorieConsumption = new TextField("Dzienne spalanie kalorii");

    @Autowired
    public ManageIngredientsView(IngredientApiClient ingredientApiClient, UserApiClient userApiClient) {
        this.ingredientApiClient = ingredientApiClient;
        this.userApiClient = userApiClient;

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

        add(userDetailContainer);
        add(createIngredientsView());

        Button addIngredientButton = new Button("Dodaj nowy składnik", event -> getUI().ifPresent(ui -> ui.navigate("add-ingredient")));
        addIngredientButton.addClassName("large-button");
        add(addIngredientButton);

        HorizontalLayout footerButtons = createFooterButtons();
        footerButtons.getStyle().set("position", "absolute");
        footerButtons.getStyle().set("bottom", "50px");
        footerButtons.setWidth("100%");
        footerButtons.setJustifyContentMode(JustifyContentMode.CENTER);
        footerButtons.setSpacing(true);

        add(footerButtons);
        getElement().appendChild(background.getElement());
    }

    private VerticalLayout createIngredientsView() {
        VerticalLayout ingredientsLayout = new VerticalLayout();

        H1 title = new H1("Lista składników");
        title.addClassName("title");

        filter.setPlaceholder("Szukaj po nazwie składnika...");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> updateList());
        filter.addClassName("custom-search-field");

        grid.setColumns("name", "quantity", "calories");

        grid.addComponentColumn(this::createEditButton).setHeader("Edytuj");
        grid.addComponentColumn(this::createDeleteButton).setHeader("Usuń");

        ingredientsLayout.add(title, filter, grid);
        refreshGrid();

        return ingredientsLayout;
    }

    private Button createEditButton(IngredientDTO ingredient) {
        return new Button("Zmień", event -> {
            if (canEditOrDelete(ingredient)) {
            } else {
                Notification.show("Nie można zmienić składnika, ponieważ jest używany w przepisie", 3000, Notification.Position.MIDDLE);
            }
        });
    }

    private Button createDeleteButton(IngredientDTO ingredient) {
        return new Button("Usuń", event -> {
            if (canEditOrDelete(ingredient)) {
                ingredientApiClient.deleteIngredient(ingredient.getId());
                refreshGrid();
            } else {
                Notification.show("Nie można usunąć składnika, ponieważ jest używany w przepisie", 3000, Notification.Position.MIDDLE);
            }
        });
    }

    private boolean canEditOrDelete(IngredientDTO ingredient) {
        return true;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.userId = event.getRouteParameters().get("userId").orElseThrow();
        this.selectedUser = userApiClient.getUserById(Long.parseLong(userId));
        updateUserDetails();
        refreshGrid();
    }

    private void updateUserDetails() {
        username.setValue(selectedUser.getUsername());
        email.setValue(selectedUser.getEmail());
        dailyCalorieIntake.setValue(String.valueOf(selectedUser.getDailyCalorieIntake()));
        dailyCalorieConsumption.setValue(String.valueOf(selectedUser.getDailyCalorieConsumption()));
    }

    private void refreshGrid() {
        List<IngredientDTO> ingredients = ingredientApiClient.getAllIngredients();
        grid.setItems(ingredients);
    }

    private void updateList() {
        List<IngredientDTO> ingredients = ingredientApiClient.getAllIngredients();
        grid.setItems(ingredients.stream()
                .filter(ingredient -> ingredient.getName().toLowerCase().contains(filter.getValue().toLowerCase()))
                .collect(Collectors.toList()));
    }

    private HorizontalLayout createFooterButtons() {
        Button backButton = new Button("Powrót do głównego menu", event -> getUI().ifPresent(ui -> ui.navigate("")));
        backButton.addClassName("black-button");

        Button undoButton = new Button("Cofnij", event -> UI.getCurrent().getPage().executeJs("history.back()"));
        undoButton.addClassName("black-button");

        Button ingredientsListButton = new Button("Przejdź do listy składników", event -> getUI().ifPresent(ui -> ui.navigate("view-ingredients/" + userId)));
        ingredientsListButton.addClassName("black-button");

        HorizontalLayout footerButtons = new HorizontalLayout(undoButton, backButton, ingredientsListButton);
        footerButtons.setSpacing(true);
        return footerButtons;
    }
}