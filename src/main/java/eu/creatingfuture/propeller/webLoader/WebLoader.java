/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.creatingfuture.propeller.webLoader;

import eu.creatingfuture.propeller.webLoader.interfaces.Compiler;
import eu.creatingfuture.propeller.webLoader.interfaces.PropellerCommunicator;
import eu.creatingfuture.propeller.webLoader.propeller.LinuxOpenSpin;
import eu.creatingfuture.propeller.webLoader.propeller.LinuxPropellerLoad;
import eu.creatingfuture.propeller.webLoader.propeller.MacOpenSpin;
import eu.creatingfuture.propeller.webLoader.propeller.MacPropellerLoad;
import eu.creatingfuture.propeller.webLoader.propeller.WindowsOpenSpin;
import eu.creatingfuture.propeller.webLoader.propeller.WindowsPropellerLoad;
import eu.creatingfuture.propeller.webLoader.utils.OsCheck;
import java.awt.Desktop;
import java.net.URI;
import java.util.logging.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 *
 * @author Michel
 */
public class WebLoader {

    private static final Logger logger = Logger.getLogger(WebLoader.class.getName());

    private static Compiler compiler;
    private static PropellerCommunicator propellerCommunicator;

    public static void main(String[] args) throws Exception {
        OsCheck.OSType os = OsCheck.getOperatingSystemType();
        switch (os) {
            case Windows:
                compiler = new WindowsOpenSpin();
                propellerCommunicator = new WindowsPropellerLoad();
                break;
            case Linux:
                compiler = new LinuxOpenSpin();
                propellerCommunicator = new LinuxPropellerLoad();
                break;
            case MacOS:
                compiler = new MacOpenSpin();
                propellerCommunicator = new MacPropellerLoad();
                break;
            default:
                logger.warning("This OS is currently not supported: " + os);
                System.exit(1);
        }
        //   PropertyConfigurator.configure("log4j.properties");

        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        server.addConnector(connector);

        // Replaced by filter in web.xml
/*
         ServletHandler servletHandler = new ServletHandler();
         //servletHandler.addServletWithMapping(server, null);
         servletHandler.addFilterWithMapping(GuiceFilter.class, "*", EnumSet.of(DispatcherType.REQUEST));
         //      servletHandler.addLifeCycleListener(new S);
         */
        ResourceHandler webcontentHandler = new ResourceHandler();
        webcontentHandler.setDirectoriesListed(true);
        webcontentHandler.setWelcomeFiles(new String[]{"index.html"});
        webcontentHandler.setResourceBase("./webcontent");

        WebAppContext context = new WebAppContext();
        context.setResourceBase("src/main/webapp");
        context.setContextPath("/webapp");
        context.setParentLoaderPriority(true);
        server.setHandler(context);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{webcontentHandler, /* servletHandler,*/ context, new DefaultHandler()});
        server.setHandler(handlers);

        server.start();

        String url = "http://localhost:" + connector.getLocalPort();
        System.out.println("Open url: " + url);
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(new URI(url));
        }

        server.join();
//        server.stop();
    }

    public static Compiler getCompiler() {
        return compiler;
    }

    public static PropellerCommunicator getPropellerCommunicator() {
        return propellerCommunicator;
    }

}
