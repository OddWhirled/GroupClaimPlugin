/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddwhirled.groupclaimplugin.storage;

import org.bukkit.Chunk;

/**
 *
 * @author Drew
 */
public class SQLQueryRunnable implements Runnable {
    
    Chunk c;

    public SQLQueryRunnable(Chunk c) {
        this.c = c;
    }

    @Override
    public void run() {

    }

}
