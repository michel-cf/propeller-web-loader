/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.creatingfuture.propeller.webLoader.servlets;

import eu.creatingfuture.propeller.webLoader.ApplicationContext;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import javax.swing.JFileChooser;

/**
 *
 * @author Michel
 */
public class WorkingDirectoryServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(WorkingDirectoryServlet.class.getName());

    private File workingDirectory = new File(System.getProperty("user.home"));

    public WorkingDirectoryServlet() {
        JFileChooser workingDirectoryChooser = new JFileChooser();
        workingDirectoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        workingDirectoryChooser.setApproveButtonText("Select");
        workingDirectoryChooser.setDialogTitle("Select working directory");
        int returnVal = workingDirectoryChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            workingDirectory = workingDirectoryChooser.getSelectedFile();
            //          System.out.println(beanFile.getAbsolutePath() + beanFile.isDirectory());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        String path = req.getParameter("path");
        if (path == null) {
            path = "";
        }
        path = path.replace("%20", " ");
        File file = new File(workingDirectory, path);

        if (!file.getCanonicalPath().startsWith(workingDirectory.getCanonicalPath())) {
            resp.sendError(403);
            return;
        }

        System.out.println(file.getCanonicalPath());

        if (!path.endsWith("/")) {
            path += "/";
        }

        if (!file.exists()) {
            resp.sendError(404);
        } else if (file.isDirectory()) {
            boolean listingSuccess = true;
            List<File> directoryContent = new ArrayList<File>();
            try {
                directoryContent.addAll(Arrays.asList(file.listFiles()));
            } catch (NullPointerException npe) {
                listingSuccess = false;
            }
//            List<String> directoryListing = new ArrayList<String>(directoryContent.size());
//            for (File content : directoryContent) {
//                if (content.isFile()) {
//                    directoryListing.add(path + content.getName());
//                } else if (content.isDirectory()) {
//                    directoryListing.add(path + content.getName() + "/");
//                }
//            }

            Map root = new HashMap();
            root.put("showparentdir", path.length() > 1);
            root.put("parentdir", (path.length() > 1) ? path.substring(0, path.substring(0, path.length() - 1).lastIndexOf("/"))
                    : "");
            root.put("directoryname", path);
            root.put("files", directoryContent);
            root.put("listingsuccess", listingSuccess);

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

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        String path = req.getParameter("path");
        if (path == null) {
            resp.sendError(400);
        }

        File file = new File(workingDirectory, path);
        if (!file.getCanonicalPath().startsWith(workingDirectory.getCanonicalPath())) {
            resp.sendError(403);
            return;
        }

        System.out.println(file.getCanonicalPath());

        try {
            byte[] buffer = new byte[1024 * 1024];
            InputStream input = req.getInputStream();
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                System.out.println(bytesRead);
                output.write(buffer, 0, bytesRead);
            }
            output.flush();
            output.close();
            input.close();
        } catch (IOException ioe) {
            resp.sendError(500);
            return;
        }
    }

}
