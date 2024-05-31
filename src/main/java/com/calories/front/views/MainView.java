package com.calories.front.views;

import com.calories.front.views.activity.ActivitiesView;
import com.calories.front.views.activity.ActivityCreationView;
import com.calories.front.views.ingredient.IngredientCreationView;
import com.calories.front.views.ingredient.IngredientsView;
import com.calories.front.views.recipe.RecipeCreationView;
import com.calories.front.views.recipe.RecipesView;
import com.calories.front.views.user.UserCreationView;
import com.calories.front.views.user.UsersView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

    public MainView() {

        H1 title = new H1("Welcome to CALORIES CALCULATOR");
        title.getStyle().set("font-size", "var(--lumo-font-size-xl)").set("margin", "20px");

        Button viewUsers = new Button("View Users", event -> UI.getCurrent().navigate(UsersView.class));
        Button createUser = new Button("Create User", event -> UI.getCurrent().navigate(UserCreationView.class));

        Button viewRecipes = new Button("View Recipes", event -> UI.getCurrent().navigate(RecipesView.class));
        Button createRecipe = new Button("Create Recipe", event -> UI.getCurrent().navigate(RecipeCreationView.class));

        Button viewActivities = new Button("View Activities", event -> UI.getCurrent().navigate(ActivitiesView.class));
        Button createActivity = new Button("Create Activity", event -> UI.getCurrent().navigate(ActivityCreationView.class));

        Button viewIngredients = new Button("View Ingredients", event -> UI.getCurrent().navigate(IngredientsView.class));
        Button createIngredients = new Button("Create Ingredients", event -> UI.getCurrent().navigate(IngredientCreationView.class));

        VerticalLayout buttonsLayout = new VerticalLayout(viewUsers, createUser, viewRecipes, createRecipe, viewActivities, createActivity, viewIngredients, createIngredients);
        buttonsLayout.setAlignItems(Alignment.CENTER);

        add(title, buttonsLayout);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();
    }
}
