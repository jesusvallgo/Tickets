package mx.gob.cenapred.tickets.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mx.gob.cenapred.tickets.R;
import mx.gob.cenapred.tickets.entity.BundleEntity;
import mx.gob.cenapred.tickets.entity.MensajeEntity;
import mx.gob.cenapred.tickets.fragment.AboutFragment;
import mx.gob.cenapred.tickets.fragment.LoadingFragment;
import mx.gob.cenapred.tickets.fragment.LoginFragment;
import mx.gob.cenapred.tickets.fragment.LogoutFragment;
import mx.gob.cenapred.tickets.fragment.MyTicketPendingFragment;
import mx.gob.cenapred.tickets.fragment.RecoverPasswordFragment;
import mx.gob.cenapred.tickets.fragment.ReportDelegateFragment;
import mx.gob.cenapred.tickets.fragment.ReportDetailFragment;
import mx.gob.cenapred.tickets.fragment.ReportViewHistoryFragment;
import mx.gob.cenapred.tickets.fragment.ReportNewOtherFragment;
import mx.gob.cenapred.tickets.fragment.ReportAddHistoryFragment;
import mx.gob.cenapred.tickets.fragment.RequestPendingFragment;
import mx.gob.cenapred.tickets.fragment.ReportNewShortcutFragment;
import mx.gob.cenapred.tickets.fragment.SearchTicketNumberFragment;
import mx.gob.cenapred.tickets.fragment.RegisterFragment;
import mx.gob.cenapred.tickets.fragment.WelcomeFragment;
import mx.gob.cenapred.tickets.gcm.RegistrationIntentService;
import mx.gob.cenapred.tickets.manager.AppPreferencesManager;
import mx.gob.cenapred.tickets.manager.MessagesManager;
import mx.gob.cenapred.tickets.manager.KeyboardManager;
import mx.gob.cenapred.tickets.manager.MenuManager;
import mx.gob.cenapred.tickets.preference.AppPreference;
import mx.gob.cenapred.tickets.util.GooglePlayServices;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // **************************** Constantes ****************************

    // Define el TAG para LOGS de la Activity principal
    private static final String TAG = "MainActivity";

    // Obtiene el contexto actual
    private final Context mainContext = this;
    private final Activity mainActivity = this;

    // Instancia a la clase auxiliar para ocultar el teclado
    private final KeyboardManager keyboardManager = new KeyboardManager();

    // **************************** Variables ****************************

    // Manejador del Drawer de la Activity
    private DrawerLayout mainDrawerLayout;

    // Manejador del menu de usuario
    private NavigationView mainNavigationView;

    // Manejador de los Fragments
    private Fragment mainCurrentFragment;

    // Indicador para definir si se desea limpiar la pila de Fragments
    private boolean clearBackStack = false;

    // Indicador para definir si se desea agregar a la pila de Fragments
    private boolean addToBackStack = false;

    // Cadenas para la barra de la aplicacion
    private String fragmentName, appBarName;

    // Indicador para definir si se desea actualizar el menu de usuario
    private boolean updateMenu = false;

    // Indicador para disparar el intent
    private boolean startIntent;

    // Componente que permite conocer los eventos que estan sucediendo en el Sistema Operativo
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    // Variables para almacenar los posibles errores
    private List<MensajeEntity> messagesList;
    private List<String> messageTypeList = new ArrayList<String>();
    private List<String> messageTitleList = new ArrayList<String>();
    private List<String> messageDescriptionList = new ArrayList<String>();

    // Contenedor de datos del Bundle
    private BundleEntity bundleEntity = new BundleEntity();

    // Manejador de los errores
    private MessagesManager messagesManager = new MessagesManager();

    // Manejador de las preferencias de la aplicacion
    private AppPreferencesManager appPreferencesManager;

    // Manejador del menu de usuario
    private MenuManager menuManager = new MenuManager();

    // Intent para capturar el BroadcastReceiver
    private Intent intent;

    // Variables para manipular los elementos del AppBar
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;

    // Variable para contar el numero de Eventos
    private Integer totalEvents;

    // Variable para capturar o especificar el ID de Reporte
    private Integer idReport;

    // Acciones que se realizan al crear la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Muestra el layout principal
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Mapea el manejador del Drawer a su elemento correspondiente
        mainDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Muestra la barra superior
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Muestra el drawer con las opciones del menu
        toggle = new ActionBarDrawerToggle(mainActivity, mainDrawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close) {
            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                keyboardManager.hideSoftKeyboard(mainActivity);
            }
        };
        //mainDrawerLayout.setDrawerListener(toggle);

        // Mapea el manejador del menu a su elemento correspondiente
        mainNavigationView = (NavigationView) findViewById(R.id.nav_view);

        // Agrega un listener a los elementos del menu
        mainNavigationView.setNavigationItemSelectedListener(this);

        // Inicializa el manejador de preferencias de la aplicacion
        appPreferencesManager = new AppPreferencesManager(mainContext);

        intent = new Intent(mainActivity, RegistrationIntentService.class);

        // Genera el Fragment correspondiente
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    // Busca si el Token pudo registrarse correctamente
                    if (!appPreferencesManager.getTokenRegistered()) {
                        // Genera la excepcion correspondiente
                        throw new Exception("No se pudo registrar el token del dispositivo");
                    }

                    // Busca si se tienen almacenadas las credenciales de usuario
                    if (appPreferencesManager.getLoginStatus() == true) {
                        // Abre el fragment predeterminado de inicio
                        manageFragment(R.id.nav_welcome, null);
                    } else {
                        // Abre el Fragment de logeo
                        manageFragment(R.id.fragment_login, null);
                    }

                    startIntent = false;
                } catch (Exception e) {
                    // Limpia las listas de error
                    messageTypeList.clear();
                    messageTitleList.clear();
                    messageDescriptionList.clear();

                    // Agrega el error a mostrar
                    messageTypeList.add(AppPreference.MESSAGE_ERROR);
                    messageTitleList.add(0, "Error crítico");
                    messageDescriptionList.add(0, e.getMessage());

                    // Crea la lista de errores
                    messagesList = messagesManager.createMensajesList(messageTitleList,messageTitleList, messageDescriptionList);

                    // Despliega los errores encontrados
                    messagesManager.displayMessage(mainActivity, mainContext, messagesList, AppPreference.ALERT_ACTION_FINISH);
                }
            }
        };

        // Agrega el listener para determinar el icono y acciones a mostrar y realizar en el Toolbar
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                manageToolbar();
            }
        });

        if (savedInstanceState != null) {
            // Mantiene el Fragment generado previamente
        } else {
            toggle.syncState();
            manageFragment(R.id.fragment_loading, null);

            idReport = getIntent().getIntExtra("idNotification", 0);
            if (idReport > 0) {
                bundleEntity.setIdReportBundle(idReport);
                bundleEntity.setAddToBackStack(false);
                manageFragment(R.id.fragment_report_detail, bundleEntity);
            } else {
                startIntent = true;
            }

            try {
                // Valida si el dispositivo soporta GooglePlayServices
                GooglePlayServices googlePlayServices = new GooglePlayServices();
                if (!googlePlayServices.validaGooglePlayServices(TAG, mainContext)) {
                    // Genera la excepcion correspondiente
                    throw new Exception("El dispositivo no soporta Google Play Services.");
                }
            } catch (Exception ex) {
                // Limpia las listas de mensajes
                messageTypeList.clear();
                messageTitleList.clear();
                messageDescriptionList.clear();

                // Agrega el error a mostrar
                messageTypeList.add(0, AppPreference.MESSAGE_ERROR);
                messageTitleList.add(0, "Error crítico");
                messageDescriptionList.add(0, ex.getMessage());

                // Crea la lista de errores
                messagesList = messagesManager.createMensajesList(messageTypeList,messageTitleList, messageDescriptionList);

                // Despliega los errores encontrados
                messagesManager.displayMessage(mainActivity, mainContext, messagesList, AppPreference.ALERT_ACTION_FINISH);
            }
        }

        // Llamada al metodo para agregar la opcion de ocultar teclado
        keyboardManager.configureUI(findViewById(R.id.drawer_layout), mainActivity);

        // Actualiza el menu cuando se crea la Activity (en cada rotacion)
        menuManager.updateMenuOptions(mainNavigationView, appPreferencesManager);

        // Actualiza el Toolbar
        manageToolbar();
    }

    // Metodo onResume de acuerdo al ciclo de vida de un Activity
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(AppPreference.REGISTRATION_COMPLETE));

        if (startIntent) {
            startService(intent);
        }
    }

    // Metodo onPause de acuerdo al ciclo de vida de un Activity
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    // Sobreescribe el metodo que reacciona ante la tecla "atras"
    @Override
    public void onBackPressed() {
        if (mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            // Si el menu esta desplegado, cierra el menu
            mainDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // Si no esa desplegado, realiza su accion por default
            super.onBackPressed();
        }
    }

    // Metodo para cambiar los Fragment de la Activity
    public void manageFragment(int id, BundleEntity bundleEntity) {
        fragmentName = getSupportActionBar().getTitle().toString();
        clearBackStack = false;
        addToBackStack = false;
        updateMenu = false;
        mainCurrentFragment = null;

        // Crea el contenedor de argumentos (datos por enviar a un Fragment)
        Bundle bundle = new Bundle();

        // Determina la opcion seleccionada
        switch (id) {
            case R.id.fragment_loading:
                mainCurrentFragment = new LoadingFragment();
                break;
            case R.id.fragment_login:
                fragmentName = "Login";
                mainCurrentFragment = new LoginFragment();
                updateMenu = true;
                break;
            case R.id.fragment_report_other:
                bundle.putInt("idAttentionArea", bundleEntity.getIdAreaAtencion());
                mainCurrentFragment = new ReportNewOtherFragment();
                addToBackStack = true;
                break;
            case R.id.fragment_report_detail:
                fragmentName = "Detalle";
                bundle.putInt("idReport", bundleEntity.getIdReportBundle());
                bundle.putBoolean("addToBackStack", bundleEntity.getAddToBackStack());
                mainCurrentFragment = new ReportDetailFragment();
                addToBackStack = bundleEntity.getAddToBackStack();
                updateMenu = true;
                break;
            case R.id.fragment_report_history:
                fragmentName = "Historial";
                bundle.putSerializable("listHistoryAction", (Serializable) bundleEntity.getListHistoryAction());
                mainCurrentFragment = new ReportViewHistoryFragment();
                addToBackStack = true;
                break;
            case R.id.fragment_report_delegate:
                fragmentName = "Turnar";
                bundle.putInt("idReport", bundleEntity.getIdReportBundle());
                bundle.putSerializable("listAttentionArea", (Serializable) bundleEntity.getListAreaAtencion());
                mainCurrentFragment = new ReportDelegateFragment();
                addToBackStack = true;
                break;
            case R.id.fragment_report_update_history:
                fragmentName = "Seguimiento";
                bundle.putInt("idReport", bundleEntity.getIdReportBundle());
                bundle.putSerializable("listStatus", (Serializable) bundleEntity.getListEstatus());
                mainCurrentFragment = new ReportAddHistoryFragment();
                addToBackStack = true;
                break;
            case R.id.nav_welcome:
                clearBackStack = true;
                mainCurrentFragment = new WelcomeFragment();
                updateMenu = true;
                break;
            case R.id.nav_about:
                fragmentName = "Acerca de";
                mainCurrentFragment = new AboutFragment();
                addToBackStack = true;
                break;
            case R.id.nav_logout:
                mainCurrentFragment = new LogoutFragment();
                break;
            case R.id.welcome_btn_ticket_technical_support:
                fragmentName = "Soporte Técnico";
                bundle.putInt("idAttentionArea",AppPreference.BUNDLE_TECHNICAL_SUPPORT);
                mainCurrentFragment = new ReportNewShortcutFragment();
                addToBackStack = true;
                break;
            case R.id.welcome_btn_my_ticket_pending:
                fragmentName = "Mis pendientes";
                mainCurrentFragment = new MyTicketPendingFragment();
                addToBackStack = true;
                break;
            case R.id.welcome_btn_request_pending:
                fragmentName = "Reportes por atender";
                mainCurrentFragment = new RequestPendingFragment();
                addToBackStack = true;
                break;
            case R.id.welcome_btn_search_ticket_number:
                fragmentName = "Buscar";
                mainCurrentFragment = new SearchTicketNumberFragment();
                addToBackStack = true;
                break;
            case R.id.login_txv_register:
                fragmentName = "Registro";
                mainCurrentFragment = new RegisterFragment();
                addToBackStack = true;
                break;
            case R.id.login_txv_recoverPassword:
                fragmentName = "Recuperar contraseña";
                mainCurrentFragment = new RecoverPasswordFragment();
                addToBackStack = true;
                break;
            default:
                mainCurrentFragment = null;
                break;
        }

        // Administra el cambio de contenido
        if (mainCurrentFragment != null) {
            // Agrega los datos para el nuevo Fragment
            mainCurrentFragment.setArguments(bundle);

            // Define el cambio de Fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, mainCurrentFragment);

            if (addToBackStack) {
                // Si es necesario, agrega a la pila de Fragments
                fragmentTransaction.addToBackStack(fragmentName);
            }

            if (clearBackStack) {
                // Si es necesario, limpia la pila de Fragments
                clearBackStack(fragmentManager);
            }

            // Realiza el cambio de Fragment
            fragmentTransaction.commit();
        }

        if (updateMenu) {
            // Actualiza el menu cuando es necesario
            menuManager.updateMenuOptions(mainNavigationView, appPreferencesManager);
        }
    }

    // Metodo determinar el icono y acciones a mostrar y realizar en el Toolbar
    public void manageToolbar() {
        // Obtiene la profundidad del BackStack
        totalEvents = getSupportFragmentManager().getBackStackEntryCount();

        if (totalEvents > 0) {
            // Obtiene el nombre del Fragment actual
            appBarName = getSupportFragmentManager().getBackStackEntryAt(totalEvents - 1).getName();

            // Deshabilita los gestos del menu
            mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            // Establece el icono de regreso
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // Indica que se debe regresar al estado anterior
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            // Obtiene el nombre del Fragment por default
            appBarName = getString(R.string.app_name);

            // Habilita los gestos del menu
            mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            // Deshabilita el icono de regreso
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            // Establece el icono de tres lineas
            toggle.syncState();

            // Indica que debe abrir el menu desplegable
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        // Establece el nombre del AppBar
        getSupportActionBar().setTitle(appBarName);
    }

    // Metodo para limpiar el BackStack
    private void clearBackStack(FragmentManager fragmentManager) {
        while (fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStackImmediate();
        }
    }

    // Metodo que captura los eventos en el Menu
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Oculta el Menu de la Activity
        mainDrawerLayout.closeDrawer(GravityCompat.START);


        manageFragment(item.getItemId(), null);
        return true;
    }
}