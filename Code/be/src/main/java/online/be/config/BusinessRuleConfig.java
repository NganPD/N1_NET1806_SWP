package online.be.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@ConfigurationProperties(prefix = "booking")
@Component
@Getter
public class BusinessRuleConfig {
    private int fixedCancelDays;
    private int flexibleCancelHours;
    private int dailyCancelHours;
}
