package com.calories.front.views.ingredient;

import com.calories.front.api.IngredientApiClient;
import com.calories.front.dto.IngredientDTO;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

@Route("add-ingredient")
@PageTitle("Dodawanie nowego składnika")
@CssImport("./styles/styles.css")
public class AddIngredientView extends VerticalLayout implements BeforeEnterObserver {

    private final IngredientApiClient ingredientApiClient;

    private final TextField nameField = new TextField("Nazwa");
    private final TextField quantityField = new TextField("Ilość");
    private final TextField caloriesField = new TextField("Kalorie");
    private final DecimalFormat decimalFormat;

    private String returnUrl;

    @Autowired
    public AddIngredientView(IngredientApiClient ingredientApiClient) {
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

        Div formContainer = new Div();
        formContainer.getStyle().set("background-color", "white");
        formContainer.getStyle().set("padding", "20px");
        formContainer.getStyle().set("border-radius", "10px");
        formContainer.getStyle().set("box-shadow", "0 4px 8px 0 rgba(0, 0, 0, 0.2)");
        formContainer.getStyle().set("margin", "50px auto 0 auto");
        formContainer.setWidth("400px");

        VerticalLayout formLayout = createFormLayout();
        formContainer.add(formLayout);

        add(formContainer);
        getElement().appendChild(background.getElement());
    }

    private VerticalLayout createFormLayout() {
        VerticalLayout formLayout = new VerticalLayout();

        nameField.setPlaceholder("Nazwa składnika");
        quantityField.setPlaceholder("Ilość");
        caloriesField.setPlaceholder("Kalorie");

        Button checkButton = new Button("Sprawdź w bazie", event -> checkIngredientInDatabase());
        checkButton.addClassName("black-button");

        Button saveButton = new Button("Zapisz", event -> saveIngredient());
        saveButton.addClassName("black-button");

        Button cancelButton = new Button("Anuluj", event -> navigateBack());
        cancelButton.addClassName("black-button");

        HorizontalLayout nameLayout = new HorizontalLayout(nameField, checkButton);
        nameLayout.setSpacing(true);

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonsLayout.setSpacing(true);

        formLayout.add(nameLayout, quantityField, caloriesField, buttonsLayout);
        formLayout.setAlignItems(Alignment.CENTER);
        formLayout.setSpacing(true);

        return formLayout;
    }

    private void checkIngredientInDatabase() {
        String name = nameField.getValue().trim();
        if (name.isEmpty()) {
            Notification.show("Proszę wpisać nazwę składnika", 3000, Notification.Position.MIDDLE);
            return;
        }
        try {
            IngredientDTO ingredient = ingredientApiClient.searchIngredientInEdamam(name);
            if (ingredient != null) {
                quantityField.setValue(String.valueOf(ingredient.getQuantity()));
                caloriesField.setValue(String.valueOf(ingredient.getCalories()));
            } else {
                Notification.show("Składnik nie znaleziony w bazie. Proszę wpisać wartości ręcznie.", 3000, Notification.Position.MIDDLE);
            }
        } catch (Exception e) {
            Notification.show("Błąd podczas wyszukiwania składnika: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void saveIngredient() {
        try {
            String name = nameField.getValue().trim();
            if (ingredientApiClient.existsByName(name)) {
                Notification.show("Składnik o tej nazwie już istnieje", 3000, Notification.Position.MIDDLE);
                return;
            }

            IngredientDTO newIngredient = new IngredientDTO();
            newIngredient.setName(name);
            newIngredient.setQuantity(Double.parseDouble(quantityField.getValue().replace(",", ".")));
            newIngredient.setCalories(Double.parseDouble(caloriesField.getValue().replace(",", ".")));

            ingredientApiClient.createIngredient(newIngredient);

            Notification.show("Składnik zapisany", 3000, Notification.Position.MIDDLE);
            navigateBack();
        } catch (NumberFormatException e) {
            Notification.show("Błąd: Nieprawidłowy format liczby", 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("Błąd podczas zapisu składnika", 3000, Notification.Position.MIDDLE);
        }
    }

    private void navigateBack() {
        if (returnUrl != null) {
            UI.getCurrent().navigate(returnUrl);
        } else {
            UI.getCurrent().navigate("");
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.returnUrl = event.getLocation().getQueryParameters().getParameters().getOrDefault("returnUrl", List.of("")).get(0);
    }
}