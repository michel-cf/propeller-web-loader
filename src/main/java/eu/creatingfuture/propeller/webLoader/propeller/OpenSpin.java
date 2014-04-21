/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.creatingfuture.propeller.webLoader.propeller;

import eu.creatingfuture.propeller.webLoader.interfaces.Compiler;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;

/**
 *
 * @author Michel
 */
public abstract class OpenSpin implements Compiler {

    private static final Logger logger = Logger.getLogger(OpenSpin.class.getName());

    protected int exitValue;
    protected String output;

    /**
     * https://code.google.com/p/open-source-spin-compiler/wiki/CommandLine
     *
     * @param executable
     * @param sourceFile
     * @return
     */
    protected boolean compile(String executable, File sourceFile) {
        try {
            File temporaryDestinationFile = File.createTempFile("blocklyapp", ".binary");
            Map map = new HashMap();
            map.put("sourceFile", sourceFile);
            map.put("destinationFile", temporaryDestinationFile);

            CommandLine cmdLine = new CommandLine(executable);
            cmdLine.addArgument("-o").addArgument("${destinationFile}");
            cmdLine.addArgument("${sourceFile}");
            cmdLine.setSubstitutionMap(map);
            DefaultExecutor executor = new DefaultExecutor();
            //  executor.setExitValues(new int[]{402, 101});

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
            executor.setStreamHandler(streamHandler);

            try {
                exitValue = executor.execute(cmdLine);
            } catch (ExecuteException ee) {
                exitValue = ee.getExitValue();
                logger.log(Level.SEVERE, "Unexpected exit value: {0}", exitValue);
                return false;
            } finally {
                temporaryDestinationFile.delete();
                output = outputStream.toString();
            }

//            System.out.println("output: " + output);
            /*
             Scanner scanner = new Scanner(output);


             Pattern chipFoundPattern = Pattern.compile(".*?(EVT:505).*?");
             Pattern pattern = Pattern.compile(".*?found on (?<comport>[a-zA-Z0-9]*).$");
             while (scanner.hasNextLine()) {
             String portLine = scanner.nextLine();
             if (chipFoundPattern.matcher(portLine).matches()) {
             Matcher portMatch = pattern.matcher(portLine);
             if (portMatch.find()) {
             //   String port = portMatch.group("comport");

             }
             }
             }
             */
//            System.out.println("output: " + output);
//            System.out.println("exitValue: " + exitValue);
            return true;
        } catch (IOException ioe) {
            logger.log(Level.SEVERE, null, ioe);
            return false;
        }
    }

    protected boolean compileForRam(String executable, File sourceFile, File destinationFile) {
        try {
            Map map = new HashMap();
            map.put("sourceFile", sourceFile);
            map.put("destinationFile", destinationFile);

            CommandLine cmdLine = new CommandLine(executable);
            cmdLine.addArgument("-b");
            cmdLine.addArgument("-o").addArgument("${destinationFile}");
            cmdLine.addArgument("${sourceFile}");
            cmdLine.setSubstitutionMap(map);
            DefaultExecutor executor = new DefaultExecutor();
            //  executor.setExitValues(new int[]{402, 101});

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
            executor.setStreamHandler(streamHandler);

            try {
                exitValue = executor.execute(cmdLine);
            } catch (ExecuteException ee) {
                exitValue = ee.getExitValue();
                logger.log(Level.SEVERE, "Unexpected exit value: {0}", exitValue);
                return false;
            } finally {
                output = outputStream.toString();
            }

//            System.out.println("output: " + output);
            /*
             Scanner scanner = new Scanner(output);


             Pattern chipFoundPattern = Pattern.compile(".*?(EVT:505).*?");
             Pattern pattern = Pattern.compile(".*?found on (?<comport>[a-zA-Z0-9]*).$");
             while (scanner.hasNextLine()) {
             String portLine = scanner.nextLine();
             if (chipFoundPattern.matcher(portLine).matches()) {
             Matcher portMatch = pattern.matcher(portLine);
             if (portMatch.find()) {
             //   String port = portMatch.group("comport");

             }
             }
             }
             */
//            System.out.println("output: " + output);
//            System.out.println("exitValue: " + exitValue);
            return true;
        } catch (IOException ioe) {
            logger.log(Level.SEVERE, null, ioe);
            return false;
        }
    }

    protected boolean compileForEeprom(String executable, File sourceFile, File destinationFile) {
        try {
            Map map = new HashMap();
            map.put("sourceFile", sourceFile);
            map.put("destinationFile", destinationFile);

            CommandLine cmdLine = new CommandLine(executable);
            cmdLine.addArgument("-e");
            cmdLine.addArgument("-o").addArgument("${destinationFile}");
            cmdLine.addArgument("${sourceFile}");
            cmdLine.setSubstitutionMap(map);
            DefaultExecutor executor = new DefaultExecutor();
            //  executor.setExitValues(new int[]{402, 101});

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
            executor.setStreamHandler(streamHandler);

            try {
                exitValue = executor.execute(cmdLine);
            } catch (ExecuteException ee) {
                exitValue = ee.getExitValue();
                logger.log(Level.SEVERE, "Unexpected exit value: {0}", exitValue);
                return false;
            } finally {
                output = outputStream.toString();
            }

//            System.out.println("output: " + output);
            /*
             Scanner scanner = new Scanner(output);


             Pattern chipFoundPattern = Pattern.compile(".*?(EVT:505).*?");
             Pattern pattern = Pattern.compile(".*?found on (?<comport>[a-zA-Z0-9]*).$");
             while (scanner.hasNextLine()) {
             String portLine = scanner.nextLine();
             if (chipFoundPattern.matcher(portLine).matches()) {
             Matcher portMatch = pattern.matcher(portLine);
             if (portMatch.find()) {
             //   String port = portMatch.group("comport");

             }
             }
             }
             */
//            System.out.println("output: " + output);
//            System.out.println("exitValue: " + exitValue);
            return true;
        } catch (IOException ioe) {
            logger.log(Level.SEVERE, null, ioe);
            return false;
        }
    }

    @Override
    public String getLastOutput() {
        return output;
    }

    @Override
    public int getLastExitValue() {
        return exitValue;
    }

}
