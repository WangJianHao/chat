package com.sen.chat.gateway.config;

import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/3 19:49
 */
@Configuration
public class SentinelConfiguration {

//    private final List<ViewResolver> viewResolvers;
//
//    private final ServerCodecConfigurer serverCodecConfigurer;
//
//    public SentinelConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider,
//                                 ServerCodecConfigurer serverCodecConfigurer) {
//        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
//        this.serverCodecConfigurer = serverCodecConfigurer;
//    }
//
//
//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
//        // Register the block exception handler for Spring Cloud Gateway.
//        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
//
//    }
//
//
//    @PostConstruct
//    public void doInit() {
//
//        // initSystemRule();
//
//        initCustomizedApis();
//
//        initGatewayRules();
//
//    }
//
//
//    private void initCustomizedApis() {
//
//        Set<ApiDefinition> definitions = new HashSet<>();
//        ApiDefinition api1 = new ApiDefinition("customized_api")
//                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
//                    add(new ApiPathPredicateItem().setPattern("/api/**")
//                            .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
//                }});
//
//        ApiDefinition api2 = new ApiDefinition("book_content_api")
//                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
//                    add(new ApiPathPredicateItem().setPattern("/api/book/queryBookContent**")
//                            .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
//                }});
//
//        definitions.add(api1);
//        definitions.add(api2);
//
//        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
//
//    }
//
//
//    /**
//     * 自定义网关限流规则（反爬虫机制）
//     * <p>
//     * 1.对所有api接口通过IP进行限流,每个IP，2秒钟内请求数量大于10，即视为爬虫
//     * <p>
//     * 2.对小说内容接口访问进行限流，每个IP，1秒钟请求数量大于1，则视为爬虫
//     */
//
//    private void initGatewayRules() {
//
//        Set<GatewayFlowRule> rules = new HashSet<>();
//        rules.add(new GatewayFlowRule("customized_api")
//                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME)
//                .setCount(10)
//                .setIntervalSec(2)
//                .setParamItem(new GatewayParamFlowItem()
//                        .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)
//                )
//        );
//
//        rules.add(new GatewayFlowRule("book_content_api")
//                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME)
//                .setCount(1)
//                .setIntervalSec(1)
//                .setParamItem(new GatewayParamFlowItem()
//                        .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)
//                )
//        );
//        GatewayRuleManager.loadRules(rules);
//    }
}
