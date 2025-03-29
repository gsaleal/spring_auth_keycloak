package com.leal.keycloak;

import jakarta.ws.rs.core.Response;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.*;
import org.keycloak.services.messages.Messages;

import java.util.UUID;

public class LealAuthenticator implements Authenticator {

    private static final String KONNEQT_TOKEN_HEADER = "X-Konneqt-Token";
    private static final String DEFAULT_REALM = "test";

    @Override
    public void authenticate(AuthenticationFlowContext authenticationFlowContext) {
        String token = authenticationFlowContext.getHttpRequest().getHttpHeaders().getHeaderString(KONNEQT_TOKEN_HEADER);
        if (token.isEmpty()) {
            Response errorResponse = authenticationFlowContext.form()
                    .setError(Messages.MISSING_PARAMETER)
                    .createErrorPage(Response.Status.UNAUTHORIZED);
            authenticationFlowContext.failure(AuthenticationFlowError.INVALID_CREDENTIALS, errorResponse);
            return;
        }
        String email = token.trim().toLowerCase();

        KeycloakSession session = authenticationFlowContext.getSession();
        RealmModel realm = session.realms().getRealmByName(DEFAULT_REALM);
        if (realm == null) {
            authenticationFlowContext.failure(AuthenticationFlowError.INTERNAL_ERROR);
            return;
        }
        UserModel user = findOrCreateUser(session, realm, email);

        // Autenticação bem sucedida
        authenticationFlowContext.setUser(user);
        authenticationFlowContext.success();
    }

    @Override
    public void action(AuthenticationFlowContext authenticationFlowContext) {
    }

    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public boolean configuredFor(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {

    }

    @Override
    public void close() {

    }

    private UserModel findOrCreateUser(KeycloakSession session, RealmModel realm, String email) {
        UserProvider userProvider = session.users();
        UserModel user = userProvider.getUserByEmail(realm, email);

        if (user == null) {
            // Criar novo user
            user = userProvider.addUser(realm, UUID.randomUUID().toString(), email, true, false);
            user.setEmail(email);
            user.setEnabled(true);

            // Atribui roles padrão (opcional)
            RoleModel defaultRole = realm.getRole("user");
            if (defaultRole != null) {
                user.grantRole(defaultRole);
            }
        }

        return user;
    }
}
