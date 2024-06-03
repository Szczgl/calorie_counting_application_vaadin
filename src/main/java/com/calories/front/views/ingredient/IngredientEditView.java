//package com.calories.front.views.ingredient;
//
//import com.calories.front.api.IngredientApiClient;
//import com.calories.front.dto.IngredientDTO;
//import com.calories.front.views.MainView;
//import com.vaadin.flow.component.AttachEvent;
//import com.vaadin.flow.component.UI;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.formlayout.FormLayout;
//import com.vaadin.flow.component.notification.Notification;
//import com.vaadin.flow.component.notification.NotificationVariant;
//import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.router.BeforeEnterEvent;
//import com.vaadin.flow.router.BeforeEnterObserver;
//import com.vaadin.flow.router.Route;
//import org.springframework.beans.factory.annotation.Autowired;
//
//@Route("ingredient-edit/:ingredientId?")
//public class IngredientEditView extends VerticalLayout implements BeforeEnterObserver {
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
//    public IngredientEditView() {
//        ingredientForm.add(name, quantity, calories);
//        ingredientForm.setSizeFull();
//        HorizontalLayout save = createSubmitLayout();
//        add(ingredientForm, save);
//    }
//
//    private HorizontalLayout createSubmitLayout() {
//        HorizontalLayout save = new HorizontalLayout();
//
//        Button submit = new Button("Submit changes");
//        submit.addClickListener(event -> {
//            String ingredientName = name.getValue();
//            double ingredientQuantity = Double.parseDouble(quantity.getValue());
//            double ingredientCalories = Double.parseDouble(calories.getValue());
//
//            updateIngredientFromForm(ingredientName, ingredientQuantity, ingredientCalories);
//
//            Notification n = new Notification("Ingredient updated successfully");
//            n.setPosition(Notification.Position.TOP_CENTER);
//            n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
//            n.setDuration(5000);
//            n.open();
//            UI.getCurrent().navigate(IngredientsView.class);
//        });
//
//        Button backToMain = new Button("Main menu");
//        backToMain.addClickListener(event -> UI.getCurrent().navigate(MainView.class));
//
//        save.add(submit, backToMain);
//        return save;
//    }
//
//    private void updateIngredientFromForm(String name, double quantity, double calories) {
//        IngredientDTO dto = new IngredientDTO(Long.parseLong(ingredientId), name, quantity, calories);
//        ingredientApiClient.updateIngredient(dto, Long.parseLong(ingredientId));
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
