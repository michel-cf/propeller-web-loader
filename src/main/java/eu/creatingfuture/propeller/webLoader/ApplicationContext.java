/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.creatingfuture.propeller.webLoader;

import freemarker.template.Configuration;

/**
 *
 * @author Michel
 */
public class ApplicationContext {

    private static ApplicationContext applicationContext;

    private Configuration freemarkerConfig;

    private ApplicationContext() {
    }

    public static ApplicationContext get() {
        if (applicationContext == null) {
            applicationContext = new ApplicationContext();
        }
        return applicationContext;
    }

    public void setFreemarkerConfig(Configuration freemarkerConfig) {
        this.freemarkerConfig = freemarkerConfig;
    }

    public Configuration getFreemarkerConfig() {
        return freemarkerConfig;
    }

}
