package com.calories.front.views.activity;

import com.calories.front.api.ActivityApiClient;
import com.calories.front.dto.ActivityDTO;
import com.calories.front.views.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

//@Route("activity-edit/:activityId?")
//public class ActivityEditView extends VerticalLayout implements BeforeEnterObserver {

//    @Autowired
//    private ActivityApiClient activityApiClient;
//
//    private String activityId;
//
//    private final FormLayout activityForm = new FormLayout();
//    private final TextField name = new TextField("Name");
//    private final TextField description = new TextField("Description");
//    private final TextField consumedCalories = new TextField("Consumed Calories");
//    private final TextField userId = new TextField("User ID");
//
//    public ActivityEditView() {
//        activityForm.add(name, description, consumedCalories, userId);
//        activityForm.setSizeFull();
//        HorizontalLayout save = createSubmitLayout();
//        add(activityForm, save);
//    }
//
//    private HorizontalLayout createSubmitLayout() {
//        HorizontalLayout save = new HorizontalLayout();
//
//        Button submit = new Button("Submit changes");
//        submit.addClickListener(event -> {
//            String activityName = name.getValue();
//            String activityDescription = description.getValue();
//            double activityConsumedCalories = Double.parseDouble(consumedCalories.getValue());
//            Long activityUserId = Long.parseLong(userId.getValue());
//
//            updateActivityFromForm(activityName, activityDescription, activityConsumedCalories, activityUserId);
//
//            Notification n = new Notification("Activity updated successfully");
//            n.setPosition(Notification.Position.TOP_CENTER);
//            n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
//            n.setDuration(5000);
//            n.open();
//            UI.getCurrent().navigate(ActivitiesView.class);
//        });
//
//        Button backToMain = new Button("Main menu");
//        backToMain.addClickListener(event -> UI.getCurrent().navigate(MainView.class));
//
//        save.add(submit, backToMain);
//        return save;
//    }
//
//    private void updateActivityFromForm(String name, String description, double consumedCalories, Long userId) {
//        ActivityDTO dto = new ActivityDTO(Long.parseLong(activityId), name, description, consumedCalories, userId);
//        activityApiClient.updateActivity(dto, Long.parseLong(activityId));
//    }
//
//    @Override
//    protected void onAttach(AttachEvent attachEvent) {
//        super.onAttach(attachEvent);
//        ActivityDTO activity = activityApiClient.getActivityById(Long.parseLong(activityId));
//        name.setValue(activity.getName());
//        description.setValue(activity.getDescription());
//        consumedCalories.setValue(String.valueOf(activity.getConsumedCalories()));
//        userId.setValue(String.valueOf(activity.getUserId()));
//    }
//
//    @Override
//    public void beforeEnter(BeforeEnterEvent event) {
//        this.activityId = event.getRouteParameters().get("activityId").orElseThrow();
//    }
//}
