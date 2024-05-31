package com.calories.front.views.ingredient;

import com.calories.front.api.IngredientApiClient;
import com.calories.front.dto.IngredientDTO;
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

@Route("ingredients")
public class IngredientsView extends VerticalLayout {

    private IngredientApiClient ingredientApiClient;

    private final Grid<IngredientDTO> grid = new Grid<>(IngredientDTO.class);

    @Autowired
    public IngredientsView(IngredientApiClient ingredientApiClient) {
        this.ingredientApiClient = ingredientApiClient;

        H1 title = new H1("All Ingredients");
        title.getStyle().set("font-size", "var(--lumo-font-size-m)").set("margin", "0");

        HorizontalLayout buttons = createButtonsLayout();

        grid.setColumns("name", "quantity", "calories");
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.setAllRowsVisible(true);

        grid.addItemClickListener(event -> {
            Long clickedIngredientId = event.getItem().getId();
            UI.getCurrent().navigate(IngredientView.class, new RouteParameters(Map.of("ingredientId", clickedIngredientId.toString())));
        });

        add(title, buttons, grid);
        setSizeFull();
        refreshGrid();
    }

    private HorizontalLayout createButtonsLayout() {
        HorizontalLayout functions = new HorizontalLayout();
        Button create = new Button("Create new");
        create.addClickListener(event -> UI.getCurrent().navigate(IngredientCreationView.class));

        Button backToMain = new Button("Main menu");
        backToMain.addClickListener(event -> UI.getCurrent().navigate(MainView.class));

        functions.add(create, backToMain);
        return functions;
    }

    private void refreshGrid() {
        List<IngredientDTO> ingredients = ingredientApiClient.getAllIngredients();
        grid.setItems(ingredients);
    }
}
