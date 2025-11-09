package com.patriclee.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * SpringDoc接口文档配置 - 基于RuoYi-Plus实现
 *
 * @author Patriclee
 */
@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty(name = "springdoc.api-docs.enabled", havingValue = "true", matchIfMissing = true)
public class SpringDocConfig {

    private final ServerProperties serverProperties;

    @Value("${springdoc.info.title:清虚观管理系统_接口文档}")
    private String title;

    @Value("${springdoc.info.description:用于管理清虚观业务信息，具体包括用户管理、部门管理、数据字典等模块}")
    private String description;

    @Value("${springdoc.info.version:1.0.0}")
    private String version;

    @Value("${springdoc.info.contact.name:Nine Factory Team}")
    private String contactName;

    @Value("${springdoc.info.contact.email:admin@theninefactory.com}")
    private String contactEmail;

    @Value("${springdoc.info.contact.url:https://github.com/theninefactory}")
    private String contactUrl;

    /**
     * 配置OpenAPI基本信息
     */
    @Bean
    @ConditionalOnProperty(name = "springdoc.api-docs.enabled", havingValue = "true", matchIfMissing = true)
    public OpenAPI customOpenAPI() {
        OpenAPI openApi = new OpenAPI();

        // 设置基本信息
        Info info = new Info()
                .title(title)
                .description(description)
                .version(version);

        // 设置联系人信息
        if (StringUtils.hasText(contactName)) {
            Contact contact = new Contact()
                    .name(contactName)
                    .email(contactEmail)
                    .url(contactUrl);
            info.contact(contact);
        }

        openApi.info(info);

        // 全局安全配置 - 支持Authorization header
        List<SecurityRequirement> securityRequirements = new ArrayList<>();
        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList("apiKey");
        securityRequirements.add(securityRequirement);
        openApi.security(securityRequirements);

        return openApi;
    }

    /**
     * 自定义OpenAPI处理器 - 处理上下文路径问题
     */
    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        String contextPath = serverProperties.getServlet().getContextPath();
        String finalContextPath = "";
        if (StringUtils.hasText(contextPath) && !"/".equals(contextPath)) {
            finalContextPath = contextPath;
        }

        String pathPrefix = finalContextPath;
        return openApi -> {
            Paths oldPaths = openApi.getPaths();
            if (oldPaths instanceof PlusPaths) {
                return;
            }
            PlusPaths newPaths = new PlusPaths();
            oldPaths.forEach((k, v) -> newPaths.addPathItem(pathPrefix + k, v));
            openApi.setPaths(newPaths);
        };
    }

    /**
     * 自定义Paths类 - 避免重复处理路径
     */
    static class PlusPaths extends Paths {
        public PlusPaths() {
            super();
        }
    }
}