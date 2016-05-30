package mx.gob.cenapred.tickets.manager;

import android.app.Activity;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.constant.MainConstant;
import mx.gob.cenapred.tickets.exception.NoInputDataException;

public class MenuManager {
    public void updateMenuOptions(NavigationView mainNavigationView, AppPreferencesManager appPreferencesManager) {
        String nombre = appPreferencesManager.getUserName(), rol = appPreferencesManager.getUserRoleName();
        Integer idImgAvatar = R.mipmap.ic_avatar, idRol = appPreferencesManager.getUserRoleId();

        Boolean welcome = false, logout = false;
        switch (idRol) {
            case 1:
                rol = "";
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
        ImageView nav_img_icon = (ImageView) headerView.findViewById(R.id.nav_img_avatar);
        nav_img_icon.setImageResource(idImgAvatar);
        TextView nav_txv_nombre = (TextView) headerView.findViewById(R.id.nav_txv_nombre);
        nav_txv_nombre.setText(nombre);
        TextView nav_txv_rol = (TextView) headerView.findViewById(R.id.nav_txv_rol);
        nav_txv_rol.setText(rol);
    }

    public void updateWelcomeTab(Activity activity, View view, Integer idRol, Integer indexTab) throws NoInputDataException {
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
        Integer putTicketTechnicalSupport = View.GONE, putTicketDevelopment = View.GONE, putTicketNetworking = View.GONE;
        Integer getMyTicketPending = View.GONE, getSearchTicketNumber = View.GONE, getRequestPending = View.GONE;
        Integer getStadisticsCustom = View.GONE;

        switch (idRol) {
            case 1:
                putTicketTechnicalSupport = View.VISIBLE;
                putTicketDevelopment = View.VISIBLE;
                putTicketNetworking = View.VISIBLE;
                getMyTicketPending = View.VISIBLE;
                getSearchTicketNumber = View.VISIBLE;
                break;
            case 2:
                putTicketTechnicalSupport = View.VISIBLE;
                putTicketDevelopment = View.VISIBLE;
                putTicketNetworking = View.VISIBLE;
                getMyTicketPending = View.VISIBLE;
                getSearchTicketNumber = View.VISIBLE;
                getRequestPending = View.VISIBLE;
                getStadisticsCustom = View.VISIBLE;

                // Crea la pestaña para "Estadisticas"
                addNewTab(welcomeTabHost, "stadistics", activity.getString(R.string.welcome_title_stadistics), R.id.welcomeTabStadistics);
                break;
            case 3:
                break;
            default:
                throw new NoInputDataException(MainConstant.MESSAGE_DESCRIPTION_NO_ROL);
        }

        for (Integer i = 0; i < welcomeTabHost.getTabWidget().getChildCount(); i++) {
            View tabView = welcomeTabHost.getTabWidget().getChildAt(i);
            tabView.setBackgroundResource(R.drawable.general_tabhost);

            TextView title = (TextView) welcomeTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            title.setAllCaps(false);
            title.setEllipsize(TextUtils.TruncateAt.END);
            title.setHorizontallyScrolling(false);
            title.setSingleLine();
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, activity.getResources().getDimension(R.dimen.general_main_font_size));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                title.setTextColor(activity.getResources().getColorStateList(R.color.general_tabhost_text, activity.getTheme()));
            } else {
                title.setTextColor(activity.getResources().getColorStateList(R.color.general_tabhost_text));
            }
        }

        // Selecciona el Tab adecuado
        welcomeTabHost.setCurrentTab(indexTab);

        view.findViewById(R.id.welcome_btn_ticket_technical_support).setVisibility(putTicketTechnicalSupport);
        view.findViewById(R.id.welcome_btn_ticket_development).setVisibility(putTicketDevelopment);
        view.findViewById(R.id.welcome_btn_ticket_networking).setVisibility(putTicketNetworking);
        view.findViewById(R.id.welcome_btn_my_ticket_pending).setVisibility(getMyTicketPending);
        view.findViewById(R.id.welcome_btn_search_ticket_number).setVisibility(getSearchTicketNumber);
        view.findViewById(R.id.welcome_btn_request_pending).setVisibility(getRequestPending);
        view.findViewById(R.id.welcome_btn_stadistics_custom).setVisibility(getStadisticsCustom);
    }

    private void addNewTab(TabHost tabHost, String tag, String text, Integer idContent) {
        TabHost.TabSpec spec = tabHost.newTabSpec(tag).setIndicator(text).setContent(idContent);
        tabHost.addTab(spec);
    }
}
