package mx.gob.cenapred.tickets.manager;

import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
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

    public void updateWelcomeOptions(View view, AppPreferencesManager appPreferencesManager){
        Integer postGroups = View.GONE, putTicket = View.GONE, getTicketPending = View.GONE, getTicketNumber = View.GONE, getStadisticsDate = View.GONE, getStadisticsPending = View.GONE, idPerfil = appPreferencesManager.getUserRole();

        switch (idPerfil) {
            case 1:
                putTicket = View.VISIBLE;
                getTicketNumber = View.VISIBLE;
                break;
            case 2:
                postGroups = View.VISIBLE;
                putTicket = View.VISIBLE;
                getTicketPending = View.VISIBLE;
                getTicketNumber = View.VISIBLE;
                getStadisticsDate = View.VISIBLE;
                getStadisticsPending = View.VISIBLE;
                break;
            default:
                break;
        }

        view.findViewById(R.id.welcome_btn_chose_groups).setVisibility(postGroups);
        view.findViewById(R.id.welcome_btn_ticket_technical_support).setVisibility(putTicket);
        view.findViewById(R.id.welcome_btn_ticket_developers).setVisibility(putTicket);
        view.findViewById(R.id.welcome_btn_ticket_networking).setVisibility(putTicket);
        view.findViewById(R.id.welcome_btn_ticket_pending).setVisibility(getTicketPending);
        view.findViewById(R.id.welcome_btn_ticket_number).setVisibility(getTicketNumber);
        view.findViewById(R.id.welcome_btn_stadistics_date).setVisibility(getStadisticsDate);
        view.findViewById(R.id.welcome_btn_stadistics_pending).setVisibility(getStadisticsPending);
    }
}
