package com.sekarre.helpcenterchat.config;

import com.sekarre.helpcenterchat.domain.User;
import com.sekarre.helpcenterchat.repositories.UserRepository;
import com.sekarre.helpcenterchat.security.jwt.JwtTokenUtil;
import com.sekarre.helpcenterchat.services.chat.security.ChatAuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.util.Objects;

import static com.sekarre.helpcenterchat.util.SimpMessageHeaderUtil.getChannelIdFromDestinationHeader;
import static com.sekarre.helpcenterchat.util.SimpMessageHeaderUtil.getUserFromHeaders;


@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@Slf4j
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
@Configuration
public class WebSocketConfigRabbitMQ implements WebSocketMessageBrokerConfigurer {

    public static final String ERRORS = "errors";

    @Value("${spring.chat.rabbitmq.login}")
    private String login;

    @Value("${spring.chat.rabbitmq.password}")
    private String password;

    @Value("${spring.chat.rabbitmq.host}")
    private String host;

    @Value("${spring.chat.rabbitmq.port}")
    private Integer port;

    private final ChatAuthorizationService chatAuthorizationService;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setPathMatcher(new AntPathMatcher("."));
        registry.setPreservePublishOrder(true);
        registry.setApplicationDestinationPrefixes("/app")
                .enableStompBrokerRelay("/topic")
                .setRelayHost(host)
                .setRelayPort(port)
                .setSystemLogin(login)
                .setSystemPasscode(password);
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry
                .setSendBufferSizeLimit(2000 * 1024 * 1024)
                .setMessageSizeLimit(2000 * 1024 * 1024);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (Objects.nonNull(accessor) && Objects.nonNull(accessor.getCommand())) {
                    switch (accessor.getCommand()) {
                        case CONNECT -> {
                            Message<?> message1 = onConnect(message, accessor);
                            if (message1 != null) return message1;
                        }
                        case SUBSCRIBE -> onSubscribe(message);
                        case DISCONNECT -> onDisconnect(accessor);
                    }
                }
                return message;
            }

            private Message<?> onConnect(Message<?> message, StompHeaderAccessor accessor) {
                String token = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION).split(" ")[1].trim();
                log.debug("Header auth token: " + token);

                if (!jwtTokenUtil.validate(token)) {
                    return message;
                }

                User user = userRepository.findByUsername(jwtTokenUtil.getUsername(token))
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
                accessor.setUser(authentication);
                return null;
            }

            private void onDisconnect(StompHeaderAccessor accessor) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (Objects.nonNull(authentication)) {
                    log.debug("Disconnected Auth : " + authentication.getName());
                } else {
                    log.debug("Disconnected Sess : " + accessor.getSessionId());
                }
            }

            private void onSubscribe(Message<?> message) {
                SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(message);
                User user = getUserFromHeaders(headers);
                String channelId = getChannelIdFromDestinationHeader(headers.getDestination());
                if (!ERRORS.equals(channelId)) {
                    chatAuthorizationService.checkIfUserAuthorizedToJoinChannel(user, getChannelIdFromDestinationHeader(headers.getDestination()));
                }
            }

            @Override
            public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
                StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);
                if (sha.getCommand() == null) {
                    return;
                }

                String sessionId = sha.getSessionId();
                switch (sha.getCommand()) {
                    case CONNECT -> log.debug("STOMP Connect [sessionId: " + sessionId + "]");
                    case CONNECTED -> log.debug("STOMP Connected [sessionId: " + sessionId + "]");
                    case DISCONNECT -> log.debug("STOMP Disconnect [sessionId: " + sessionId + "]");
                    default -> {
                    }
                }
            }
        });
    }
}