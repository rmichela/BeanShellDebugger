package com.ryanmichela.bshd;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BshdCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		// build the code string
		StringBuilder sb = new StringBuilder();
		for(String a : args) {
			sb.append(a);
			sb.append(" ");
		}
		
		try {
			Socket s = new Socket("localhost", 1338);
			s.setSoTimeout(10);
			BufferedReader in = new BufferedReader
			  (new InputStreamReader(s.getInputStream()));
			PrintWriter out = new PrintWriter
			  (s.getOutputStream(), true /* autoFlush */);
			
			out.println(sb.toString());
			
			boolean more = true;
			boolean first = true;
			try {
				while (more) {
				    String line = in.readLine();
				    if (line == null)
				    	more = false;
				    else if (first)
				    	first = false;
				    else
				    	sender.sendMessage(line);
				 }
			} catch (SocketTimeoutException e) {
				
			}
			
			s.close();
			in.close();
			out.close();
			
		} catch (Exception e) {
			sender.sendMessage("Error: " + e.getMessage());
		}
		
		return true;
	}

}
