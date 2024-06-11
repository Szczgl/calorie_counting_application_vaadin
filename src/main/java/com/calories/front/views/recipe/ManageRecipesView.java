package com.calories.front.views.recipe;

import com.calories.front.api.RecipeApiClient;
import com.calories.front.api.UserApiClient;
import com.calories.front.dto.RecipeDTO;
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

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@Route("manage-recipes/:userId")
@PageTitle("Dodawanie, usuwanie i modyfikowanie przepisów")
@CssImport("./styles/styles.css")
public class ManageRecipesView extends VerticalLayout implements BeforeEnterObserver {

    private final RecipeApiClient recipeApiClient;
    private final UserApiClient userApiClient;

    private final Grid<RecipeDTO> grid = new Grid<>(RecipeDTO.class);
    private final TextField filter = new TextField();

    private String userId;
    private UserDTO selectedUser;

    private final TextField username = new TextField("Nazwa użytkownika");
    private final EmailField email = new EmailField("Email");
    private final TextField dailyCalorieIntake = new TextField("Dzienne zapotrzebowanie kaloryczne");
    private final TextField dailyCalorieConsumption = new TextField("Dzienne spalanie kalorii");

    private final DecimalFormat decimalFormat = new DecimalFormat("#.0");

    @Autowired
    public ManageRecipesView(RecipeApiClient recipeApiClient, UserApiClient userApiClient) {
        this.recipeApiClient = recipeApiClient;
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
        add(createRecipesView());

        Button addRecipeButton = new Button("Dodaj nowy przepis", event -> getUI().ifPresent(ui -> ui.navigate("add-recipe/" + userId)));
        addRecipeButton.addClassName("large-button");
        add(addRecipeButton);

        HorizontalLayout footerButtons = createFooterButtons();
        footerButtons.getStyle().set("position", "absolute");
        footerButtons.getStyle().set("bottom", "50px");
        footerButtons.setWidth("100%");
        footerButtons.setJustifyContentMode(JustifyContentMode.CENTER);
        footerButtons.setSpacing(true);

        add(footerButtons);
        getElement().appendChild(background.getElement());
    }

    private VerticalLayout createRecipesView() {
        VerticalLayout recipesLayout = new VerticalLayout();

        H1 title = new H1("Lista przepisów");
        title.addClassName("title");

        filter.setPlaceholder("Szukaj po nazwie przepisu...");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> updateList());
        filter.addClassName("custom-search-field");

        grid.setColumns("name", "description", "totalCalories");

        grid.addComponentColumn(this::createEditButton).setHeader("Edytuj");
        grid.addComponentColumn(this::createDeleteButton).setHeader("Usuń");

        recipesLayout.add(title, filter, grid);
        refreshGrid();

        return recipesLayout;
    }

    private Button createEditButton(RecipeDTO recipe) {
        Button editButton = new Button("Zmień", event -> UI.getCurrent().navigate("edit-recipe/" + recipe.getId()));
        editButton.addClassName("black-button");
        return editButton;
    }

    private Button createDeleteButton(RecipeDTO recipe) {
        Button deleteButton = new Button("Usuń", event -> {
            recipeApiClient.deleteRecipe(recipe.getId());
            refreshGrid();
            Notification.show("Przepis usunięty", 3000, Notification.Position.MIDDLE);
        });
        deleteButton.addClassName("black-button");
        return deleteButton;
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
        dailyCalorieIntake.setValue(decimalFormat.format(selectedUser.getDailyCalorieIntake()));
        updateUserCalorieConsumption();
    }

    private void updateUserCalorieConsumption() {
        List<RecipeDTO> recipes = recipeApiClient.getAllRecipes();
        double totalCalories = recipes.stream()
                .filter(recipe -> recipe.getUserId().equals(Long.parseLong(userId)))
                .mapToDouble(RecipeDTO::getTotalCalories)
                .sum();
        dailyCalorieConsumption.setValue(decimalFormat.format(totalCalories));
    }

    private void refreshGrid() {
        List<RecipeDTO> recipes = recipeApiClient.getAllRecipes();
        grid.setItems(recipes);
    }

    private void updateList() {
        List<RecipeDTO> recipes = recipeApiClient.getAllRecipes();
        grid.setItems(recipes.stream()
                .filter(recipe -> recipe.getName().toLowerCase().contains(filter.getValue().toLowerCase()))
                .collect(Collectors.toList()));
    }

    private HorizontalLayout createFooterButtons() {
        Button backButton = new Button("Powrót do głównego menu", event -> getUI().ifPresent(ui -> ui.navigate("")));
        backButton.addClassName("black-button");

        Button undoButton = new Button("Cofnij", event -> UI.getCurrent().getPage().executeJs("history.back()"));
        undoButton.addClassName("black-button");

        Button recipesListButton = new Button("Przejdź do listy przepisów", event -> getUI().ifPresent(ui -> ui.navigate("view-recipes/" + userId)));
        recipesListButton.addClassName("black-button");

        HorizontalLayout footerButtons = new HorizontalLayout(undoButton, backButton, recipesListButton);
        footerButtons.setSpacing(true);
        return footerButtons;
    }
}