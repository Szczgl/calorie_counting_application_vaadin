package com.calories.front.views.user;

import com.calories.front.api.UserApiClient;
import com.calories.front.dto.UserDTO;
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

@Route("users")
public class UsersView extends VerticalLayout {

    private UserApiClient userApiClient;

    private final Grid<UserDTO> grid = new Grid<>(UserDTO.class);

    @Autowired
    public UsersView(UserApiClient userApiClient) {
        this.userApiClient = userApiClient;

        H1 title = new H1("All Users");
        title.getStyle().set("font-size", "var(--lumo-font-size-m)").set("margin", "0");

        HorizontalLayout buttons = createButtonsLayout();

        grid.setColumns("username", "email", "dailyCalorieIntake", "dailyCalorieConsumption");
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.setAllRowsVisible(true);

        grid.addItemClickListener(event -> {
            Long clickedUserId = event.getItem().getId();
            UI.getCurrent().navigate(UserView.class, new RouteParameters(Map.of("userId", clickedUserId.toString())));
        });

        add(title, buttons, grid);
        setSizeFull();
        refreshGrid();
    }

    private HorizontalLayout createButtonsLayout() {
        HorizontalLayout functions = new HorizontalLayout();
        Button create = new Button("Create new");
        create.addClickListener(event -> UI.getCurrent().navigate(UserCreationView.class));

        Button backToMain = new Button("Main menu");
        backToMain.addClickListener(event -> UI.getCurrent().navigate(MainView.class));

        functions.add(create, backToMain);
        return functions;
    }

    private void refreshGrid() {
        List<UserDTO> users = userApiClient.getAllUsers();
        grid.setItems(users);
    }
}
