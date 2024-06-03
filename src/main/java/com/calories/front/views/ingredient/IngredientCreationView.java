//package com.calories.front.views.ingredient;
//
//import com.calories.front.api.IngredientApiClient;
//import com.calories.front.dto.IngredientDTO;
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
//@Route("ingredient-create")
//public class IngredientCreationView extends VerticalLayout {
//
//    @Autowired
//    private IngredientApiClient ingredientApiClient;
//
//    private final FormLayout ingredientForm = new FormLayout();
//    private final TextField name = new TextField("Name");
//    private final TextField quantity = new TextField("Quantity");
//    private final TextField calories = new TextField("Calories");
//
//    public IngredientCreationView() {
//        name.setRequired(true);
//        quantity.setRequired(true);
//        calories.setRequired(true);
//
//        ingredientForm.add(name, quantity, calories);
//        ingredientForm.setSizeFull();
//
//        HorizontalLayout buttons = createButtonsLayout();
//
//        add(ingredientForm, buttons);
//    }
//
//    private HorizontalLayout createButtonsLayout() {
//        HorizontalLayout functions = new HorizontalLayout();
//
//        Button create = new Button("Create");
//        create.addClickListener(event -> {
//            String ingredientName = name.getValue();
//            String ingredientQuantity = quantity.getValue();
//            String ingredientCalories = calories.getValue();
//
//            IngredientDTO ingredientDTO = new IngredientDTO(null, ingredientName, Integer.parseInt(ingredientQuantity), Integer.parseInt(ingredientCalories));
//
//            try {
//                ingredientApiClient.createIngredient(ingredientDTO);
//                Notification.show("Ingredient created successfully!", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
//                UI.getCurrent().navigate(MainView.class);
//            } catch (Exception e) {
//                Notification.show("Error creating ingredient: " + e.getMessage(), 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
//            }
//        });
//
//        Button backToMain = new Button("Main menu");
//        backToMain.addClickListener(event -> UI.getCurrent().navigate(MainView.class));
//
//        functions.add(create, backToMain);
//        return functions;
//    }
//}