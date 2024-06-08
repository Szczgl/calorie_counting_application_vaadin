package com.calories.front.views.activity;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("add-activity")
@PageTitle("Dodaj aktywność fizyczną")
@CssImport("./styles/styles.css")
public class AddActivityView extends VerticalLayout {

    public AddActivityView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        Div background = new Div();
        background.addClassName("background");
        background.setSizeFull();

        FormLayout formLayout = new FormLayout();
        TextField activityName = new TextField("Nazwa aktywności");
        TextArea activityDescription = new TextArea("Opis aktywności");
        TextField activityCalories = new TextField("Kalorie");

        formLayout.add(activityName, activityDescription, activityCalories);

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
