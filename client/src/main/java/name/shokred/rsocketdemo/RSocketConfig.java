package name.shokred.rsocketdemo;

import io.rsocket.*;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.springframework.context.annotation.*;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.*;
import org.springframework.util.MimeTypeUtils;

@Configuration
public class RSocketConfig {

    @Bean
    public RSocket rSocket() {
        return RSocketFactory.connect()
                .mimeType(MimeTypeUtils.APPLICATION_JSON_VALUE, MimeTypeUtils.APPLICATION_JSON_VALUE)
                .frameDecoder(PayloadDecoder.ZERO_COPY)
                .transport(TcpClientTransport.create(7000))
                .start()
                .block();
    }

    @Bean
    public RSocketRequester rSocketRequester(RSocketStrategies rSocketStrategies) {
        return RSocketRequester.wrap(rSocket(), MimeTypeUtils.APPLICATION_JSON, MimeTypeUtils.APPLICATION_JSON, rSocketStrategies);
    }
}
