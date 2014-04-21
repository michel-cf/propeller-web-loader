/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.creatingfuture.propeller.webLoader.servlets;

import com.google.gson.Gson;
import eu.creatingfuture.propeller.webLoader.WebLoader;
import eu.creatingfuture.propeller.webLoader.utils.PropellentResult;
import eu.creatingfuture.propeller.webLoader.utils.PropellerPostAction;
import eu.creatingfuture.propeller.webLoader.utils.PropellerPutAction;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Michel
 */
public class PropellerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        List<String> ports = WebLoader.getPropellerCommunicator().getPorts(); // propellent.getPorts();

        out.print(gson.toJson(ports));
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        PropellentResult result = new PropellentResult(this);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        // Parse and validate parameters
        String actionString = req.getParameter("action");
        if (actionString == null) {
            // return error
            result.setSucces(false);
            result.setCode(101);
            result.setMessage("Action is not defined");
            out.print(gson.toJson(result));
            out.flush();
            return;
        }
        PropellerPostAction action = null;
        try {
            action = PropellerPostAction.valueOf(actionString);
        } catch (IllegalArgumentException iae) {
            // return error
            result.setSucces(false);
            result.setCode(102);
            result.setMessage("Invalid action");
            out.print(gson.toJson(result));
            out.flush();
            return;
        }
        if (action == null) {
            result.setSucces(false);
            result.setCode(102);
            result.setMessage("Invalid action");
            out.print(gson.toJson(result));
            out.flush();
            return;
        }

        String spinCode = req.getParameter("code");

        File blocklyAppFile = File.createTempFile("blocklyapp", ".spin");
        try (PrintWriter blocklyAppWriter = new PrintWriter(blocklyAppFile)) {
            blocklyAppWriter.print(spinCode);
            blocklyAppWriter.flush();
        }

        boolean success = false;
        switch (action) {
            case COMPILE:
                success = WebLoader.getCompiler().compile(blocklyAppFile);
                break;
            case LOAD_RAM:
            case LOAD_EEPROM:
                success = compileAndRun(action, blocklyAppFile);
                break;
        }
        result.setSucces(success);
        result.setMessage(WebLoader.getCompiler().getLastOutput() + "\n\n" + WebLoader.getPropellerCommunicator().getLastOutput());
        result.setCode(WebLoader.getCompiler().getLastExitValue());

        blocklyAppFile.delete();

        out.print(gson.toJson(result));
        out.flush();
    }

    private boolean compileAndRun(PropellerPostAction action, File blocklyAppFile) throws IOException {
        boolean success = true;

        File compiledFile = null;
        switch (action) {
            case LOAD_RAM:
                compiledFile = File.createTempFile("blocklyapp", ".binary");
                success = WebLoader.getCompiler().compileForRam(blocklyAppFile, compiledFile);
                break;
            case LOAD_EEPROM:
                compiledFile = File.createTempFile("blocklyapp", ".eeprom");
                success = WebLoader.getCompiler().compileForEeprom(blocklyAppFile, compiledFile);
                break;
        }
        if (success) {
            switch (action) {
                case LOAD_RAM:
                    success = WebLoader.getPropellerCommunicator().loadIntoRam(compiledFile, null);
                    break;
                case LOAD_EEPROM:
                    success = WebLoader.getPropellerCommunicator().loadIntoEeprom(compiledFile, null);
                    break;
            }

        }
        if (compiledFile != null) {
            compiledFile.delete();
        }

        return success;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        PropellentResult result = new PropellentResult(this);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        // Parse and validate parameters
        String actionString = req.getParameter("action");
        if (actionString == null) {
            // return error
            result.setSucces(false);
            result.setCode(101);
            result.setMessage("Action is not defined");
            out.print(gson.toJson(result));
            out.flush();
            return;
        }

        PropellerPutAction action = null;
        try {
            action = PropellerPutAction.valueOf(actionString);
        } catch (IllegalArgumentException iae) {
            // return error
            result.setSucces(false);
            result.setCode(102);
            result.setMessage("Invalid action");
            out.print(gson.toJson(result));
            out.flush();
            return;
        }
        if (action == null) {
            result.setSucces(false);
            result.setCode(102);
            result.setMessage("Invalid action");
            out.print(gson.toJson(result));
            out.flush();
            return;
        }

        String spinCode = req.getParameter("code");

        File blocklyAppFile = File.createTempFile("blocklyapp", ".spin");
        try (PrintWriter blocklyAppWriter = new PrintWriter(blocklyAppFile)) {
            blocklyAppWriter.print(spinCode);
            blocklyAppWriter.flush();
        }

        boolean success = false;
        switch (action) {
            case LOAD_RAM_BUF:
                // success = WebLoader.getCompiler().compile(blocklyAppFile);
                break;
            case LOAD_EEPROM_BUF:
                // success = compileAndRun(action, blocklyAppFile);
                break;
        }
        result.setSucces(success);
        result.setMessage(WebLoader.getPropellerCommunicator().getLastOutput());
        result.setCode(WebLoader.getPropellerCommunicator().getLastExitValue());

        blocklyAppFile.delete();

        out.print(gson.toJson(result));
        out.flush();

    }

}
