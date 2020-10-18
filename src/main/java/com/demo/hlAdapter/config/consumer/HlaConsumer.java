package com.demo.hlAdapter.config.consumer;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;
import org.apache.http.client.utils.URIBuilder;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.demo.hlAdapter.entity.TokenDetails;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HlaConsumer {

    // http://localhost:8084/userRole/get/Oauth_Official
    @Autowired
    private RestTemplate restTemplate;

    @Value("${oauth2.host:localhost}")
    private String oauth2Host;

    @Value("${oauth2.port:8084}")
    private int port;

    @Value("${oauth2.url:/userRole/get/{userRole}}")
    private String oauth2Url;

    @Value("${oauth2.protocol:http}")
    private String oauth2Protocol;

    @KafkaListener(topics = "token", containerFactory = "concurrentKafkaListenerContainerFactory")
    public void saveTokenDetails(ConsumerRecord<Object, Object> record) {
        TokenDetails tokenDetails = getTokenDetails(record.value().toString());
    }

    @KafkaListener(topics = "userRole",
                    containerFactory = "concurrentKafkaListenerContainerFactory")
    public void persistUserRole(ConsumerRecord<Object, Object> record) {
        log.info("::::HlaConsumer Class, persistUserRole method::::");
        String userRole = record.value().toString();
        log.info(":::::tenantDetails {}", userRole);
        persistUserRoleInDB(userRole);

    }

    private void persistUserRoleInDB(String tenantDetails) {
        log.info(":::::HlaConsumer Class, persistUserRoleInDB method:::::");
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme(oauth2Protocol);
        uriBuilder.setHost(oauth2Host);
        uriBuilder.setPort(port);
        uriBuilder.setPath(oauth2Url.replace("{userRole}", "Oauth_Official"));
        try {
            URL url = uriBuilder.build().toURL();
            log.info(":::::url {}", url);
            UriComponentsBuilder uriComponentBuilder = UriComponentsBuilder.fromUri(url.toURI());
            log.info(":::::uriComponentBuilder {}", uriComponentBuilder.toUriString());

            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            ResponseEntity<ModelMap> responseEntity =
                            restTemplate.exchange(uriComponentBuilder.toUriString(), HttpMethod.GET,
                                            httpEntity, ModelMap.class);
            log.info(":::::responseEntity {}", responseEntity.getBody());
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private TokenDetails getTokenDetails(String jwtToken) {
        TokenDetails tokenDetails = new TokenDetails();
        String[] tokenValue = jwtToken.split(" ")[1].split("\\.");
        if (tokenValue.length < 3) {
            log.info(":::::Invalid Token.");
            return null;
        }
        String token = tokenValue[1];
        Base64.Decoder decoder = Base64.getDecoder();
        String tokenBody = new String(decoder.decode(token.getBytes()));
        log.info(":::::tokenBody {}", tokenBody);
        return tokenDetails;
    }
}
