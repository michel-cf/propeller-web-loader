/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.creatingfuture.propeller.webLoader.servlets;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Michel
 */
public class WebRequestServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(WebRequestServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        String getUrl = req.getParameter("get");
        if (getUrl == null) {
            resp.sendError(400);
            return;
        }
        logger.info(getUrl);
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL(getUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);

            resp.setContentType(connection.getContentType());
            resp.setContentLengthLong(connection.getContentLengthLong());
            logger.log(Level.INFO, "Content length: {0} response code: {1} content type: {2}", new Object[]{connection.getContentLengthLong(), connection.getResponseCode(), connection.getContentType()});
            resp.setStatus(connection.getResponseCode());
            IOUtils.copy(connection.getInputStream(), resp.getOutputStream());
        } catch (IOException ioe) {
            resp.sendError(500, ioe.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
