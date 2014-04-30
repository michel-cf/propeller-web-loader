/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.creatingfuture.propeller.webLoader.servlets.listeners;

import eu.creatingfuture.propeller.webLoader.ApplicationContext;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Michel
 */
public class FreemarkerInitializer implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(FreemarkerInitializer.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Configuration cfg = new Configuration();

        try {
            //  cfg.setServletContextForTemplateLoading(sce.getServletContext(), "freemarker");
            cfg.setDirectoryForTemplateLoading(new File("freemarker"));
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        cfg.setIncompatibleImprovements(new Version(2, 3, 20));

        ApplicationContext.get().setFreemarkerConfig(cfg);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
