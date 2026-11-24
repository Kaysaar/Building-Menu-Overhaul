package kaysaar.bmo.buildingmenu.upgradequeue;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import kaysaar.bmo.BmoListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

public class UpdateQueueMarketManager {
    ArrayList<UpdateQueueInstance>queues = new ArrayList<>();
    private LinkedHashMap<String, Integer> paidBuildPrices;

    public static UpdateQueueMarketManager getOrCreate(MarketAPI market) {
        Object current = market.getMemoryWithoutUpdate().get(UpdateQueueMainManager.memKey);
        if (current instanceof UpdateQueueMarketManager manager) return manager;
        UpdateQueueMarketManager manager = new UpdateQueueMarketManager();
        market.getMemoryWithoutUpdate().set(UpdateQueueMainManager.memKey, manager);
        return manager;
    }

    public void enqueueBuild(MarketAPI market, String industryId, int cost) {
        market.getConstructionQueue().addToEnd(industryId, cost);
        if (paidBuildPrices == null) paidBuildPrices = new LinkedHashMap<>();
        paidBuildPrices.put(industryId, cost);
    }

    public void applyPaidBuildPrices(MarketAPI market) {
        if (paidBuildPrices == null || paidBuildPrices.isEmpty()) return;
        paidBuildPrices.entrySet().removeIf(entry -> {
            Industry industry = market.getIndustry(entry.getKey());
            if (industry instanceof BaseIndustry base) {
                base.setBuildCostOverride(entry.getValue());
                return true;
            }
            return !market.getConstructionQueue().hasItem(entry.getKey());
        });
    }

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
        addNewQueue(getStagePrices(industries, market));
    }

    public static LinkedHashMap<String, Float> getStagePrices(Set<String> industries, MarketAPI market) {
        LinkedHashMap<String, Float> prices = new LinkedHashMap<>();
        boolean upgrade = false;
        for (String id : industries) {
            Industry industry = Global.getSettings().getIndustrySpec(id).getNewPluginInstance(market);
            prices.put(id, (float) BmoListener.getIndustryConstructionPrice(industry, upgrade));
            upgrade = true;
        }
        return prices;
    }

    public void addNewQueue(LinkedHashMap<String, Float> prices) {
        queues.add(new UpdateQueueInstance(new LinkedHashMap<>(prices)));
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
