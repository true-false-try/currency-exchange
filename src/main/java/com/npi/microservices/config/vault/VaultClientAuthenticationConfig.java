package com.npi.microservices.config.vault;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.cloud.vault.config.VaultProperties;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.vault.VaultException;
import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.config.AbstractVaultConfiguration;
import org.springframework.vault.support.VaultToken;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
public class VaultClientAuthenticationConfig implements EnvironmentPostProcessor, Ordered {

    public static final int ORDER = ConfigDataEnvironmentPostProcessor.ORDER - 1;
    public static final String VAULT_HOST = "VAULT_HOST";
    public static final String VAULT_USERNAME = "VAULT_USERNAME";
    public static final String VAULT_PASSWORD = "VAULT_PASSWORD";
    public static final String DEFAULT_SCHEME = "https";
    public static final String DEFAULT_PATH = "v1/auth/userpass/login";
    public static final String DEFAULT_REQUEST_BODY_TEMPLATE = "{\"password\": \"%s\"}";

    private final ConfigurableBootstrapContext bootstrapContext;

    public VaultClientAuthenticationConfig(ConfigurableBootstrapContext bootstrapContext) {
        this.bootstrapContext = bootstrapContext;
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (!isEnabled(environment)) {
            return;
        }
        Map<String, Object> systemEnvironment = environment.getSystemEnvironment();
        String host = (String) systemEnvironment.get(VAULT_HOST);
        String username = (String) systemEnvironment.get(VAULT_USERNAME);
        String password = (String) systemEnvironment.get(VAULT_PASSWORD);

        bootstrapContext.registerIfAbsent(ClientAuthentication.class,
                context -> clientAuthentication(host, username, password));
        bootstrapContext.addCloseListener(event -> {
            GenericApplicationContext gac = (GenericApplicationContext) event.getApplicationContext();
            ClientAuthentication instance = event.getBootstrapContext().get(ClientAuthentication.class);
            gac.registerBean("clientAuthentication", ClientAuthentication.class, () -> instance);
        });
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    protected ClientAuthentication clientAuthentication(String host, String username, String password) {
        ClientHttpRequestFactory factory;
        if (bootstrapContext.isRegistered(AbstractVaultConfiguration.ClientFactoryWrapper.class)) {
            factory = bootstrapContext.get(AbstractVaultConfiguration.ClientFactoryWrapper.class)
                    .getClientHttpRequestFactory();
        } else {
            factory = new AbstractVaultConfiguration.ClientFactoryWrapper(new SimpleClientHttpRequestFactory())
                    .getClientHttpRequestFactory();
        }

        VaultProperties properties = bootstrapContext.getOrElse(VaultProperties.class, null);
        String scheme = properties != null ? properties.getScheme() : DEFAULT_SCHEME;
        return new UserPassAuthentication(scheme, host, DEFAULT_PATH, username, password, factory);
    }

    protected boolean isEnabled(ConfigurableEnvironment environment) {
        Map<String, Object> systemEnvironment = environment.getSystemEnvironment();
        return systemEnvironment.get(VAULT_HOST) != null &&
                systemEnvironment.get(VAULT_USERNAME) != null &&
                systemEnvironment.get(VAULT_PASSWORD) != null;
    }

    private static class UserPassAuthentication implements ClientAuthentication {
        private final String url;
        private final String password;
        private final RestOperations restOperations;
        private final Logger logger = LoggerFactory.getLogger(UserPassAuthentication.class);

        private UserPassAuthentication(String scheme, String host, String path, String user, String password, ClientHttpRequestFactory requestFactory) {
            this.url = UriComponentsBuilder.newInstance()
                    .scheme(scheme)
                    .host(host)
                    .pathSegment(path, user)
                    .build()
                    .toUriString();
            this.password = password;
            this.restOperations = new RestTemplate(requestFactory);
            log.debug("Vault url {}", this.url);
        }

        @SuppressWarnings({"rawtypes", "NullableProblems"})
        @Override
        public VaultToken login() throws VaultException {
            logger.debug("Sending JSON Request to Vault: {}", url);
            VaultToken vaultToken = Optional.of(restOperations.postForEntity(url, String.format(DEFAULT_REQUEST_BODY_TEMPLATE, password), Map.class))
                    .map(HttpEntity::getBody)
                    .map(b -> (Map) b.get("auth"))
                    .map(a -> (String) a.get("client_token"))
                    .map(VaultToken::of)
                    .orElseThrow(NoSuchElementException::new);
            log.debug("Vault token {}", vaultToken.getToken());
            return vaultToken;
        }
    }
}
