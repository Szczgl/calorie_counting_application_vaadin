//package com.calories.front.views.recipe;
//
//import com.calories.front.api.RecipeApiClient;
//import com.calories.front.dto.RecipeDTO;
//import com.calories.front.views.MainView;
//import com.vaadin.flow.component.UI;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.formlayout.FormLayout;
//import com.vaadin.flow.component.notification.Notification;
//import com.vaadin.flow.component.notification.NotificationVariant;
//import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.router.Route;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.Arrays;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Route("recipe-create")
//public class RecipeCreationView extends VerticalLayout {
//
//    @Autowired
//    private RecipeApiClient recipeApiClient;
//
//    private final FormLayout recipeForm = new FormLayout();
//    private final TextField name = new TextField("Name");
//    private final TextField description = new TextField("Description");
//    private final TextField totalCalories = new TextField("Total Calories");
//    private final TextField userId = new TextField("User ID");
//    private final TextField ingredientIds = new TextField("Ingredient IDs (comma separated)");
//
//    public RecipeCreationView() {
//        name.setRequired(true);
//        description.setRequired(true);
//        totalCalories.setRequired(true);
//        userId.setRequired(true);
//        ingredientIds.setRequired(true);
//
//        recipeForm.add(name, description, totalCalories, userId, ingredientIds);
//        recipeForm.setSizeFull();
//
//        HorizontalLayout buttons = createButtonsLayout();
//
//        add(recipeForm, buttons);
//    }
//
//    private HorizontalLayout createButtonsLayout() {
//        HorizontalLayout functions = new HorizontalLayout();
//
//        Button create = new Button("Create");
//        create.addClickListener(event -> {
//            String recipeName = name.getValue();
//            String recipeDescription = description.getValue();
//            double recipeTotalCalories = Double.parseDouble(totalCalories.getValue());
//            Long recipeUserId = Long.parseLong(userId.getValue());
//            Set<Long> recipeIngredientIds = Arrays.stream(ingredientIds.getValue().split(","))
//                    .map(Long::parseLong)
//                    .collect(Collectors.toSet());
//
//            if (recipeName.isEmpty() || recipeDescription.isEmpty()) {
//                Notification fail = new Notification();
//                fail.setText("Please fill in all required fields");
//                fail.setDuration(3000);
//                fail.setPosition(Notification.Position.TOP_CENTER);
//                fail.addThemeVariants(NotificationVariant.LUMO_WARNING);
//                fail.open();
//            } else {
//                createRecipe(recipeName, recipeDescription, recipeTotalCalories, recipeUserId, recipeIngredientIds);
//                UI.getCurrent().navigate(RecipesView.class);
//            }
//        });
//
//        Button backToMain = new Button("Main menu");
//        backToMain.addClickListener(event -> UI.getCurrent().navigate(MainView.class));
//
//        functions.add(create, backToMain);
//        return functions;
//    }
//
//    private void createRecipe(String name, String description, double totalCalories, Long userId, Set<Long> ingredientIds) {
//        RecipeDTO dto = new RecipeDTO(null, name, description, totalCalories, userId, ingredientIds);
//        recipeApiClient.createRecipe(dto);
//    }
//}