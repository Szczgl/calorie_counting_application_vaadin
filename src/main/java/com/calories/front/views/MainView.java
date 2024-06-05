package com.calories.front.views;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")
@PageTitle("Kalkulator Kalorii")
@CssImport("./styles/styles.css")
public class MainView extends VerticalLayout {

    public MainView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Tło
        Div background = new Div();
        background.addClassName("background");

        // Nagłówek
        H1 title = new H1("KALKULATOR KALORII");
        title.addClassName("title");

        // Przyciski
        Button selectUserButton = new Button("Wybierz użytkownika");
        selectUserButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        selectUserButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("users")));
        selectUserButton.addClassName("black-button");

        Button addUserButton = new Button("Dodaj użytkownika");
        addUserButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addUserButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("create-user")));
        addUserButton.addClassName("black-button");

        // Kontener na przyciski
        HorizontalLayout buttonLayout = new HorizontalLayout(selectUserButton, addUserButton);
        buttonLayout.setSpacing(true);
        buttonLayout.setAlignItems(Alignment.CENTER);

        // Napis w prawym dolnym rogu
        Div footer = new Div();
        footer.setText("Created by Tomasz Sz.");
        footer.addClassName("footer");

        add(title, buttonLayout, footer);
        getElement().appendChild(background.getElement());
    }
}