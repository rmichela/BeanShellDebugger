package com.ryanmichela.bshd;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import bsh.Interpreter;

public class BeanShellDebugger extends JavaPlugin {

	private Interpreter bsh;
	private Logger log;
	
	@Override
	public void onDisable() {}

	@Override
	public void onEnable() {
		log = getServer().getLogger();
		log.info("[bshd] Starting BeanShell Debugger");
		
		bsh = new Interpreter();
		try {
			bsh.set("portnum", 1337);
			
			// Set up debug environment with globals
			bsh.set("pluginLoader", getPluginLoader());
			bsh.set("pluginManager", getServer().getPluginManager());
			bsh.set("server", getServer());
			bsh.set("classLoader", getClassLoader());
			
			// Create an alias for each plugin name using its class name
			for(Plugin p : getServer().getPluginManager().getPlugins()) {
				String[] cn = p.getClass().getName().split("\\.");
				log.info("[bshd] Regisering object " + cn[cn.length-1]);
				bsh.set(cn[cn.length-1], p);
			}
			
			// Source any .bsh files in the plugin directory
			if(getDataFolder().listFiles() != null) {
				for(File f : getDataFolder().listFiles()) {
					if(f.getName().endsWith(".bsh")) {
						log.info("[bshd] Sourcing file " + f.getName());
						bsh.source(f.getPath());
					}
					else {
						log.info("*** skipping " + f.getAbsolutePath());
					}
				}
			}
			
			bsh.eval("setAccessibility(true)"); // turn off access restrictions
			bsh.eval("server(portnum)");
			log.info("[bshd] BeanShell web console at http://localhost:1337");
			log.info("[bshd] BeanShell telnet console at localhost:1338");
			
			// Register the bshd command
			getCommand("bshd").setExecutor(new BshdCommand());
			
		} catch (Exception e) {
			log.severe("[bshd] Error in BeanShell. " + e.toString());
		}
		
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		
	}

}
