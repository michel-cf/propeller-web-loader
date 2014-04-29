/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.creatingfuture.propeller.webLoader.propeller;

import java.io.File;

/**
 *
 * @author Michel
 */
public class MacOpenSpin extends OpenSpin {

    @Override
    public boolean compile(File sourceFile) {
        return this.compile("propeller-tools/mac/openspin", sourceFile);
    }

    @Override
    public boolean compileForRam(File sourceFile, File destinationFile) {
        return this.compileForRam("propeller-tools/mac/openspin", sourceFile, destinationFile);
    }

    @Override
    public boolean compileForEeprom(File sourceFile, File destinationFile) {
        return this.compileForEeprom("propeller-tools/mac/openspin", sourceFile, destinationFile);
    }

}
