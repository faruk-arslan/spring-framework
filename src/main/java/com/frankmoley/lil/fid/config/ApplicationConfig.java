package com.frankmoley.lil.fid.config;


import com.frankmoley.lil.fid.service.GreetingService;
import com.frankmoley.lil.fid.service.OutputService;
import com.frankmoley.lil.fid.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationConfig {

    @Value("${app.greeting}")
    private String greeting;
    @Value("${app.name}")
    private String name;
    /**
     * This is the spring environment (includes system OS env. variables,
     * VM arguments, Application arguments as well as any configuration that is loaded).
     */
    @Value("#{new Boolean(environment['spring.profiles.active']!='dev')}")
    private boolean is24;



    @Autowired
    private GreetingService greetingService;
    @Autowired
    private TimeService timeService;

    @Bean
    // we use Bean to define spring application
    public TimeService timeService(){
        return new TimeService(is24);
    }

    @Bean
    public OutputService outputService(){
        return new OutputService(name, greetingService, timeService);
    }

    @Bean
    public GreetingService greetingService(){
        return new GreetingService(greeting);
    }
}
