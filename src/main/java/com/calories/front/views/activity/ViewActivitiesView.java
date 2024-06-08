package com.calories.front.views.activity;

import com.calories.front.api.ActivityApiClient;
import com.calories.front.api.UserApiClient;
import com.calories.front.dto.ActivityDTO;
import com.calories.front.dto.UserDTO;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Route("view-activities/:userId")
@PageTitle("Wyświetl aktywności")
@CssImport("./styles/styles.css")
public class ViewActivitiesView extends VerticalLayout implements BeforeEnterObserver {

    private final ActivityApiClient activityApiClient;
    private final UserApiClient userApiClient;

    private final Grid<ActivityDTO> grid = new Grid<>(ActivityDTO.class);
    private final TextField filter = new TextField();
    private final Checkbox userOnlyCheckbox = new Checkbox("Wyświetl tylko aktywności wybranego użytkownika");

    private String userId;
    private UserDTO selectedUser;

    private final TextField username = new TextField("Nazwa użytkownika");
    private final EmailField email = new EmailField("Email");
    private final TextField dailyCalorieIntake = new TextField("Dzienne zapotrzebowanie kaloryczne");
    private final TextField dailyCalorieConsumption = new TextField("Dzienne spalanie kalorii");

    @Autowired
    public ViewActivitiesView(ActivityApiClient activityApiClient, UserApiClient userApiClient) {
        this.activityApiClient = activityApiClient;
        this.userApiClient = userApiClient;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        Div background = new Div();
        background.addClassName("background");
        background.setSizeFull();

        Div userDetailContainer = new Div();
        userDetailContainer.getStyle().set("background-color", "white");
        userDetailContainer.getStyle().set("padding", "20px");
        userDetailContainer.getStyle().set("border-radius", "10px");
        userDetailContainer.getStyle().set("margin", "50px auto 0 auto");

        HorizontalLayout userDetailLayout = new HorizontalLayout(username, email, dailyCalorieIntake, dailyCalorieConsumption);
        userDetailLayout.setSpacing(true);

        Button changeUserButton = new Button("Zmień", event -> getUI().ifPresent(ui -> ui.navigate("users")));
        changeUserButton.addClassName("black-button");

        VerticalLayout userInfoLayout = new VerticalLayout(userDetailLayout, changeUserButton);
        userInfoLayout.setSpacing(true);
        userDetailContainer.add(userInfoLayout);

        add(userDetailContainer);
        add(createActivitiesView());

        HorizontalLayout footerButtons = createFooterButtons();
        footerButtons.getStyle().set("position", "absolute");
        footerButtons.getStyle().set("bottom", "50px");
        footerButtons.setWidth("100%");
        footerButtons.setJustifyContentMode(JustifyContentMode.CENTER);
        footerButtons.setSpacing(true);

        add(footerButtons);
        getElement().appendChild(background.getElement());
    }

    private VerticalLayout createActivitiesView() {
        VerticalLayout activitiesLayout = new VerticalLayout();

        H1 title = new H1("Lista aktywności");
        title.addClassName("title");

        filter.setPlaceholder("Szukaj po nazwie aktywności...");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> updateList());
        filter.addClassName("custom-search-field");

        userOnlyCheckbox.addValueChangeListener(e -> updateList());
        userOnlyCheckbox.getStyle().set("background-color", "white");
        userOnlyCheckbox.getStyle().set("padding", "5px");
        userOnlyCheckbox.getStyle().set("border-radius", "5px");

        HorizontalLayout filterLayout = new HorizontalLayout(filter, userOnlyCheckbox);
        filterLayout.setWidthFull();
        filterLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        filterLayout.setAlignItems(Alignment.CENTER);

        grid.removeAllColumns();
        grid.addColumn(ActivityDTO::getName).setHeader("Nazwa");
        grid.addComponentColumn(activity -> {
            Span description = new Span(activity.getDescription());
            description.getElement().setProperty("title", activity.getDescription());
            return description;
        }).setHeader("Opis");
        grid.addColumn(ActivityDTO::getConsumedCalories).setHeader("Kalorie");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        activitiesLayout.add(title, filterLayout, grid);
        refreshGrid();

        return activitiesLayout;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.userId = event.getRouteParameters().get("userId").orElseThrow();
        this.selectedUser = userApiClient.getUserById(Long.parseLong(userId));
        updateUserDetails();
        refreshGrid();
    }

    private void updateUserDetails() {
        username.setValue(selectedUser.getUsername());
        email.setValue(selectedUser.getEmail());
        dailyCalorieIntake.setValue(String.valueOf(selectedUser.getDailyCalorieIntake()));
        dailyCalorieConsumption.setValue(String.valueOf(selectedUser.getDailyCalorieConsumption()));
    }

    private void refreshGrid() {
        List<ActivityDTO> activities = activityApiClient.getAllActivities();
        grid.setItems(activities);
    }

    private void updateList() {
        List<ActivityDTO> activities = activityApiClient.getAllActivities();
        if (userOnlyCheckbox.getValue()) {
            activities = activities.stream()
                    .filter(activity -> activity.getUserId().equals(Long.parseLong(userId)))
                    .collect(Collectors.toList());
        }
        grid.setItems(activities.stream()
                .filter(activity -> activity.getName().toLowerCase().contains(filter.getValue().toLowerCase()))
                .collect(Collectors.toList()));
    }

    private HorizontalLayout createFooterButtons() {
        Button backButton = new Button("Powrót do głównego menu", event -> getUI().ifPresent(ui -> ui.navigate("")));
        backButton.addClassName("black-button");

        Button undoButton = new Button("Cofnij", event -> UI.getCurrent().getPage().executeJs("history.back()"));
        undoButton.addClassName("black-button");

        HorizontalLayout footerButtons = new HorizontalLayout(undoButton, backButton);
        footerButtons.setSpacing(true);
        return footerButtons;
    }
}