package com.dopp.doppapi.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "security.cookie")
public class CookieProperties {

    private boolean secure;
    private String sameSite;

}
