package com.calories.front.views.recipe;

import com.calories.front.api.RecipeApiClient;
import com.calories.front.api.UserApiClient;
import com.calories.front.dto.IngredientDTO;
import com.calories.front.dto.RecipeDTO;
import com.calories.front.dto.UserDTO;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
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

@Route("view-recipes/:userId")
@PageTitle("Wyświetl przepisy")
@CssImport("./styles/styles.css")
public class ViewRecipesView extends VerticalLayout implements BeforeEnterObserver {

    private final RecipeApiClient recipeApiClient;
    private final UserApiClient userApiClient;

    private final Grid<RecipeDTO> grid = new Grid<>(RecipeDTO.class);
    private final TextField filter = new TextField();
    private final Checkbox userOnlyCheckbox = new Checkbox("Wyświetl tylko przepisy wybranego użytkownika");

    private String userId;
    private UserDTO selectedUser;

    private final TextField username = new TextField("Nazwa użytkownika");
    private final EmailField email = new EmailField("Email");
    private final TextField dailyCalorieIntake = new TextField("Dzienne zapotrzebowanie kaloryczne");
    private final TextField dailyCalorieConsumption = new TextField("Dzienne spalanie kalorii");

    @Autowired
    public ViewRecipesView(RecipeApiClient recipeApiClient, UserApiClient userApiClient) {
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

        userOnlyCheckbox.addValueChangeListener(e -> updateList());
        userOnlyCheckbox.getStyle().set("background-color", "white");
        userOnlyCheckbox.getStyle().set("padding", "5px");
        userOnlyCheckbox.getStyle().set("border-radius", "5px");

        HorizontalLayout filterLayout = new HorizontalLayout(filter, userOnlyCheckbox);
        filterLayout.setWidthFull();
        filterLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        filterLayout.setAlignItems(Alignment.CENTER);

        grid.removeAllColumns();
        grid.addColumn(RecipeDTO::getName).setHeader("Nazwa");
        grid.addColumn(RecipeDTO::getDescription).setHeader("Opis");
        grid.addColumn(RecipeDTO::getTotalCalories).setHeader("Kalorie");
        grid.addComponentColumn(recipe -> {
            Button button = new Button("Podaj listę składników");
            button.addClickListener(click -> showIngredientsDialog(recipe));
            return button;
        }).setHeader("Składniki");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        recipesLayout.add(title, filterLayout, grid);
        refreshGrid();

        return recipesLayout;
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
        List<RecipeDTO> recipes = recipeApiClient.getAllRecipes();
        grid.setItems(recipes);
    }

    private void updateList() {
        List<RecipeDTO> recipes = recipeApiClient.getAllRecipes();
        if (userOnlyCheckbox.getValue()) {
            recipes = recipes.stream()
                    .filter(recipe -> recipe.getUserId().equals(Long.parseLong(userId)))
                    .collect(Collectors.toList());
        }
        grid.setItems(recipes.stream()
                .filter(recipe -> recipe.getName().toLowerCase().contains(filter.getValue().toLowerCase()))
                .collect(Collectors.toList()));
    }

    private void showIngredientsDialog(RecipeDTO recipe) {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");
        dialog.setHeight("300px");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setSpacing(true);

        List<IngredientDTO> ingredients = recipe.getIngredients();
        if (ingredients != null && !ingredients.isEmpty()) {
            ingredients.forEach(ingredient -> dialogLayout.add(new Div(new Span(ingredient.getName() + ": " + ingredient.getQuantity()))));
        } else {
            dialogLayout.add(new Div(new Span("Brak składników")));
        }

        Button closeButton = new Button("Zamknij", event -> dialog.close());
        dialogLayout.add(closeButton);

        dialog.add(dialogLayout);
        dialog.open();
    }

    private HorizontalLayout createFooterButtons() {
        Button backButton = new Button("Powrót do głównego menu", event -> getUI().ifPresent(ui -> ui.navigate("")));
        backButton.addClassName("black-button");

        Button undoButton = new Button("Cofnij", event -> UI.getCurrent().getPage().executeJs("history.back()"));
        undoButton.addClassName("black-button");

        HorizontalLayout footerButtons = new HorizontalLayout(undoButton, backButton);
        footerButtons.setSpacing(true);
        return footerButtons;
    }
}