package top.mstudy.utils.time;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author machao
 * @description:
 * @date 2022-09-02
 */
@Data @Component @ConfigurationProperties("time") public class TimeHandlerConfig {

    private String handler;

}
