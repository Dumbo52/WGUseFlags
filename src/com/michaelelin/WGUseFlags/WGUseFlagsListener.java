package com.michaelelin.WGUseFlags;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;

public class WGUseFlagsListener implements Listener {

    private WGUseFlagsPlugin plugin;
    private WorldGuardPlugin worldguard;

    public WGUseFlagsListener(WGUseFlagsPlugin plugin, WorldGuardPlugin worldguard) {
        this.plugin = plugin;
        this.worldguard = worldguard;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            Location location = event.getClickedBlock().getLocation();
            LocalPlayer player = worldguard.wrapPlayer(event.getPlayer());
    
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                switch (event.getClickedBlock().getType()) {
                case DISPENSER:
                    cancelEvent(event, !allows(plugin.USE_DISPENSER, location, player), true);
                    break;
                case NOTE_BLOCK:
                    cancelEvent(event, !allows(plugin.USE_NOTE_BLOCK, location, player), true);
                    break;
                case WORKBENCH:
                    cancelEvent(event, !allows(plugin.USE_WORKBENCH, location, player), true);
                    break;
                case WOODEN_DOOR:
                    cancelEvent(event, !allows(plugin.USE_DOOR, location, player), true);
                    break;
                case LEVER:
                    cancelEvent(event, !allows(plugin.USE_LEVER, location, player), true);
                    break;
                case STONE_BUTTON:
                case WOOD_BUTTON:
                    cancelEvent(event, !allows(plugin.USE_BUTTON, location, player), true);
                    break;
                case JUKEBOX:
                    cancelEvent(event, !allows(plugin.USE_JUKEBOX, location, player), true);
                    break;
                case DIODE_BLOCK_OFF:
                case DIODE_BLOCK_ON:
                    cancelEvent(event, !allows(plugin.USE_REPEATER, location, player), true, true);
                    break;
                case TRAP_DOOR:
                    cancelEvent(event, !allows(plugin.USE_TRAP_DOOR, location, player), true);
                    break;
                case FENCE_GATE:
                    cancelEvent(event, !allows(plugin.USE_FENCE_GATE, location, player), true);
                    break;
                case BREWING_STAND:
                    cancelEvent(event, !allows(plugin.USE_BREWING_STAND, location, player), true);
                    break;
                case CAULDRON:
                    cancelEvent(event, !allows(plugin.USE_CAULDRON, location, player), true);
                    break;
                case ENCHANTMENT_TABLE:
                    cancelEvent(event, !allows(plugin.USE_ENCHANTMENT_TABLE, location, player), true);
                    break;
                case ENDER_CHEST:
                    cancelEvent(event, !allows(plugin.USE_ENDER_CHEST, location, player), true);
                    break;
                case BEACON:
                    cancelEvent(event, !allows(plugin.USE_BEACON, location, player), true);
                    break;
                case ANVIL:
                    cancelEvent(event, !allows(plugin.USE_ANVIL, location, player), true);
                    break;
                case REDSTONE_COMPARATOR_OFF:
                case REDSTONE_COMPARATOR_ON:
                    cancelEvent(event, !allows(plugin.USE_COMPARATOR, location, player), true, true);
                    break;
                case HOPPER:
                    cancelEvent(event, !allows(plugin.USE_HOPPER, location, player), true);
                    break;
                case DROPPER:
                    cancelEvent(event, !allows(plugin.USE_DROPPER, location, player), true);
                    break;
                default:
                }
            }
            if (event.getAction() == Action.PHYSICAL) {
                Material mat = event.getClickedBlock().getType();
                if (mat == Material.STONE_PLATE || mat == Material.WOOD_PLATE || mat == Material.GOLD_PLATE || mat == Material.IRON_PLATE) {
                    cancelEvent(event, !allows(plugin.USE_PRESSURE_PLATE, location, player), false);
                }
                else if (mat == Material.TRIPWIRE) {
                    cancelEvent(event, !allows(plugin.USE_TRIPWIRE, location, player), false);
                }
            }
        }
    }

    private boolean allows(StateFlag flag, Location location, LocalPlayer player) {
        ApplicableRegionSet set = worldguard.getGlobalRegionManager().get(location.getWorld()).getApplicableRegions(location);
        return worldguard.getGlobalRegionManager().hasBypass(player, location.getWorld()) || set.canBuild(player) || set.allows(flag, player);
    }

    private void cancelEvent(PlayerInteractEvent e, boolean cancel, boolean notifyPlayer) {
        cancelEvent(e, cancel, notifyPlayer, false);
    }

    // Override for repeaters and comparators, since WG cancels these without
    // any checks.
    private void cancelEvent(PlayerInteractEvent e, boolean cancel, boolean notifyPlayer, boolean override) {
        e.setCancelled(cancel);
        if (e.isCancelled() && notifyPlayer) {
            e.getPlayer().sendMessage(ChatColor.DARK_RED + "You don't have permission to use that in this area.");
        }
    }
}
