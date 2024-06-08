package com.calories.front.views.user;

import com.calories.front.api.UserApiClient;
import com.calories.front.dto.ReportDTO;
import com.calories.front.dto.UserDTO;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("users")
@PageTitle("Wszyscy użytkownicy")
@CssImport("./styles/styles.css")
public class UsersView extends VerticalLayout {

    private final UserApiClient userApiClient;

    private final Grid<UserDTO> grid = new Grid<>(UserDTO.class);
    private final TextField filter = new TextField();

    @Autowired
    public UsersView(UserApiClient userApiClient) {
        this.userApiClient = userApiClient;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        Div background = new Div();
        background.addClassName("background");
        background.setSizeFull();

        H1 title = new H1("Wszyscy użytkownicy");
        title.addClassName("title");

        filter.setPlaceholder("Szukaj po nazwie użytkownika...");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> updateList());
        filter.addClassName("custom-search-field");

        grid.removeAllColumns();
        grid.addColumn(UserDTO::getUsername).setHeader("Nazwa użytkownika").setAutoWidth(true);
        grid.addColumn(UserDTO::getEmail).setHeader("Email").setAutoWidth(true);
        grid.addColumn(UserDTO::getDailyCalorieIntake).setHeader("Dzienne zapotrzebowanie kaloryczne").setAutoWidth(true);

        grid.addColumn(new ComponentRenderer<>(user -> {
            HorizontalLayout actions = new HorizontalLayout();

            Button selectButton = new Button("Wybierz", clickEvent -> {
                getUI().ifPresent(ui -> ui.navigate(UserDetailsView.class, new RouteParameters("userId", user.getId().toString())));
            });
            selectButton.addClassName("black-button");

            Button editButton = new Button("Edytuj", clickEvent -> {
                getUI().ifPresent(ui -> ui.navigate(UserEditView.class, new RouteParameters("userId", user.getId().toString())));
            });
            editButton.addClassName("black-button");

            Button deleteButton = new Button("Usuń", clickEvent -> {
                userApiClient.deleteUser(user.getId());
                refreshGrid();
            });
            deleteButton.addClassName("black-button");

            Button reportButton = new Button("Raport", clickEvent -> {
                generateReport(user.getId());
            });
            reportButton.addClassName("black-button");

            actions.add(selectButton, editButton, deleteButton, reportButton);
            return actions;
        })).setHeader("Akcje").setAutoWidth(true);

        HorizontalLayout buttonsLayout = createButtonsLayout();
        add(title, filter, grid, buttonsLayout);
        getElement().appendChild(background.getElement());

        refreshGrid();
    }

    private HorizontalLayout createButtonsLayout() {
        HorizontalLayout buttonsLayout = new HorizontalLayout();

        Button createButton = new Button("Utwórz nowego", event -> getUI().ifPresent(ui -> ui.navigate("create-user")));
        createButton.addClassName("black-button");

        Button undoButton = new Button("Cofnij", event -> UI.getCurrent().getPage().executeJs("history.back()"));
        undoButton.addClassName("black-button");

        Button backButton = new Button("Powrót do głównego menu", event -> getUI().ifPresent(ui -> ui.navigate("")));
        backButton.addClassName("black-button");

        buttonsLayout.add(createButton,undoButton, backButton);
        return buttonsLayout;
    }

    private void refreshGrid() {
        List<UserDTO> users = userApiClient.getAllUsers();
        grid.setItems(users);
    }

    private void updateList() {
        List<UserDTO> users = userApiClient.getAllUsers();
        grid.setItems(users.stream()
                .filter(user -> user.getUsername().toLowerCase().contains(filter.getValue().toLowerCase()))
                .toList());
    }

    private void generateReport(Long userId) {
        ReportDTO report = userApiClient.generateReport(userId);
        showReportDialog(report);
    }

    private void showReportDialog(ReportDTO report) {
        Dialog dialog = new Dialog();
        dialog.setWidth("800px");
        dialog.setHeight("600px");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(
                new H1("Raport dla użytkownika " + report.getUsername()),
                new Div(new Text("Data raportu: " + report.getReportDate())),
                new Div(new Text("Kalorie spożyte: " + report.getDailyCalorieIntake())),
                new Div(new Text("Kalorie spalone: " + report.getDailyCalorieConsumption())),
                new Div(new Text("Bilans kaloryczny: " + report.getCalorieBalance())),
                new Div(new Text("Przepisy: " + String.join(", ", report.getRecipesName())))
        );

        Button closeButton = new Button("Zamknij", event -> dialog.close());
        closeButton.addClassName("black-button");
        dialogLayout.add(closeButton);

        dialog.add(dialogLayout);
        dialog.open();
    }
}