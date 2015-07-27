package com.matecat.converter.core.okapiclient;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

/**
 * Class representing the 'pack' folder generated by Okapi Framework, which includes:
 * 1. Manifest file
 * 2. Original file
 * 3. Xliff file
 */
public class OkapiPack {

    // File and directory names
    public static final String PACK_FILENAME = "pack";
    public static final String MANIFEST_FILENAME = "manifest.rkm";
    public static final String ORIGINAL_DIRECTORY_NAME = "original";
    public static final String WORK_DIRECTORY_NAME = "work";
    public static final String DONE_DIRECTORY_NAME = "done";

    // Inner files
    private File pack;
    private File xlf, manifest, originalFile;
    private File derivedFile;


    /**
     * Pack constructor
     * @param pack File wrapping the pack's folder
     */
    public OkapiPack(final File pack) {

        // Check that it is a valid pack
        if (pack == null  ||  !pack.exists()  ||  !pack.isDirectory()  ||  pack.listFiles().length == 0)
            throw new IllegalArgumentException("The given pack is not valid");

        // Save the pack and init the rest of files
        this.pack = pack;
        initFiles();

    }


    /**
     * Init the Xliff, the manifest and the original file.
     * It throws a RuntimeException if any of them is not found.
     */
    private void initFiles() {

        // Load manifest
        manifest = new File(pack.getPath() + File.separator + MANIFEST_FILENAME);
        if (!manifest.exists())
            throw new RuntimeException("The generated pack is corrupted (no manifest)");

        // Load original file
        File originalFileFolder = new File(pack.getPath() + File.separator + ORIGINAL_DIRECTORY_NAME);
        if (!originalFileFolder.exists())
            throw new RuntimeException("The generated pack is corrupted (no original folder)");
        if (originalFileFolder.listFiles().length == 0)
            throw new RuntimeException("The generated pack is corrupted (no original)");
        originalFile = originalFileFolder.listFiles()[0];
        if (!originalFile.exists())
            throw new RuntimeException("The generated pack is corrupted (no original)");

        // Load xlf
        xlf = new File(pack.getPath() + File.separator + WORK_DIRECTORY_NAME + File.separator + originalFile.getName() + ".xlf");
        if (!xlf.exists())
            throw new RuntimeException("The generated pack is corrupted (no xliff)");

    }


    /**
     * Get pack folder
     * @return File wrapping the folder of the pack
     */
    public File getPackFolder() {
        if (pack == null)
            throw new IllegalStateException("The pack has been deleted");
        return pack;
    }


    /**
     * Get Xlf
     * @return Get the xliff file contained inside the pack
     */
    public File getXlf() {
        if (xlf == null)
            throw new IllegalStateException("The pack has been deleted");
        return xlf;
    }


    /**
     * Get manifest
     * @return Get the manifest file contained inside the pack
     */
    public File getManifest() {
        if (manifest == null)
            throw new IllegalStateException("The pack has been deleted");
        return manifest;
    }


    /**
     * Get original file
     * @return Get the original file contained inside the pack
     */
    public File getOriginalFile() {
        if (originalFile == null)
            throw new IllegalStateException("The pack has been deleted");
        return originalFile;
    }


    /**
     * Delete the pack, with all the files, assigning null to the variables
     */
    public void delete() {
        try {
            FileUtils.deleteDirectory(getPackFolder());
            xlf = null;
            originalFile = null;
            manifest = null;
            pack = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Get the derived file
     * If it has not been computed yet, an Runtime exception will be thrown
     * @return Derived file
     */
    public File getDerivedFile() {
        // Check that the pack has not been deleted
        if (originalFile == null)
            throw new IllegalStateException("The pack has been deleted");
        // Lazy generation
        if (derivedFile == null)
            derivedFile = new File(pack.getPath() + File.separator + DONE_DIRECTORY_NAME + File.separator + originalFile.getName());
        // Check that it has been retrieved properly and return it
        if (!derivedFile.exists())
            throw new RuntimeException("The pack has not derived file to obtain (process it with Okapi first)");
        return derivedFile;
    }

}