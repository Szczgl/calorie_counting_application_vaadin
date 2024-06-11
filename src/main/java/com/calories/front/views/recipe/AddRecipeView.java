package com.calories.front.views.recipe;

import com.calories.front.api.IngredientApiClient;
import com.calories.front.api.RecipeApiClient;
import com.calories.front.dto.IngredientDTO;
import com.calories.front.dto.RecipeDTO;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Route("add-recipe/:userId")
@PageTitle("Dodawanie nowego przepisu")
@CssImport("./styles/styles.css")
public class AddRecipeView extends VerticalLayout implements BeforeEnterObserver {

    private final RecipeApiClient recipeApiClient;
    private final IngredientApiClient ingredientApiClient;

    private final TextField nameField = new TextField("Nazwa");
    private final TextArea descriptionField = new TextArea("Opis");
    private final TextField totalCaloriesField = new TextField("Kalorie");

    private String userId;

    private List<IngredientDTO> availableIngredients;
    private List<IngredientDTO> recipeIngredients;

    @Autowired
    public AddRecipeView(RecipeApiClient recipeApiClient, IngredientApiClient ingredientApiClient) {
        this.recipeApiClient = recipeApiClient;
        this.ingredientApiClient = ingredientApiClient;

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
        formContainer.getStyle().set("box-shadow", "0 4px 8px 0 rgba(0, 0, 0, 0.2)");
        formContainer.getStyle().set("margin", "50px auto 0 auto");
        formContainer.setWidth("400px");

        VerticalLayout formLayout = createFormLayout();
        formContainer.add(formLayout);

        add(formContainer);
        getElement().appendChild(background.getElement());
    }

    private VerticalLayout createFormLayout() {
        VerticalLayout formLayout = new VerticalLayout();

        nameField.setPlaceholder("Nazwa przepisu");
        descriptionField.setPlaceholder("Opis");
        totalCaloriesField.setPlaceholder("Kalorie");
        totalCaloriesField.setReadOnly(true);

        Button saveButton = new Button("Zapisz", event -> saveRecipe());
        saveButton.addClassName("black-button");

        Button cancelButton = new Button("Anuluj", event -> UI.getCurrent().navigate("manage-recipes/" + userId));
        cancelButton.addClassName("black-button");

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonsLayout.setSpacing(true);

        formLayout.add(nameField, descriptionField, totalCaloriesField, buttonsLayout);
        formLayout.setAlignItems(Alignment.CENTER);
        formLayout.setSpacing(true);

        return formLayout;
    }

    private void saveRecipe() {
        try {
            RecipeDTO newRecipe = new RecipeDTO();
            newRecipe.setName(nameField.getValue().trim());
            newRecipe.setDescription(descriptionField.getValue().trim());
            newRecipe.setUserId(Long.parseLong(userId));
            newRecipe.setIngredients(recipeIngredients);
            newRecipe.setTotalCalories(recipeIngredients.stream().mapToDouble(IngredientDTO::getCalories).sum());

            RecipeDTO createdRecipe = recipeApiClient.createRecipe(newRecipe);
            if (createdRecipe != null) {
                Notification.show("Przepis zapisany", 3000, Notification.Position.MIDDLE);
                UI.getCurrent().navigate("manage-recipes/" + userId);
            } else {
                Notification.show("Błąd podczas zapisu przepisu", 3000, Notification.Position.MIDDLE);
            }
        } catch (Exception e) {
            Notification.show("Błąd podczas zapisu przepisu: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        RouteParameters parameters = event.getRouteParameters();
        userId = parameters.get("userId").orElse("");
        if (userId.isEmpty()) {
            Notification.show("Brak wymaganych parametrów.", 3000, Notification.Position.MIDDLE);
            UI.getCurrent().navigate("users");
        }

        availableIngredients = ingredientApiClient.getAllIngredients();
        recipeIngredients = new ArrayList<>();
    }
}
