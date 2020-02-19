package name.shokred.rsocketdemo;

import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Controller
public class Server {

    @MessageMapping("channel.{param}")
    public Flux<String> channel(@DestinationVariable String param, @Payload Flux<String> flux) {
        return Flux.merge(
                flux.doOnNext(x -> System.out.println("Received: " + x)),
                Flux.interval(Duration.ofSeconds(1)).map(aLong -> aLong.toString() + param)
        );
    }
}
