package ie.gmit.ds;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class UserApiApplication extends Application<UserApiConfig> {

    public static void main(String[] args) throws Exception {
        new UserApiApplication().run(args);
    }

    public void run(UserApiConfig artistApiConfig, Environment environment) throws Exception {

        final ExampleHealthCheck healthCheck = new ExampleHealthCheck();
        environment.healthChecks().register("example", healthCheck);

        final UserApiResource resource = new UserApiResource();

        environment.jersey().register(resource);
    }
}
