package ru.insros.vaadintest;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.Date;
import javax.servlet.annotation.WebServlet;
import org.joda.time.DateTime;

@Theme("mytheme")
public class MyVaadinUI extends UI {

    private static final long serialVersionUID = -502706719645480657L;

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class, widgetset = "ru.insros.vaadintest.AppWidgetSet")
    public static class Servlet extends VaadinServlet {

        private static final long serialVersionUID = 2884282116947804102L;
    }

    @Override
    protected void init(VaadinRequest request) {
        setSizeFull();
        
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);

        final TextField entry = new TextField("Enter this");
        Label display = new Label("See this");
        final InlineDateField date = new InlineDateField("Дата");
        date.setValue(DateTime.now().toDate());

        layout.addComponent(entry);
        layout.addComponent(display);
        layout.addComponent(new Label("День рождения:"));
        layout.addComponent(date);

        date.addValueChangeListener(new InlineDateField.ValueChangeListener() {
            private static final long serialVersionUID = 9008562589163306041L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                String value = entry.getValue();
                layout.addComponent(new Label("Какая то фигня с календарем - " + value));
                layout.addComponent(new Label("Событие: - " + event.toString()));
                layout.addComponent(new Label("Свойство: - " + event.getProperty().toString()));
            }
        });

        Button button = new Button("Click Me");
        button.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;

            @Override
            public void buttonClick(ClickEvent event) {
                Date data = date.getValue();
                String value = entry.getValue();
                layout.addComponent(new Label("Thank you for clicking. Choose date: " + data.toString()));
                layout.addComponent(new Label("Thank you for clicking. Your text: " + value));
            }
        });

        layout.addComponent(button);

    }

}
