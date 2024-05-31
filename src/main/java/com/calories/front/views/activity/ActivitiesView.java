package com.calories.front.views.activity;

import com.calories.front.api.ActivityApiClient;
import com.calories.front.dto.ActivityDTO;
import com.calories.front.views.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Route("activities")
public class ActivitiesView extends VerticalLayout {

    private ActivityApiClient activityApiClient;

    private final Grid<ActivityDTO> grid = new Grid<>(ActivityDTO.class);

    @Autowired
    public ActivitiesView(ActivityApiClient activityApiClient) {
        this.activityApiClient = activityApiClient;

        H1 title = new H1("All Activities");
        title.getStyle().set("font-size", "var(--lumo-font-size-m)").set("margin", "0");

        HorizontalLayout buttons = createButtonsLayout();

        grid.setColumns("name", "description", "consumedCalories");
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.setAllRowsVisible(true);

        grid.addItemClickListener(event -> {
            Long clickedActivityId = event.getItem().getId();
            UI.getCurrent().navigate(ActivityView.class, new RouteParameters(Map.of("activityId", clickedActivityId.toString())));
        });

        add(title, buttons, grid);
        setSizeFull();
        refreshGrid();
    }

    private HorizontalLayout createButtonsLayout() {
        HorizontalLayout functions = new HorizontalLayout();
        Button create = new Button("Create new");
        create.addClickListener(event -> UI.getCurrent().navigate(ActivityCreationView.class));

        Button backToMain = new Button("Main menu");
        backToMain.addClickListener(event -> UI.getCurrent().navigate(MainView.class));

        functions.add(create, backToMain);
        return functions;
    }

    private void refreshGrid() {
        List<ActivityDTO> activities = activityApiClient.getAllActivities();
        grid.setItems(activities);
    }
}