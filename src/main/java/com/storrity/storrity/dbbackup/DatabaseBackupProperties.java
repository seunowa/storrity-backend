/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.dbbackup;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * @author Seun Owa
 */
@ConfigurationProperties(prefix = "app.database-backup")
public class DatabaseBackupProperties {

    private String directory;
    private String pgDump;

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getPgDump() {
        return pgDump;
    }

    public void setPgDump(String pgDump) {
        this.pgDump = pgDump;
    }
}