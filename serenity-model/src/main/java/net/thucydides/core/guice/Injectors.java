package net.thucydides.core.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Somewhere to hold the Guice injector.
 * There might be a better way to do this.
 */
public class Injectors {

    private static Map<String,Injector>  injectors = Collections.synchronizedMap(new HashMap<>());

    private static final Logger LOGGER = LoggerFactory.getLogger(Injectors.class);

    public static synchronized Injector getInjector() {
        return getInjector(new ThucydidesModule());
    }
    
    public static synchronized Injector getInjector(com.google.inject.Module module) {
//        LOGGER.info("Getting Injector");
        String moduleClassName = module.getClass().getCanonicalName();
//        LOGGER.info("Module class name: " + moduleClassName);
        Injector injector = injectors.get(moduleClassName);
        if (injector == null) {
            LOGGER.info("Creating Injector");
    		injector = Guice.createInjector(module);
    		injectors.put(moduleClassName, injector);
    	}
    	return injector;
    }
}