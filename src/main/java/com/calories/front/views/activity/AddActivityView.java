package com.calories.front.views.activity;

import com.calories.front.api.ActivityApiClient;
import com.calories.front.api.ExerciseApiClient;
import com.calories.front.dto.ActivityDTO;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route("add-activity/:userId")
@PageTitle("Dodawanie nowej aktywności")
@CssImport("./styles/styles.css")
public class AddActivityView extends VerticalLayout implements BeforeEnterObserver {

    private final ActivityApiClient activityApiClient;
    private final ExerciseApiClient exerciseApiClient;

    private final TextField nameField = new TextField("Nazwa");
    private final TextArea descriptionField = new TextArea("Opis");
    private final TextField consumedCaloriesField = new TextField("Kalorie");

    private String userId;

    @Autowired
    public AddActivityView(ActivityApiClient activityApiClient, ExerciseApiClient exerciseApiClient) {
        this.activityApiClient = activityApiClient;
        this.exerciseApiClient = exerciseApiClient;

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

        nameField.setPlaceholder("Nazwa aktywności");
        descriptionField.setPlaceholder("Opis");
        consumedCaloriesField.setPlaceholder("Kalorie");

        Button saveButton = new Button("Zapisz", event -> checkActivityInApi());
        saveButton.addClassName("black-button");

        Button cancelButton = new Button("Anuluj", event -> UI.getCurrent().navigate("manage-activities/" + userId));
        cancelButton.addClassName("black-button");

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonsLayout.setSpacing(true);

        formLayout.add(nameField, descriptionField, consumedCaloriesField, buttonsLayout);
        formLayout.setAlignItems(Alignment.CENTER);
        formLayout.setSpacing(true);

        return formLayout;
    }

    private void checkActivityInApi() {
        String name = nameField.getValue().trim();
        String exerciseData = exerciseApiClient.getExerciseByName(name);
        if (exerciseData != null && exerciseData.contains(name)) {
            Dialog dialog = new Dialog();
            dialog.add("Znaleziono aktywność o tej nazwie w API. Czy chcesz pobrać opis z API?");
            Button yesButton = new Button("Tak", event -> {
                descriptionField.setValue(exerciseApiClient.getDescriptionByName(name));
                dialog.close();
                saveActivity();
            });
            Button noButton = new Button("Nie", event -> {
                dialog.close();
                saveActivity();
            });
            dialog.add(new HorizontalLayout(yesButton, noButton));
            dialog.open();
        } else {
            saveActivity();
        }
    }

    private void saveActivity() {
        try {
            ActivityDTO newActivity = new ActivityDTO();
            newActivity.setName(nameField.getValue().trim());
            newActivity.setDescription(descriptionField.getValue().trim());
            newActivity.setConsumedCalories(Double.parseDouble(consumedCaloriesField.getValue().replace(",", ".")));
            newActivity.setUserId(Long.parseLong(userId));
            newActivity.setSource("manual");

            ActivityDTO createdActivity = activityApiClient.createActivity(newActivity);
            if (createdActivity != null) {
                Notification.show("Aktywność zapisana", 3000, Notification.Position.MIDDLE);
                UI.getCurrent().navigate("manage-activities/" + userId);
            } else {
                Notification.show("Błąd podczas zapisu aktywności", 3000, Notification.Position.MIDDLE);
            }
        } catch (NumberFormatException e) {
            Notification.show("Błąd: Nieprawidłowy format liczby", 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("Błąd podczas zapisu aktywności: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        RouteParameters parameters = event.getRouteParameters();
        userId = parameters.get("userId").orElse("");
        if (userId.isEmpty()) {
            Notification.show("Brak wymaganych parametrów.", 3000, Notification.Position.MIDDLE);
            UI.getCurrent().navigate("users");
        }
    }
}