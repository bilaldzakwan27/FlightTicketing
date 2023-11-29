package com.smk.view;

import com.smk.MainView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Search Booking")
@Route(value = "search-booking", layout = MainView.class)
public class SearchBooking extends VerticalLayout {
}