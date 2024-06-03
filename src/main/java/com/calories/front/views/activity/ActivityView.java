package com.calories.front.views.activity;

import com.calories.front.api.ActivityApiClient;
import com.calories.front.dto.ActivityDTO;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.*;
import com.calories.front.views.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

//@Route("activity-details/:activityId")
//public class ActivityView extends VerticalLayout implements BeforeEnterObserver {

//    @Autowired
//    private ActivityApiClient activityApiClient;
//
//    private String activityId;
//
//    private final FormLayout activityForm = new FormLayout();
//    private final TextField name = new TextField("Name");
//    private final TextArea description = new TextArea("Description");
//    private final TextField consumedCalories = new TextField("Calories Burned");
//
//    public ActivityView() {
//        HorizontalLayout buttons = createButtonsLayout();
//        activityForm.add(name, description, consumedCalories);
//        activityForm.setSizeFull();
//        add(buttons, activityForm);
//    }
//
//    private HorizontalLayout createButtonsLayout() {
//        HorizontalLayout functions = new HorizontalLayout();
//
//        Button edit = new Button("Edit");
//        edit.addClickListener(event -> UI.getCurrent().navigate(ActivityEditView.class, new RouteParameters("activityId", activityId)));
//
//        Button delete = new Button("Delete");
//        delete.addClickListener(event -> {
//            deleteActivityById(Long.valueOf(activityId));
//            UI.getCurrent().navigate(ActivitiesView.class);
//        });
//
//        Button backToMain = new Button("Main menu");
//        backToMain.addClickListener(event -> UI.getCurrent().navigate(MainView.class));
//
//        functions.add(edit, delete, backToMain);
//        return functions;
//    }
//
//    private void deleteActivityById(Long id) {
//        activityApiClient.deleteActivity(id);
//    }
//
//    @Override
//    protected void onAttach(AttachEvent attachEvent) {
//        super.onAttach(attachEvent);
//        ActivityDTO activity = activityApiClient.getActivityById(Long.parseLong(activityId));
//        name.setValue(activity.getName());
//        description.setValue(activity.getDescription());
//        consumedCalories.setValue(String.valueOf(activity.getConsumedCalories()));
//    }
//
//    @Override
//    public void beforeEnter(BeforeEnterEvent event) {
//        this.activityId = event.getRouteParameters().get("activityId").orElseThrow();
//    }
//}