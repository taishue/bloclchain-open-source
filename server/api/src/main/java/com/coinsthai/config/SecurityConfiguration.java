package com.coinsthai.config;

import com.coinsthai.security.AuthenticationSuccessHandler;
import com.coinsthai.security.AuthorizationFailureHandler;
import com.coinsthai.security.BearerAuthorizationHeaderTokenResolver;
import com.coinsthai.security.*;
import in.clouthink.daas.security.token.annotation.EnableToken;
import in.clouthink.daas.security.token.configure.TokenConfigurer;
import in.clouthink.daas.security.token.configure.TokenConfigurerAdapter;
import in.clouthink.daas.security.token.configure.UrlAclBuilder;
import in.clouthink.daas.security.token.configure.UrlAclProviderBuilder;
import in.clouthink.daas.security.token.core.AuthenticationFeature;
import in.clouthink.daas.security.token.core.FeatureConfigurer;
import in.clouthink.daas.security.token.core.TokenLifeSupport;
import in.clouthink.daas.security.token.repackage.org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import in.clouthink.daas.security.token.repackage.org.springframework.security.web.util.matcher.OrRequestMatcher;
import in.clouthink.daas.security.token.repackage.org.springframework.security.web.util.matcher.RequestMatcher;
import in.clouthink.daas.security.token.spi.DigestMetadataProvider;
import in.clouthink.daas.security.token.spi.IdentityProvider;
import in.clouthink.daas.security.token.spi.TokenProvider;
import in.clouthink.daas.security.token.support.i18n.MessageProvider;
import in.clouthink.daas.security.token.support.web.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Configuration
@EnableToken
public class SecurityConfiguration {

    @Primary
    @Bean
    public TokenProvider tokenProvider() {
        return new RedisTokenProvider();
    }

    @Primary
    @Bean
    public IdentityProvider identityProvider() {
        return new UserIdentityProvider();
    }

    @Primary
    @Bean
    public DigestMetadataProvider digestMetadataSampleProvider() {
        return new UserDigestMetadataProvider();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandler();
    }

    @Bean
    public TokenConfigurer tokenConfigurer() {
        return new TokenConfigurerAdapter() {

            AuthorizationFailureHandler authorizationFailureHandler = new AuthorizationFailureHandler();

            CompositeTokenResolver tokenResolver;

            private TokenResolver getTokenResolver() {
                if (tokenResolver == null) {
                    tokenResolver = new CompositeTokenResolver();
                    tokenResolver.getResolvers()
                                 .add(new BearerAuthorizationHeaderTokenResolver());
                    tokenResolver.getResolvers().add(new CookieTokenResolver());
                }
                return tokenResolver;
            }

            @Override
            public void configure(MessageProvider messageProvider) {
                messageProvider.setLocale(Locale.CHINESE);
            }

            @Override
            public void configure(LoginEndpoint endpoint) {
                endpoint.setLoginProcessesUrl("/login");
                endpoint.setAuthenticationSuccessHandler(authenticationSuccessHandler());
            }

            @Override
            public void configure(LogoutEndpoint endpoint) {
                endpoint.setLogoutProcessesUrl("/logout");
                endpoint.setTokenResolver(getTokenResolver());
                endpoint.setUseStrict(false);
            }

            @Override
            public void configure(AuthorizationFilter filter) {
                List<RequestMatcher> matchers = new ArrayList<>();
                matchers.add(new AntPathRequestMatcher("/api/home/**"));
                matchers.add(new AntPathRequestMatcher("/api/admin/**"));
                filter.setUrlRequestMatcher(new OrRequestMatcher(matchers));
                filter.setAuthorizationFailureHandler(authorizationFailureHandler);
            }

            @Override
            public void configure(AuthenticationFilter filter) {
                List<RequestMatcher> matchers = new ArrayList<>();
                matchers.add(new AntPathRequestMatcher("/api/home/**"));
                matchers.add(new AntPathRequestMatcher("/api/admin/**"));
                filter.setUrlRequestMatcher(new OrRequestMatcher(matchers));
                filter.setAuthorizationFailureHandler(authorizationFailureHandler);
            }

            @Override
            public void configure(PreAuthenticationFilter filter) {
                filter.setProcessesUrl("/api/**");
                filter.setTokenResolver(getTokenResolver());
            }

            @Override
            public void configure(TokenLifeSupport tokenLifeSupport) {
                tokenLifeSupport.setTokenTimeout(7 * 24 * 60 * 60 * 1000); // 1周
                tokenLifeSupport.enableMultiTokens();
            }

            @Override
            public void configure(UrlAclProviderBuilder builder) {
                builder.add(UrlAclBuilder.antPathBuilder()
                                         .url("/api/home/**")
                                         .grantRules("ROLE:USER"))

                       .add(UrlAclBuilder.antPathBuilder()
                                         .url("/api/admin/**")
                                         .grantRules("ROLE:ADMIN"));
            }

            @Override
            public void configure(FeatureConfigurer featureConfigurer) {
                featureConfigurer.enable(AuthenticationFeature.CORS)
                                 .enable(AuthenticationFeature.IGNORE_PRE_AUTHN_ERROR)
                                 .disable(AuthenticationFeature.STRICT_TOKEN);
            }
        };
    }

}
