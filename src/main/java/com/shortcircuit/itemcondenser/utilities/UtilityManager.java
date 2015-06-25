package com.shortcircuit.itemcondenser.utilities;

import com.shortcircuit.itemcondenser.ItemCondenser;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author ShortCircuit908
 */
@SuppressWarnings("deprecation")
public class UtilityManager {
	private final Set<Utility> utilities = new HashSet<>();
	private ItemCondenser plugin;

	public UtilityManager(ItemCondenser plugin) {
		this.plugin = plugin;
	}

	public void addUtility(Utility utility) {

	}

	public Utility getUtility(UUID uuid) {
		for (Utility utility : utilities) {
			if (utility.getUuid().equals(uuid)) {
				return utility;
			}
		}
		return null;
	}

	public boolean hasUtility(UUID uuid) {
		return getUtility(uuid) != null;
	}

	public Utility removeUtility(UUID uuid){
		Utility utility = getUtility(uuid);
		if(utility != null){
			utilities.remove(utility);
		}
		return utility;
	}
}