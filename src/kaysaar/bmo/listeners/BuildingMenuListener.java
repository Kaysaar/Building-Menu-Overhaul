package kaysaar.bmo.listeners;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.combat.MutableStat;

import java.util.HashSet;

public interface BuildingMenuListener {
    public HashSet<String>addBuildingsToBeHidden(MarketAPI market);
    default void modifyIndustryConstructionPrice(Industry indInstance, MarketAPI market, boolean isForUpgrade, MutableStat price) {

    }
}
