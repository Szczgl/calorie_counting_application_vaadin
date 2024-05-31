package com.calories.front.views.activity;

import com.calories.front.api.ActivityApiClient;
import com.calories.front.dto.ActivityDTO;
import com.calories.front.views.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("activity-create")
public class ActivityCreationView extends VerticalLayout {

    @Autowired
    private ActivityApiClient activityApiClient;

    private final FormLayout activityForm = new FormLayout();
    private final TextField name = new TextField("Name");
    private final TextField description = new TextField("Description");
    private final TextField consumedCalories = new TextField("Consumed Calories");
    private final TextField userId = new TextField("User ID");

    public ActivityCreationView() {
        name.setRequired(true);
        description.setRequired(true);
        consumedCalories.setRequired(true);
        userId.setRequired(true);

        activityForm.add(name, description, consumedCalories, userId);
        activityForm.setSizeFull();

        HorizontalLayout buttons = createButtonsLayout();

        add(activityForm, buttons);
    }

    private HorizontalLayout createButtonsLayout() {
        HorizontalLayout functions = new HorizontalLayout();

        Button create = new Button("Create");
        create.addClickListener(event -> {
            String activityName = name.getValue();
            String activityDescription = description.getValue();
            double caloriesBurned = Double.parseDouble(consumedCalories.getValue());
            Long userIdValue = Long.parseLong(userId.getValue());

            if (activityName.isEmpty() || activityDescription.isEmpty() || caloriesBurned <= 0 || userIdValue == null) {
                Notification fail = new Notification();
                fail.setText("Please fill in all required fields");
                fail.setDuration(3000);
                fail.setPosition(Notification.Position.TOP_CENTER);
                fail.addThemeVariants(NotificationVariant.LUMO_WARNING);
                fail.open();
            } else {
                createActivity(activityName, activityDescription, caloriesBurned, userIdValue);
                UI.getCurrent().navigate(ActivitiesView.class);
            }
        });

        Button backToMain = new Button("Main menu");
        backToMain.addClickListener(event -> UI.getCurrent().navigate(MainView.class));

        functions.add(create, backToMain);
        return functions;
    }

    private void createActivity(String name, String description, double caloriesBurned, Long userId) {
        ActivityDTO dto = new ActivityDTO(null, name, description, caloriesBurned, userId);
        activityApiClient.createActivity(dto);
    }
}
