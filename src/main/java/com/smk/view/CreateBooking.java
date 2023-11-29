package com.smk.view;

import com.smk.MainView;
import com.smk.dao.BookingDao;
import com.smk.dao.LocationDao;
import com.smk.dao.ScheduleDao;
import com.smk.model.Booking;
import com.smk.model.Location;
import com.smk.model.Schedule;
import com.smk.model.dto.ScheduleDTO;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.awt.print.Book;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

@PageTitle("Create Booking")
@Route(value = "create booking", layout = MainView.class)
public class CreateBooking extends VerticalLayout {
    private LocationDao locationDao;
    private final ScheduleDao scheduleDao;
    private static final BookingDao bookingDao = new BookingDao();

    public CreateBooking(){
        locationDao = new LocationDao();
        scheduleDao = new ScheduleDao();
        createForm();
    }

    private void createForm(){
        setAlignItems(Alignment.STRETCH);
        ComboBox<Location> fromComboBox = new ComboBox("Dari");
        fromComboBox.setItems(locationDao.getAll());
        fromComboBox.setItemLabelGenerator(Location::getName);

        ComboBox<Location> toComboBox = new ComboBox("Ke");
        toComboBox.setItems(locationDao.getAll());
        toComboBox.setItemLabelGenerator(Location::getName);
        DatePicker departureDatePicker = new DatePicker("Tanggal Keberangkatan");
        DatePicker arrivalDatePicker = new DatePicker("Tanggal Kepulangan");
        Button searchButton = new Button("Search");
        searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(fromComboBox, toComboBox, departureDatePicker, arrivalDatePicker, searchButton);
        Grid<ScheduleDTO> grid = new Grid<>(ScheduleDTO.class, false);
        grid.addColumn(ScheduleDTO::getId).setHeader("Id");
        grid.addColumn(ScheduleDTO::getFlightNumber).setHeader("Nomor Pesawat");
        grid.addColumn(ScheduleDTO::getDepartureLocation).setHeader("Keberangkatan");
        grid.addColumn(ScheduleDTO::getArrivalLocation).setHeader("Kedatangan");
        grid.addColumn(ScheduleDTO::getDepartureDate).setHeader("Waktu Keberangkatan");
        grid.setItemDetailsRenderer(createBookingRenderer());

        searchButton.addClickListener(clickEvent -> {
            Collection<ScheduleDTO> scheduleDTOCollection = scheduleDao.searchSchedule(
                    fromComboBox.getValue().getId(),
                    toComboBox.getValue().getId(),
                    Date.from(departureDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())
            );
            grid.setItems(scheduleDTOCollection);
        });



        add(fromComboBox, toComboBox, departureDatePicker, arrivalDatePicker, searchButton, grid);
    }
    private static class CreateBookingFormLayout extends FormLayout{
        private final TextField idTextFeld = new TextField("");
        private final TextField fromTextField = new TextField("Dari");
        private final TextField toTextField = new TextField("Ke");
        private final DatePicker departureDatePicker = new DatePicker("Tanggal Keberangkatan");
        private final TextField nameTextField = new TextField("Nama");
        private final TextField priceTextField = new TextField("Harga");
        private final Button saveBooking = new Button("Save");

        public CreateBookingFormLayout() {
            idTextFeld.setVisible(false);
            add(idTextFeld);
            fromTextField.setReadOnly(true);
            toTextField.setReadOnly(true);
            departureDatePicker.setReadOnly(true);
            Stream.of(fromTextField, toTextField, departureDatePicker, nameTextField, priceTextField, saveBooking).forEach(this::add);
            saveBooking.addClickListener(clickEvent -> {
                Booking booking = new Booking();
                booking.setScheduleId(Long.parseLong(idTextFeld.getValue()));
                booking.setName(nameTextField.getValue());
                booking.setPrice(Double.parseDouble(priceTextField.getValue()));
                Optional<Integer> id = bookingDao.save(booking);
                id.ifPresent(integer -> {
                    ConfirmDialog confirmDialog = new ConfirmDialog();
                    confirmDialog.setText("booking created with id =" + integer);
                    confirmDialog.setCancelable(false);
                    confirmDialog.setRejectable(false);
                    confirmDialog.setConfirmText("OK");
                    confirmDialog.addConfirmListener(event -> {
                        confirmDialog.close();
                    });
                    add(confirmDialog);
                    confirmDialog.open();
                });

            });

        }
        public void setScheduleDTO(ScheduleDTO scheduleDTO){
            idTextFeld.setValue(String.valueOf(scheduleDTO.getId()));
            fromTextField.setValue(scheduleDTO.getDepartureLocation());
            toTextField.setValue(scheduleDTO.getArrivalLocation());
            departureDatePicker.setValue(LocalDate.parse(scheduleDTO.getDepartureDate().toString()));
        }

    }
    private static ComponentRenderer<CreateBookingFormLayout, ScheduleDTO> createBookingRenderer() {
        return new ComponentRenderer<>(CreateBookingFormLayout::new,CreateBookingFormLayout::setScheduleDTO);
    }
}