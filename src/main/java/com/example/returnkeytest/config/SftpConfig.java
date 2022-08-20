package com.example.returnkeytest.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sftp")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SftpConfig {
    private String host;
    private Integer port;
    private String username;
    private String password;
    private Integer sessionTimeout;
    private Integer channelTimeout;
    private Order order;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Order {
        private String localFileName;
        private String remoteFilePath;
        private String remoteFileName;
    }
}
