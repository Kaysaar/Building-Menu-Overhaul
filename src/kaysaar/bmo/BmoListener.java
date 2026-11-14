package kaysaar.bmo;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.combat.MutableStat;
import kaysaar.bmo.listeners.BuildingMenuListener;

import java.util.HashSet;

public class BmoListener {
    public static HashSet<String> aboutToCreateBuildingMenu(MarketAPI market){
        HashSet<String> aboutToCreateBuildingMenu = new HashSet<>();
        Global.getSector().getListenerManager().getListeners(BuildingMenuListener.class).forEach(x->{
            aboutToCreateBuildingMenu.addAll(x.addBuildingsToBeHidden(market));
        });
        return aboutToCreateBuildingMenu;
    }
    public static void modifyIndustryConstructionPrice(Industry indInstance, MarketAPI market, boolean isForUpgrade, MutableStat price){
        Global.getSector().getListenerManager().getListeners(BuildingMenuListener.class).forEach(x->{
            x.modifyIndustryConstructionPrice(indInstance,market,isForUpgrade,price);
        });
    }

}

