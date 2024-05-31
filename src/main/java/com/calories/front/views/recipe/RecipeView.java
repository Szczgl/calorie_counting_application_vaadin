package com.calories.front.views.recipe;

import com.calories.front.api.RecipeApiClient;
import com.calories.front.dto.RecipeDTO;
import com.calories.front.views.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import org.springframework.beans.factory.annotation.Autowired;

@Route("recipe-details/:recipeId")
public class RecipeView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    private RecipeApiClient recipeApiClient;

    private String recipeId;

    private final FormLayout recipeForm = new FormLayout();
    private final TextField name = new TextField("Name");
    private final TextArea description = new TextArea("Description");
    private final TextField totalCalories = new TextField("Total Calories");

    public RecipeView() {
        HorizontalLayout buttons = createButtonsLayout();
        recipeForm.add(name, description, totalCalories);
        recipeForm.setSizeFull();
        add(buttons, recipeForm);
    }

    private HorizontalLayout createButtonsLayout() {
        HorizontalLayout functions = new HorizontalLayout();

        Button edit = new Button("Edit");
        edit.addClickListener(event -> UI.getCurrent().navigate(RecipeEditView.class, new RouteParameters("recipeId", recipeId)));

        Button delete = new Button("Delete");
        delete.addClickListener(event -> {
            deleteRecipeById(Long.valueOf(recipeId));
            UI.getCurrent().navigate(RecipesView.class);
        });

        Button backToMain = new Button("Main menu");
        backToMain.addClickListener(event -> UI.getCurrent().navigate(MainView.class));

        functions.add(edit, delete, backToMain);
        return functions;
    }

    private void deleteRecipeById(Long id) {
        recipeApiClient.deleteRecipe(id);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        RecipeDTO recipe = recipeApiClient.getRecipeById(Long.parseLong(recipeId));
        name.setValue(recipe.getName());
        description.setValue(recipe.getDescription());
        totalCalories.setValue(String.valueOf(recipe.getTotalCalories()));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.recipeId = event.getRouteParameters().get("recipeId").orElseThrow();
    }
}
