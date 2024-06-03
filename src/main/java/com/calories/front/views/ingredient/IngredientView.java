//package com.calories.front.views.ingredient;
//
//import com.calories.front.api.IngredientApiClient;
//import com.calories.front.dto.IngredientDTO;
//import com.calories.front.views.MainView;
//import com.vaadin.flow.component.AttachEvent;
//import com.vaadin.flow.component.UI;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.formlayout.FormLayout;
//import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.router.BeforeEnterEvent;
//import com.vaadin.flow.router.BeforeEnterObserver;
//import com.vaadin.flow.router.Route;
//import com.vaadin.flow.router.RouteParameters;
//import org.springframework.beans.factory.annotation.Autowired;
//
//@Route("ingredient-details/:ingredientId")
//public class IngredientView extends VerticalLayout implements BeforeEnterObserver {
//
//    @Autowired
//    private IngredientApiClient ingredientApiClient;
//
//    private String ingredientId;
//
//    private final FormLayout ingredientForm = new FormLayout();
//    private final TextField name = new TextField("Name");
//    private final TextField quantity = new TextField("Quantity");
//    private final TextField calories = new TextField("Calories");
//
//    public IngredientView() {
//        HorizontalLayout buttons = createButtonsLayout();
//        ingredientForm.add(name, quantity, calories);
//        ingredientForm.setSizeFull();
//        add(buttons, ingredientForm);
//    }
//
//    private HorizontalLayout createButtonsLayout() {
//        HorizontalLayout functions = new HorizontalLayout();
//
//        Button edit = new Button("Edit");
//        edit.addClickListener(event -> UI.getCurrent().navigate(IngredientEditView.class, new RouteParameters("ingredientId", ingredientId)));
//
//        Button delete = new Button("Delete");
//        delete.addClickListener(event -> {
//            deleteIngredientById(Long.valueOf(ingredientId));
//            UI.getCurrent().navigate(IngredientsView.class);
//        });
//
//        Button backToMain = new Button("Main menu");
//        backToMain.addClickListener(event -> UI.getCurrent().navigate(MainView.class));
//
//        functions.add(edit, delete, backToMain);
//        return functions;
//    }
//
//    private void deleteIngredientById(Long id) {
//        ingredientApiClient.deleteIngredient(id);
//    }
//
//    @Override
//    protected void onAttach(AttachEvent attachEvent) {
//        super.onAttach(attachEvent);
//        IngredientDTO ingredient = ingredientApiClient.getIngredientById(Long.parseLong(ingredientId));
//        name.setValue(ingredient.getName());
//        quantity.setValue(String.valueOf(ingredient.getQuantity()));
//        calories.setValue(String.valueOf(ingredient.getCalories()));
//    }
//
//    @Override
//    public void beforeEnter(BeforeEnterEvent event) {
//        this.ingredientId = event.getRouteParameters().get("ingredientId").orElseThrow();
//    }
//}
