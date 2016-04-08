package mx.gob.cenapred.tickets.manager;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import mx.gob.cenapred.tickets.R;

public class MenuManager {
    public void updateMenuOptions(NavigationView mainNavigationView, AppPreferencesManager appPreferencesManager){
        String nombre = appPreferencesManager.getUserName();
        Integer idImgAvatar = R.mipmap.ic_avatar, idPerfil = appPreferencesManager.getUserRole();

        Boolean welcome = false, logout = false;
        switch (idPerfil) {
            case 1:
                welcome = true;
                logout = true;
                break;
            case 2:
                welcome = true;
                logout = true;
                break;
            default:
                nombre = "";
                break;
        }

        Menu menu = mainNavigationView.getMenu();
        menu.findItem(R.id.nav_welcome).setVisible(welcome);
        menu.findItem(R.id.nav_logout).setVisible(logout);

        View headerView = mainNavigationView.getHeaderView(0);
        ImageView nav_img_icon = (ImageView)headerView.findViewById(R.id.nav_img_avatar);
        nav_img_icon.setImageResource(idImgAvatar);
        TextView nav_txv_nombre = (TextView)headerView.findViewById(R.id.nav_txv_nombre);
        nav_txv_nombre.setText(nombre);
    }

    public void updateWelcomeTab(Activity activity, View view, Integer idRol){
        // Mapea el TabHost
        TabHost welcomeTabHost = (TabHost) view.findViewById(R.id.welcomeTabHost);

        // Mapea el contenido de las pestañas
        LinearLayout welcomeTabCreateTicket = (LinearLayout) view.findViewById(R.id.welcomeTabCreateTicket);
        LinearLayout welcomeTabSearchTicket = (LinearLayout) view.findViewById(R.id.welcomeTabSearchTicket);
        LinearLayout welcomeTabStadistics = (LinearLayout) view.findViewById(R.id.welcomeTabStadistics);

        // Hace invisible el contenido de las pestañas por default
        welcomeTabCreateTicket.setVisibility(View.INVISIBLE);
        welcomeTabSearchTicket.setVisibility(View.INVISIBLE);
        welcomeTabStadistics.setVisibility(View.INVISIBLE);

        // Habilita el TabHost
        welcomeTabHost.setup();

        // Crea las pestañas para "Generar" y "Buscar"
        addNewTab(welcomeTabHost, "createTicket", activity.getString(R.string.welcome_title_create_ticket), R.id.welcomeTabCreateTicket);
        addNewTab(welcomeTabHost, "searchTicket", activity.getString(R.string.welcome_title_search_ticket), R.id.welcomeTabSearchTicket);

        // Banderas para elementos de "Generar"
        Integer putTicketTechnicalSupport = View.GONE, putTicketDevelopers = View.GONE, putTicketNetworking = View.GONE;
        Integer getMyTicketPending = View.GONE, getSearchTicketNumber = View.GONE, getRequestPending = View.GONE;
        Integer getStadisticsGeneral = View.GONE;

        switch (idRol){
            case 1:
                putTicketTechnicalSupport = View.VISIBLE;
                putTicketDevelopers = View.VISIBLE;
                putTicketNetworking = View.VISIBLE;
                getMyTicketPending = View.VISIBLE;
                getSearchTicketNumber = View.VISIBLE;
                break;
            case 2:
                putTicketTechnicalSupport = View.VISIBLE;
                putTicketDevelopers = View.VISIBLE;
                putTicketNetworking = View.VISIBLE;
                getMyTicketPending = View.VISIBLE;
                getSearchTicketNumber = View.VISIBLE;
                getRequestPending = View.VISIBLE;
                getStadisticsGeneral = View.VISIBLE;

                // Crea la pestaña para "Estadisticas"
                addNewTab(welcomeTabHost, "stadistics", activity.getString(R.string.welcome_title_stadistics), R.id.welcomeTabStadistics);
                break;
            case 3:
                break;
        }

        view.findViewById(R.id.welcome_btn_ticket_technical_support).setVisibility(putTicketTechnicalSupport);
        view.findViewById(R.id.welcome_btn_ticket_developers).setVisibility(putTicketDevelopers);
        view.findViewById(R.id.welcome_btn_ticket_networking).setVisibility(putTicketNetworking);
        view.findViewById(R.id.welcome_btn_my_ticket_pending).setVisibility(getMyTicketPending);
        view.findViewById(R.id.welcome_btn_search_ticket_number).setVisibility(getSearchTicketNumber);
        view.findViewById(R.id.welcome_btn_request_pending).setVisibility(getRequestPending);
        view.findViewById(R.id.welcome_btn_stadistics_general).setVisibility(getStadisticsGeneral);
    }

    private void addNewTab(TabHost tabHost, String tag, String text, Integer idContent){
        TabHost.TabSpec spec = tabHost.newTabSpec(tag).setIndicator(text).setContent(idContent);
        tabHost.addTab(spec);
    }
}
