package com.calories.front.views.activity;

import com.calories.front.api.ActivityApiClient;
import com.calories.front.dto.ActivityDTO;
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

@Route("edit-activity/:activityId")
@PageTitle("Edycja aktywności")
@CssImport("./styles/styles.css")
public class EditActivityView extends VerticalLayout implements BeforeEnterObserver {

    private final ActivityApiClient activityApiClient;

    private ActivityDTO activity;

    private final TextField name = new TextField("Nazwa");
    private final TextField description = new TextField("Opis");
    private final TextField consumedCalories = new TextField("Kalorie");

    private final DecimalFormat decimalFormat;

    @Autowired
    public EditActivityView(ActivityApiClient activityApiClient) {
        this.activityApiClient = activityApiClient;

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

        Button saveButton = new Button("Zapisz", event -> saveActivity());
        saveButton.addClassName("black-button");

        Button cancelButton = new Button("Anuluj", event -> getUI().ifPresent(ui -> ui.navigate("manage-activities/" + activity.getUserId())));
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

        H1 title = new H1("Edycja aktywności");
        title.addClassName("title");

        name.setReadOnly(true);
        description.setReadOnly(true);
        consumedCalories.setPlaceholder("Kalorie");
        consumedCalories.setValueChangeMode(ValueChangeMode.EAGER);

        formLayout.add(title, name, description, consumedCalories);
        formLayout.setAlignItems(Alignment.CENTER);
        formLayout.setSpacing(true);

        return formLayout;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String activityId = event.getRouteParameters().get("activityId").orElseThrow();
        this.activity = activityApiClient.getActivityById(Long.parseLong(activityId));
        updateForm();
    }

    private void updateForm() {
        name.setValue(activity.getName());
        description.setValue(activity.getDescription());
        consumedCalories.setValue(decimalFormat.format(activity.getConsumedCalories()));
    }

    private void saveActivity() {
        if (activity != null) {
            try {
                activity.setConsumedCalories(decimalFormat.parse(consumedCalories.getValue()).doubleValue());

                ActivityDTO updatedActivity = activityApiClient.updateActivity(activity, activity.getId());
                if (updatedActivity != null) {
                    Notification.show("Aktywność zaktualizowana", 3000, Notification.Position.MIDDLE);
                    getUI().ifPresent(ui -> ui.navigate("manage-activities/" + activity.getUserId()));
                } else {
                    Notification.show("Błąd podczas aktualizacji aktywności", 3000, Notification.Position.MIDDLE);
                }
            } catch (Exception e) {
                Notification.show("Błąd podczas zapisu aktywności", 3000, Notification.Position.MIDDLE);
            }
        }
    }
}