package kaysaar.bmo.buildingmenu.upgradequeue;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.combat.MutableStat;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import kaysaar.bmo.BmoListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

public class UpdateQueueMarketManager {
    ArrayList<UpdateQueueInstance>queues = new ArrayList<>();
    public void removeQueue(String id){
        for (UpdateQueueInstance queue : queues) {
            if(queue.containsItem(id)){
                queue.queue.clear();
                queues.remove(queue);
                break;
            }
        }
    }
    public void addNewQueue(Set<String> industries, MarketAPI market){
        LinkedHashMap<String,Float>map = new LinkedHashMap<>();

        for (String industry : industries) {
            BaseIndustry ind = (BaseIndustry) Global.getSettings().getIndustrySpec(industry).getNewPluginInstance(market);
            MutableStat basePrice = new MutableStat(ind.getBuildCost());
            BmoListener.modifyIndustryConstructionPrice(ind, ind.getMarket(),ind.getSpec().getDowngrade()!=null,basePrice);
            ind.setBuildCostOverride(basePrice.getModifiedValue());
            map.put(industry,ind.getBuildCost());
        }
        UpdateQueueInstance instance = new UpdateQueueInstance(map);
        queues.add(instance);
    }
    public UpdateQueueInstance getQueue(String id){
        for (UpdateQueueInstance queue : queues) {
            if(queue.containsItem(id)){
                return queue;
            }
        }
        return null;
    }


}
