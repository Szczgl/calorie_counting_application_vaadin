package com.calories.front.views.recipe;

import com.calories.front.api.RecipeApiClient;
import com.calories.front.dto.RecipeDTO;
import com.calories.front.views.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Route("recipe-edit/:recipeId?")
public class RecipeEditView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    private RecipeApiClient recipeApiClient;

    private String recipeId;

    private final FormLayout recipeForm = new FormLayout();
    private final TextField name = new TextField("Name");
    private final TextField description = new TextField("Description");
    private final TextField totalCalories = new TextField("Total Calories");
    private final TextField userId = new TextField("User ID");
    private final TextField ingredientIds = new TextField("Ingredient IDs (comma separated)");

    public RecipeEditView() {
        recipeForm.add(name, description, totalCalories, userId, ingredientIds);
        recipeForm.setSizeFull();
        HorizontalLayout save = createSubmitLayout();
        add(recipeForm, save);
    }

    private HorizontalLayout createSubmitLayout() {
        HorizontalLayout save = new HorizontalLayout();

        Button submit = new Button("Submit changes");
        submit.addClickListener(event -> {
            String recipeName = name.getValue();
            String recipeDescription = description.getValue();
            double recipeTotalCalories = Double.parseDouble(totalCalories.getValue());
            Long recipeUserId = Long.parseLong(userId.getValue());
            Set<Long> recipeIngredientIds = Arrays.stream(ingredientIds.getValue().split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toSet());

            updateRecipeFromForm(recipeName, recipeDescription, recipeTotalCalories, recipeUserId, recipeIngredientIds);

            Notification n = new Notification("Recipe updated successfully");
            n.setPosition(Notification.Position.TOP_CENTER);
            n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            n.setDuration(5000);
            n.open();
            UI.getCurrent().navigate(RecipesView.class);
        });

        Button backToMain = new Button("Main menu");
        backToMain.addClickListener(event -> UI.getCurrent().navigate(MainView.class));

        save.add(submit, backToMain);
        return save;
    }

    private void updateRecipeFromForm(String name, String description, double totalCalories, Long userId, Set<Long> ingredientIds) {
        RecipeDTO dto = new RecipeDTO(Long.parseLong(recipeId), name, description, totalCalories, userId, ingredientIds);
        recipeApiClient.updateRecipe(dto, Long.parseLong(recipeId));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        RecipeDTO recipe = recipeApiClient.getRecipeById(Long.parseLong(recipeId));
        name.setValue(recipe.getName());
        description.setValue(recipe.getDescription());
        totalCalories.setValue(String.valueOf(recipe.getTotalCalories()));
        userId.setValue(String.valueOf(recipe.getUserId()));
        ingredientIds.setValue(recipe.getIngredientIds().stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.recipeId = event.getRouteParameters().get("recipeId").orElseThrow();
    }
}
