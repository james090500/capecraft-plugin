package net.capecraft.joinleave;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class MessageListener implements PluginMessageListener {

	Plugin instance;
	
	public MessageListener(Plugin instance) {
		this.instance = instance;
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
	    
	    ByteArrayDataInput in = ByteStreams.newDataInput(message);	     
	      
	    String subchannel = in.readUTF();
	    if (subchannel.equals("CapeCraftJL")) {
    		String JLMessage = in.readUTF();
    		instance.getLogger().info(JLMessage);
	    	for(Player p : instance.getServer().getOnlinePlayers()) {	    		
	    		p.sendMessage(ChatColor.YELLOW + JLMessage);
	    	}
	    }	    	   
	}

}
