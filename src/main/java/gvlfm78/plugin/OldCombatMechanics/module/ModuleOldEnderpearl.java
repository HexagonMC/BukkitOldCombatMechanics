package gvlfm78.plugin.OldCombatMechanics.module;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import gvlfm78.plugin.OldCombatMechanics.OCMMain;

/**
 * Created by ghac on 4/23/17.
 */
public class ModuleOldEnderpearl extends Module {

    public ModuleOldEnderpearl(OCMMain plugin) {
        super(plugin, "old-enderpearl");
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEnderpearlLaunched(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        World world = p.getWorld();

        if (!isEnabled(world))
            return;

        if (module().getBoolean("enabled")) {
            ItemStack it = p.getInventory().getItemInMainHand();
            if (!it.getType().equals(Material.ENDER_PEARL))
                return;

            debug("Cancelling enderpearl cooldown!", p);

            if (!p.getGameMode().equals(GameMode.CREATIVE)) {
                int amount = it.getAmount();
                if (amount < 2) {
                    p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                } else {
                    it.setAmount(amount - 1);
                    p.getInventory().setItemInMainHand(it);
                }
                p.updateInventory();
            }

            e.setCancelled(true);
            EnderPearl pearl = p.launchProjectile(EnderPearl.class);
            pearl.setVelocity(p.getEyeLocation().getDirection());
        }
    }
}
