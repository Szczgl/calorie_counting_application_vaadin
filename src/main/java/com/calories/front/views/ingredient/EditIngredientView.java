package com.calories.front.views.ingredient;

import com.calories.front.api.IngredientApiClient;
import com.calories.front.dto.IngredientDTO;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Route("edit-ingredient/:ingredientId")
@PageTitle("Edycja składnika")
@CssImport("./styles/styles.css")
public class EditIngredientView extends VerticalLayout implements BeforeEnterObserver {

    private final IngredientApiClient ingredientApiClient;

    private IngredientDTO ingredient;

    private final TextField name = new TextField("Nazwa");
    private final TextField quantity = new TextField("Ilość");
    private final TextField calories = new TextField("Kalorie");

    private final DecimalFormat decimalFormat;

    @Autowired
    public EditIngredientView(IngredientApiClient ingredientApiClient) {
        this.ingredientApiClient = ingredientApiClient;

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator(',');
        decimalFormat = new DecimalFormat("#.0", symbols);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        Div background = new Div();
        background.addClassName("background");
        background.setSizeFull();

        VerticalLayout formLayout = createFormLayout();

        Button saveButton = new Button("Zapisz", event -> saveIngredient());
        saveButton.addClassName("black-button");

        Button cancelButton = new Button("Anuluj", event -> getUI().ifPresent(ui -> ui.navigate("manage-ingredients")));
        cancelButton.addClassName("black-button");

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonsLayout.setSpacing(true);

        Div container = new Div(formLayout, buttonsLayout);
        container.getStyle().set("background-color", "white");
        container.getStyle().set("padding", "20px");
        container.getStyle().set("border-radius", "10px");
        container.getStyle().set("box-shadow", "0 4px 8px 0 rgba(0, 0, 0, 0.2)");
        container.getStyle().set("margin", "auto");

        add(container);
        getElement().appendChild(background.getElement());
    }

    private VerticalLayout createFormLayout() {
        VerticalLayout formLayout = new VerticalLayout();

        H1 title = new H1("Edycja składnika");
        title.addClassName("title");

        name.setPlaceholder("Nazwa składnika");
        quantity.setPlaceholder("Ilość");
        quantity.setValueChangeMode(ValueChangeMode.EAGER);
        quantity.addValueChangeListener(e -> updateCalories());
        calories.setPlaceholder("Kalorie");
        calories.setReadOnly(true);

        formLayout.add(title, name, quantity, calories);
        formLayout.setAlignItems(Alignment.CENTER);
        formLayout.setSpacing(true);

        return formLayout;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String ingredientId = event.getRouteParameters().get("ingredientId").orElseThrow();
        this.ingredient = ingredientApiClient.getIngredientById(Long.parseLong(ingredientId));
        updateForm();
    }

    private void updateForm() {
        name.setValue(ingredient.getName());
        quantity.setValue(decimalFormat.format(ingredient.getQuantity()));
        calories.setValue(decimalFormat.format(ingredient.getCalories()));
    }

    private void updateCalories() {
        try {
            double newQuantity = decimalFormat.parse(quantity.getValue()).doubleValue();
            double newCalories = calculateCalories(newQuantity);
            calories.setValue(decimalFormat.format(newCalories));
        } catch (Exception e) {
            calories.setValue("");
        }
    }

    private void saveIngredient() {
        if (ingredient != null) {
            try {
                ingredient.setName(name.getValue());
                ingredient.setQuantity(decimalFormat.parse(quantity.getValue()).doubleValue());
                ingredient.setCalories(decimalFormat.parse(calories.getValue()).doubleValue());

                IngredientDTO updatedIngredient = ingredientApiClient.updateIngredient(ingredient, ingredient.getId());
                if (updatedIngredient != null) {
                    Notification.show("Składnik zaktualizowany", 3000, Notification.Position.MIDDLE);
                    getUI().ifPresent(ui -> ui.navigate("manage-ingredients"));
                } else {
                    Notification.show("Błąd podczas aktualizacji składnika", 3000, Notification.Position.MIDDLE);
                }
            } catch (Exception e) {
                Notification.show("Błąd podczas zapisu składnika", 3000, Notification.Position.MIDDLE);
            }
        }
    }

    private double calculateCalories(double quantity) {
        double caloriesPerUnit = ingredient.getCalories() / ingredient.getQuantity();
        return Math.round(caloriesPerUnit * quantity * 10) / 10.0;
    }
}