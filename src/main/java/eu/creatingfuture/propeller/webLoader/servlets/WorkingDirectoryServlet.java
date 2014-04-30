/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.creatingfuture.propeller.webLoader.servlets;

import eu.creatingfuture.propeller.webLoader.ApplicationContext;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Michel
 */
public class WorkingDirectoryServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(WorkingDirectoryServlet.class.getName());

    private File workingDirectory = new File(System.getProperty("user.home"));

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getParameter("path");
        if (path == null) {
            path = "";
        }
        File file = new File(workingDirectory, path);
        System.out.println(file.getCanonicalPath());

        if (!path.endsWith("/")) {
            path += "/";
        }

        if (!file.exists()) {
            resp.sendError(404);
        } else if (file.isDirectory()) {
            List<File> directoryContent = new ArrayList<File>(Arrays.asList(file.listFiles()));
            List<String> directoryListing = new ArrayList<String>(directoryContent.size());
            for (File content : directoryContent) {
                if (content.isFile()) {
                    directoryListing.add(path + content.getName());
                } else if (content.isDirectory()) {
                    directoryListing.add(path + content.getName() + "/");
                }
            }

            Map root = new HashMap();
            root.put("directoryname", path);
            root.put("files", directoryListing);

            Template template = ApplicationContext.get().getFreemarkerConfig().getTemplate("directory-index.ftl");
            try {
                template.process(root, new OutputStreamWriter(resp.getOutputStream()));
            } catch (TemplateException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        } else if (file.isFile()) {
            FileInputStream fileToDownload = new FileInputStream(file);
            ServletOutputStream output = resp.getOutputStream();
            //         response.setContentType("application/zip");
            resp.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            resp.setContentLength(fileToDownload.available());
            int c;
            while ((c = fileToDownload.read()) != -1) {
                output.write(c);
            }
            output.flush();
            output.close();
            fileToDownload.close();
        }
    }

}
