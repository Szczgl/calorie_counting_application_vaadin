package com.calories.front.views.recipe;

import com.calories.front.api.RecipeApiClient;
import com.calories.front.dto.RecipeDTO;
import com.calories.front.views.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Route("recipes")
public class RecipesView extends VerticalLayout {

    private RecipeApiClient recipeApiClient;

    private final Grid<RecipeDTO> grid = new Grid<>(RecipeDTO.class);

    @Autowired
    public RecipesView(RecipeApiClient recipeApiClient) {
        this.recipeApiClient = recipeApiClient;

        H1 title = new H1("All Recipes");
        title.getStyle().set("font-size", "var(--lumo-font-size-m)").set("margin", "0");

        HorizontalLayout buttons = createButtonsLayout();

        grid.setColumns("name", "description", "totalCalories");
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.setAllRowsVisible(true);

        grid.addItemClickListener(event -> {
            Long clickedRecipeId = event.getItem().getId();
            UI.getCurrent().navigate(RecipeView.class, new RouteParameters(Map.of("recipeId", clickedRecipeId.toString())));
        });

        add(title, buttons, grid);
        setSizeFull();
        refreshGrid();
    }

    private HorizontalLayout createButtonsLayout() {
        HorizontalLayout functions = new HorizontalLayout();
        Button create = new Button("Create new");
        create.addClickListener(event -> UI.getCurrent().navigate(RecipeCreationView.class));

        Button backToMain = new Button("Main menu");
        backToMain.addClickListener(event -> UI.getCurrent().navigate(MainView.class));

        functions.add(create, backToMain);
        return functions;
    }

    private void refreshGrid() {
        List<RecipeDTO> recipes = recipeApiClient.getAllRecipes();
        grid.setItems(recipes);
    }
}