package com.calories.front.views.ingredient;

import com.calories.front.api.IngredientApiClient;
import com.calories.front.dto.IngredientDTO;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Route("manage-ingredients")
@PageTitle("Dodawanie, usuwanie i modyfikowanie składników")
@CssImport("./styles/styles.css")
public class ManageIngredientsView extends VerticalLayout implements BeforeEnterObserver {

    private final IngredientApiClient ingredientApiClient;

    private final Grid<IngredientDTO> grid = new Grid<>(IngredientDTO.class);
    private final TextField filter = new TextField();

    @Autowired
    public ManageIngredientsView(IngredientApiClient ingredientApiClient) {
        this.ingredientApiClient = ingredientApiClient;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        Div background = new Div();
        background.addClassName("background");
        background.setSizeFull();

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
        grid.getColumnByKey("quantity").setHeader("Ilość");
        grid.getColumnByKey("calories").setHeader("Kalorie");

        grid.addComponentColumn(this::createEditButton).setHeader("Edytuj");
        grid.addComponentColumn(this::createDeleteButton).setHeader("Usuń");

        ingredientsLayout.add(title, filter, grid);
        refreshGrid();

        return ingredientsLayout;
    }

    private Button createEditButton(IngredientDTO ingredient) {
        Button editButton = new Button("Zmień", event -> {
            if (canEditOrDelete(ingredient)) {
                getUI().ifPresent(ui -> ui.navigate("edit-ingredient/" + ingredient.getId()));
            } else {
                Notification.show("Nie można zmienić składnika, ponieważ jest używany w przepisie", 3000, Notification.Position.MIDDLE);
            }
        });
        editButton.addClassName("black-button");
        return editButton;
    }

    private Button createDeleteButton(IngredientDTO ingredient) {
        Button deleteButton = new Button("Usuń", event -> {
            if (canEditOrDelete(ingredient)) {
                ingredientApiClient.deleteIngredient(ingredient.getId());
                refreshGrid();
            } else {
                Notification.show("Nie można usunąć składnika, ponieważ jest używany w przepisie", 3000, Notification.Position.MIDDLE);
            }
        });
        deleteButton.addClassName("black-button");
        return deleteButton;
    }

    private boolean canEditOrDelete(IngredientDTO ingredient) {
        return !ingredientApiClient.isIngredientInAnyRecipe(ingredient.getId());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        refreshGrid();
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

        HorizontalLayout footerButtons = new HorizontalLayout(undoButton, backButton);
        footerButtons.setSpacing(true);
        return footerButtons;
    }
}