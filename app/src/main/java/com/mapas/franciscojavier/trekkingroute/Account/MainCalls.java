package com.mapas.franciscojavier.trekkingroute.Account;

        import greendao.Usuario;

/**
 * Created by Josemar on 04-06-2015.
 */
public interface MainCalls {

    public void goToRegister(String email, String password);

    public void signup(Usuario client);

    public void login(String email, String password);

    public void goToHome();
}
