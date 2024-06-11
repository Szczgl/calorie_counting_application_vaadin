package com.calories.front.views.recipe;

import com.calories.front.api.IngredientApiClient;
import com.calories.front.api.RecipeApiClient;
import com.calories.front.dto.RecipeDTO;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Route("edit-recipe/:recipeId")
@PageTitle("Edycja przepisu")
@CssImport("./styles/styles.css")
public class EditRecipeView extends VerticalLayout implements BeforeEnterObserver {

    private final RecipeApiClient recipeApiClient;
    private final IngredientApiClient ingredientApiClient;

    private RecipeDTO recipe;

    private final TextField nameField = new TextField("Nazwa");
    private final TextArea descriptionField = new TextArea("Opis");
    private final TextField totalCaloriesField = new TextField("Kalorie");

    private final DecimalFormat decimalFormat;

    @Autowired
    public EditRecipeView(RecipeApiClient recipeApiClient, IngredientApiClient ingredientApiClient) {
        this.recipeApiClient = recipeApiClient;
        this.ingredientApiClient = ingredientApiClient;

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator(',');
        decimalFormat = new DecimalFormat("#.0", symbols);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        Div background = new Div();
        background.addClassName("background");
        background.setSizeFull();

        VerticalLayout formLayout = createFormLayout();

        Button saveButton = new Button("Zapisz", event -> saveRecipe());
        saveButton.addClassName("black-button");

        Button cancelButton = new Button("Anuluj", event -> UI.getCurrent().navigate("manage-recipes/" + recipe.getUserId()));
        cancelButton.addClassName("black-button");

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonsLayout.setSpacing(true);

        Div container = new Div(formLayout, buttonsLayout);
        container.getStyle().set("background-color", "white");
        container.getStyle().set("padding", "20px");
        container.getStyle().set("border-radius", "10px");
        container.getStyle().set("box-shadow", "0 4px 8px 0 rgba(0, 0, 0, 0.2)");
        container.getStyle().set("margin", "auto");

        add(container);
        getElement().appendChild(background.getElement());
    }

    private VerticalLayout createFormLayout() {
        VerticalLayout formLayout = new VerticalLayout();

        H1 title = new H1("Edycja przepisu");
        title.addClassName("title");

        nameField.setReadOnly(false);
        descriptionField.setReadOnly(false);
        totalCaloriesField.setReadOnly(true);

        formLayout.add(title, nameField, descriptionField, totalCaloriesField);
        formLayout.setAlignItems(Alignment.CENTER);
        formLayout.setSpacing(true);

        return formLayout;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String recipeId = event.getRouteParameters().get("recipeId").orElseThrow();
        this.recipe = recipeApiClient.getRecipeById(Long.parseLong(recipeId));
        updateForm();
    }

    private void updateForm() {
        nameField.setValue(recipe.getName());
        descriptionField.setValue(recipe.getDescription());
        totalCaloriesField.setValue(decimalFormat.format(recipe.getTotalCalories()));
    }

    private void saveRecipe() {
        if (recipe != null) {
            try {
                recipe.setName(nameField.getValue().trim());
                recipe.setDescription(descriptionField.getValue().trim());

                RecipeDTO updatedRecipe = recipeApiClient.updateRecipe(recipe, recipe.getId());
                if (updatedRecipe != null) {
                    Notification.show("Przepis zaktualizowany", 3000, Notification.Position.MIDDLE);
                    getUI().ifPresent(ui -> ui.navigate("manage-recipes/" + recipe.getUserId()));
                } else {
                    Notification.show("Błąd podczas aktualizacji przepisu", 3000, Notification.Position.MIDDLE);
                }
            } catch (Exception e) {
                Notification.show("Błąd podczas zapisu przepisu", 3000, Notification.Position.MIDDLE);
            }
        }
    }
}
