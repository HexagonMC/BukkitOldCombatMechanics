package gvlfm78.plugin.OldCombatMechanics.module;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.Vector;

import gvlfm78.plugin.OldCombatMechanics.OCMMain;

/**
 * Created by ghac on 4/23/17.
 */
public class ModuleOldEnderpearl extends Module {

    public ModuleOldEnderpearl(OCMMain plugin) {
        super(plugin, "old-enderpearl");
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEnderpearlLaunched(ProjectileLaunchEvent e) {
        if (!(e.getEntity() instanceof EnderPearl))
            return;
        
        if (!(e.getEntity().getShooter() instanceof Player))
            return;

        World world = e.getEntity().getWorld();

        if (!isEnabled(world))
            return;
        
        if (module().getBoolean("enabled")) {
            Player p = (Player) (e.getEntity().getShooter());
            p.setCooldown(Material.ENDER_PEARL, 0);
            
            // e.setCancelled(true);
            
            
            // EnderPearl ep = p.getWorld().spawn(p.getLocation(), EnderPearl.class);
            EnderPearl ep = (EnderPearl) e.getEntity();
            ep.setShooter(p);
            double posX = p.getLocation().getX();
            double posY = p.getLocation().getY() + p.getEyeHeight();
            double posZ = p.getLocation().getZ();
            float yaw = p.getLocation().getYaw();
            float pitch = p.getLocation().getPitch();
            ep.teleport(new Location(ep.getWorld(), posX, posY, posZ));
            ep.getLocation().setYaw(yaw);
            ep.getLocation().setPitch(pitch);
            
            posX -= (double) (Math.cos(yaw / 180.0F * (float) Math.PI) * 0.16F);
            posY -= 0.10000000149011612D;
            posZ -= (double) (Math.sin(yaw / 180.0F * (float) Math.PI) * 0.16F);
            ep.teleport(new Location(ep.getWorld(), posX, posY, posZ));
            
            float factor = 0.4F;
            double motX = (double) (-Math.sin(yaw / 180.0F * (float) Math.PI) * Math.cos(pitch / 180.0F * (float) Math.PI) * factor);
            double motY = (double) (-Math.sin((pitch) / 180.0F * (float) Math.PI) * factor);
            double motZ = (double) (Math.cos(yaw / 180.0F * (float) Math.PI) * Math.cos(pitch / 180.0F * (float) Math.PI) * factor);
            
            double motionLength = Math.sqrt(motX * motX + motY * motY + motZ * motZ);
            motX = motX / motionLength;
            motY = motY / motionLength;
            motZ = motZ / motionLength;
            
            Random rand = new Random();
            motX = motX + rand.nextGaussian() * 0.007499999832361937D;
            motY = motY + rand.nextGaussian() * 0.007499999832361937D;
            motZ = motZ + rand.nextGaussian() * 0.007499999832361937D;
            
            motX = motX * 1.5;
            motY = motY * 1.5;
            motZ = motZ * 1.5;

            ep.setVelocity(new Vector(motX, motY, motZ));
            yaw = (float) (func_181159_b(motX, motZ) * 180.0D / Math.PI);
            
            double fact = Math.sqrt(motX * motX + motZ * motZ);
            pitch = (float) (func_181159_b(motY, fact) * 180.0D / Math.PI);
            
            ep.getLocation().setYaw(yaw);
            ep.getLocation().setPitch(pitch);
            
            p.setCooldown(Material.ENDER_PEARL, 0);
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                
                @Override
                public void run() {
                    p.setCooldown(Material.ENDER_PEARL, 0);
                }
            }, 1);
        }
    }
    
    public static double func_181159_b(double p_181159_0_, double p_181159_2_)
    {
        double d0 = p_181159_2_ * p_181159_2_ + p_181159_0_ * p_181159_0_;
        double field_181163_d = Double.longBitsToDouble(4805340802404319232L);
        double[] field_181164_e = new double[257];
        double[] field_181165_f = new double[257];
        
        for (int j = 0; j < 257; ++j)
        {
            double dd0 = (double)j / 256.0D;
            double dd1 = Math.asin(dd0);
            field_181165_f[j] = Math.cos(dd1);
            field_181164_e[j] = dd1;
        }

        if (Double.isNaN(d0))
        {
            return Double.NaN;
        }
        else
        {
            boolean flag = p_181159_0_ < 0.0D;

            if (flag)
            {
                p_181159_0_ = -p_181159_0_;
            }

            boolean flag1 = p_181159_2_ < 0.0D;

            if (flag1)
            {
                p_181159_2_ = -p_181159_2_;
            }

            boolean flag2 = p_181159_0_ > p_181159_2_;

            if (flag2)
            {
                double d1 = p_181159_2_;
                p_181159_2_ = p_181159_0_;
                p_181159_0_ = d1;
            }

            double d9 = func_181161_i(d0);
            p_181159_2_ = p_181159_2_ * d9;
            p_181159_0_ = p_181159_0_ * d9;
            double d2 = field_181163_d + p_181159_0_;
            int i = (int)Double.doubleToRawLongBits(d2);
            double d3 = field_181164_e[i];
            double d4 = field_181165_f[i];
            double d5 = d2 - field_181163_d;
            double d6 = p_181159_0_ * d4 - p_181159_2_ * d5;
            double d7 = (6.0D + d6 * d6) * d6 * 0.16666666666666666D;
            double d8 = d3 + d7;

            if (flag2)
            {
                d8 = (Math.PI / 2D) - d8;
            }

            if (flag1)
            {
                d8 = Math.PI - d8;
            }

            if (flag)
            {
                d8 = -d8;
            }

            return d8;
        }
    }
    
    public static double func_181161_i(double p_181161_0_)
    {
        double d0 = 0.5D * p_181161_0_;
        long i = Double.doubleToRawLongBits(p_181161_0_);
        i = 6910469410427058090L - (i >> 1);
        p_181161_0_ = Double.longBitsToDouble(i);
        p_181161_0_ = p_181161_0_ * (1.5D - d0 * p_181161_0_ * p_181161_0_);
        return p_181161_0_;
    }
}
