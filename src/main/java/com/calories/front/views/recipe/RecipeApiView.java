//package com.calories.front.views.recipe;
//
//import com.calories.front.api.UserApiClient;
//import com.calories.front.dto.RecipeDTO;
//import com.calories.front.views.user.UserView;
//import com.vaadin.flow.component.UI;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.formlayout.FormLayout;
//import com.vaadin.flow.component.notification.Notification;
//import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.component.textfield.TextArea;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.router.BeforeEnterEvent;
//import com.vaadin.flow.router.BeforeEnterObserver;
//import com.vaadin.flow.router.Route;
//import com.vaadin.flow.router.RouteParameters;
//import com.vaadin.flow.server.VaadinSession;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.Arrays;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Route("add-recipe")
//public class RecipeApiView extends VerticalLayout implements BeforeEnterObserver {
//
//    private UserApiClient userApiClient;
//    private Long userId;
//
//    private final TextField recipeName = new TextField("Nazwa przepisu");
//    private final TextArea recipeDescription = new TextArea("Opis przepisu");
//    private final TextField totalCalories = new TextField("Całkowite kalorie");
//    private final TextField ingredientIds = new TextField("ID składników (oddzielone przecinkami)");
//
//    @Autowired
//    public RecipeApiView(UserApiClient userApiClient) {
//        this.userApiClient = userApiClient;
//        add(createFormLayout(), createButtonsLayout());
//    }
//
//    private FormLayout createFormLayout() {
//        FormLayout formLayout = new FormLayout();
//        formLayout.add(recipeName, recipeDescription, totalCalories, ingredientIds);
//        return formLayout;
//    }
//
//    private HorizontalLayout createButtonsLayout() {
//        Button saveButton = new Button("Zapisz");
//        saveButton.addClickListener(event -> saveRecipe());
//
//        Button cancelButton = new Button("Anuluj");
//        cancelButton.addClickListener(event -> UI.getCurrent().navigate(UserView.class, new RouteParameters("userId", userId.toString())));
//
//        return new HorizontalLayout(saveButton, cancelButton);
//    }
//
//    private void saveRecipe() {
//        String name = recipeName.getValue();
//        String description = recipeDescription.getValue();
//        double calories = Double.parseDouble(totalCalories.getValue());
//        Set<Long> ingredients = Arrays.stream(ingredientIds.getValue().split(","))
//                .map(Long::parseLong)
//                .collect(Collectors.toSet());
//        RecipeDTO recipeDTO = new RecipeDTO(null, name, description, calories, userId, ingredients);
//
//        userApiClient.addRecipeToUser(userId, recipeDTO);
//
//        Notification.show("Przepis dodany pomyślnie", 3000, Notification.Position.TOP_CENTER);
//        UI.getCurrent().navigate(UserView.class, new RouteParameters("userId", userId.toString()));
//    }
//
//    @Override
//    public void beforeEnter(BeforeEnterEvent event) {
//        this.userId = Long.parseLong(event.getRouteParameters().get("userId").orElseThrow(() -> new IllegalStateException("Brak parametru 'userId'")));
//    }
//}
