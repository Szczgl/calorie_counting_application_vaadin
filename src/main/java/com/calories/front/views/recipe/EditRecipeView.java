package com.calories.front.views.recipe;

import com.calories.front.api.IngredientApiClient;
import com.calories.front.api.RecipeApiClient;
import com.calories.front.dto.IngredientDTO;
import com.calories.front.dto.RecipeDTO;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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

    private List<IngredientDTO> availableIngredients;
    private List<IngredientDTO> recipeIngredients;

    private final Grid<IngredientDTO> availableIngredientsGrid = new Grid<>(IngredientDTO.class);
    private final Grid<IngredientDTO> recipeIngredientsGrid = new Grid<>(IngredientDTO.class);

    private final DecimalFormat decimalFormat;
    private String userId;
    private String recipeId;

    @Autowired
    public EditRecipeView(RecipeApiClient recipeApiClient, IngredientApiClient ingredientApiClient) {
        this.recipeApiClient = recipeApiClient;
        this.ingredientApiClient = ingredientApiClient;

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator(',');
        decimalFormat = new DecimalFormat("#.0", symbols);

        setSizeFull();
        setAlignItems(Alignment.START);
        setJustifyContentMode(JustifyContentMode.START);

        Div background = new Div();
        background.addClassName("background");
        background.setSizeFull();

        Div formContainer = new Div();
        formContainer.getStyle().set("background-color", "white");
        formContainer.getStyle().set("padding", "20px");
        formContainer.getStyle().set("border-radius", "10px");
        formContainer.getStyle().set("margin", "20px auto");

        VerticalLayout formLayout = createFormLayout();
        formContainer.add(formLayout);

        Button saveButton = new Button("Zapisz", event -> saveRecipe());
        saveButton.addClassName("black-button");

        Button cancelButton = new Button("Anuluj", event -> UI.getCurrent().navigate("manage-recipes/" + userId));
        cancelButton.addClassName("black-button");

        Button addIngredientButton = new Button("Dodaj nowy składnik", event -> navigateToAddIngredient());
        addIngredientButton.addClassName("black-button");

        HorizontalLayout leftButtonsLayout = new HorizontalLayout(addIngredientButton);
        leftButtonsLayout.setSpacing(true);
        leftButtonsLayout.setJustifyContentMode(JustifyContentMode.START);

        HorizontalLayout rightButtonsLayout = new HorizontalLayout(saveButton, cancelButton);
        rightButtonsLayout.setSpacing(true);
        rightButtonsLayout.setJustifyContentMode(JustifyContentMode.END);

        HorizontalLayout buttonsLayout = new HorizontalLayout(leftButtonsLayout, rightButtonsLayout);
        buttonsLayout.setWidthFull();
        buttonsLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        formLayout.add(buttonsLayout);

        add(formContainer);
        getElement().appendChild(background.getElement());
    }

    private VerticalLayout createFormLayout() {
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.setPadding(true);
        formLayout.setSpacing(true);
        formLayout.setWidthFull();

        H1 title = new H1("Edycja istniejącego przepisu");
        title.addClassName("title");

        nameField.setWidthFull();
        descriptionField.setWidthFull();
        totalCaloriesField.setWidthFull();
        totalCaloriesField.setReadOnly(true);

        VerticalLayout recipeDetailsLayout = new VerticalLayout(nameField, descriptionField, totalCaloriesField);
        recipeDetailsLayout.setPadding(false);
        recipeDetailsLayout.setSpacing(true);
        recipeDetailsLayout.setWidthFull();

        VerticalLayout recipeIngredientsLayout = createRecipeIngredientsGrid();
        recipeIngredientsLayout.setPadding(false);
        recipeIngredientsLayout.setSpacing(true);
        recipeIngredientsLayout.setWidthFull();
        recipeIngredientsLayout.setHeight("350px");

        VerticalLayout availableIngredientsLayout = createAvailableIngredientsGrid();
        availableIngredientsLayout.setPadding(false);
        availableIngredientsLayout.setSpacing(true);
        availableIngredientsLayout.setWidthFull();
        availableIngredientsLayout.setHeight("350px");

        formLayout.add(title, recipeDetailsLayout, recipeIngredientsLayout, availableIngredientsLayout);
        return formLayout;
    }

    private VerticalLayout createAvailableIngredientsGrid() {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(true);
        layout.setSpacing(true);
        layout.setWidthFull();

        TextField filter = new TextField();
        filter.setPlaceholder("Szukaj składników...");
        filter.setWidthFull();
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> updateAvailableIngredientsList(filter.getValue()));

        availableIngredientsGrid.setColumns("name", "quantity", "calories");
        availableIngredientsGrid.getColumnByKey("name").setWidth("250px").setFlexGrow(0);
        availableIngredientsGrid.getColumnByKey("quantity").setHeader("Ilość").setWidth("100px").setFlexGrow(0);
        availableIngredientsGrid.getColumnByKey("calories").setHeader("Kalorie").setWidth("100px").setFlexGrow(0);

        availableIngredientsGrid.addComponentColumn(this::createAddButton).setHeader("Dodaj").setWidth("100px").setFlexGrow(0);

        layout.add(filter, availableIngredientsGrid);
        return layout;
    }

    private VerticalLayout createRecipeIngredientsGrid() {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(true);
        layout.setSpacing(true);
        layout.setWidthFull();

        TextField filter = new TextField();
        filter.setPlaceholder("Szukaj składników w przepisie...");
        filter.setWidthFull();
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> updateRecipeIngredientsList(filter.getValue()));

        recipeIngredientsGrid.setColumns("name", "quantity", "calories");
        recipeIngredientsGrid.getColumnByKey("name").setWidth("250px").setFlexGrow(0);
        recipeIngredientsGrid.getColumnByKey("quantity").setHeader("Ilość").setWidth("100px").setFlexGrow(0);
        recipeIngredientsGrid.getColumnByKey("calories").setHeader("Kalorie").setWidth("100px").setFlexGrow(0);

        recipeIngredientsGrid.addComponentColumn(this::createEditButton).setHeader("Edytuj").setWidth("100px").setFlexGrow(0);
        recipeIngredientsGrid.addComponentColumn(this::createRemoveButton).setHeader("Usuń").setWidth("100px").setFlexGrow(0);

        layout.add(filter, recipeIngredientsGrid);
        return layout;
    }

    private Button createAddButton(IngredientDTO ingredient) {
        Button addButton = new Button("Dodaj", event -> {
            Dialog dialog = new Dialog();
            TextField quantityField = new TextField("Ilość");
            Button confirmButton = new Button("Potwierdź", e -> {
                try {
                    double quantity = decimalFormat.parse(quantityField.getValue().replace(",", ".")).doubleValue();
                    IngredientDTO ingredientInRecipe = new IngredientDTO();
                    ingredientInRecipe.setName(ingredient.getName());
                    ingredientInRecipe.setQuantity(quantity);
                    ingredientInRecipe.setCalories((ingredient.getCalories() / ingredient.getQuantity()) * quantity);

                    recipeIngredients.add(ingredientInRecipe);
                    updateTotalCalories();
                    updateRecipeIngredientsList("");

                    dialog.close();
                } catch (NumberFormatException | java.text.ParseException ex) {
                    Notification.show("Nieprawidłowy format liczby", 3000, Notification.Position.MIDDLE);
                }
            });
            dialog.add(quantityField, confirmButton);
            dialog.open();
        });
        addButton.addClassName("black-button");
        return addButton;
    }

    private Button createEditButton(IngredientDTO ingredient) {
        Button editButton = new Button("Edytuj", event -> {
            Dialog dialog = new Dialog();
            TextField quantityField = new TextField("Ilość", decimalFormat.format(ingredient.getQuantity()));
            Button confirmButton = new Button("Potwierdź", e -> {
                try {
                    double quantity = decimalFormat.parse(quantityField.getValue().replace(",", ".")).doubleValue();
                    double previousQuantity = ingredient.getQuantity();
                    ingredient.setQuantity(quantity);
                    ingredient.setCalories((ingredient.getCalories() / previousQuantity) * quantity);

                    updateTotalCalories();
                    updateRecipeIngredientsList("");

                    dialog.close();
                } catch (NumberFormatException | java.text.ParseException ex) {
                    Notification.show("Nieprawidłowy format liczby", 3000, Notification.Position.MIDDLE);
                }
            });
            dialog.add(quantityField, confirmButton);
            dialog.open();
        });
        editButton.addClassName("black-button");
        return editButton;
    }

    private Button createRemoveButton(IngredientDTO ingredient) {
        Button removeButton = new Button("Usuń", event -> {
            recipeIngredients.remove(ingredient);
            updateTotalCalories();
            updateRecipeIngredientsList("");
        });
        removeButton.addClassName("black-button");
        return removeButton;
    }

    private void updateAvailableIngredientsList(String filter) {
        List<IngredientDTO> filteredIngredients = availableIngredients.stream()
                .filter(ingredient -> ingredient.getName().toLowerCase().contains(filter.toLowerCase()))
                .collect(Collectors.toList());
        availableIngredientsGrid.setItems(filteredIngredients);
    }

    private void updateRecipeIngredientsList(String filter) {
        List<IngredientDTO> filteredIngredients = recipeIngredients.stream()
                .filter(ingredient -> ingredient.getName().toLowerCase().contains(filter.toLowerCase()))
                .collect(Collectors.toList());
        recipeIngredientsGrid.setItems(filteredIngredients);
    }

    private void updateTotalCalories() {
        double totalCalories = recipeIngredients.stream()
                .mapToDouble(IngredientDTO::getCalories)
                .sum();
        totalCaloriesField.setValue(decimalFormat.format(totalCalories));
    }

    private void saveRecipe() {
        if (recipe != null) {
            try {
                recipe.setName(nameField.getValue().trim());
                recipe.setDescription(descriptionField.getValue().trim());
                recipe.setIngredients(recipeIngredients);
                recipe.setTotalCalories(recipeIngredients.stream().mapToDouble(IngredientDTO::getCalories).sum());

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

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        RouteParameters parameters = event.getRouteParameters();
        recipeId = parameters.get("recipeId").orElseThrow(() -> new IllegalArgumentException("Brak ID przepisu"));

        recipe = recipeApiClient.getRecipeById(Long.parseLong(recipeId));
        if (recipe != null) {
            userId = String.valueOf(recipe.getUserId());
            updateForm();
        } else {
            Notification.show("Przepis nie został znaleziony", 3000, Notification.Position.MIDDLE);
            UI.getCurrent().navigate("manage-recipes");
        }
    }

    private void updateForm() {
        nameField.setValue(recipe.getName());
        descriptionField.setValue(recipe.getDescription());
        totalCaloriesField.setValue(decimalFormat.format(recipe.getTotalCalories()));
        recipeIngredients = recipe.getIngredients();

        availableIngredients = ingredientApiClient.getAllIngredients();

        updateAvailableIngredientsList("");
        updateRecipeIngredientsList("");
    }

    private void navigateToAddIngredient() {
        UI.getCurrent().navigate("add-ingredient?returnUrl=" + getReturnUrl());
    }

    private String getReturnUrl() {
        return "edit-recipe/" + recipeId;
    }
}