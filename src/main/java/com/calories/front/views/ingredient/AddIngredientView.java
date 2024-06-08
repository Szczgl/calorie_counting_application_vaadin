package com.calories.front.views.ingredient;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("add-ingredient")
@PageTitle("Dodaj składnik")
@CssImport("./styles/styles.css")
public class AddIngredientView extends VerticalLayout {

    public AddIngredientView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        Div background = new Div();
        background.addClassName("background");
        background.setSizeFull();

        FormLayout formLayout = new FormLayout();
        TextField ingredientName = new TextField("Nazwa składnika");
        TextField ingredientQuantity = new TextField("Ilość");
        TextField ingredientCalories = new TextField("Kalorie");

        formLayout.add(ingredientName, ingredientQuantity, ingredientCalories);

        Button saveButton = new Button("Zapisz", event -> {});
        saveButton.addClassName("black-button");

        Button backButton = new Button("Cofnij", event -> UI.getCurrent().getPage().executeJs("history.back()"));
        backButton.addClassName("black-button");

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, backButton);
        buttonsLayout.setSpacing(true);

        add(formLayout, buttonsLayout);
        getElement().appendChild(background.getElement());
    }
}