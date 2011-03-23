//Copyright (C) 2011  Ryan Michela
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.

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
			int ln = 0;
			try {
				while (more) {
				    String line = in.readLine();
				    if (line == null)
				    	more = false;
				    else if (ln == 0) {
				    	ln++;
					} else if (ln == 1) {
				    	sender.sendMessage(line.substring(6));
				    	ln++;
				    } else {
				    	sender.sendMessage(line);
				    }
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
