package sorcerycraft.abilities;

import sorcerycraft.abilities.Ability;
import sorcerycraft.main.SorceryCraft;

import java.util.ArrayList;
import java.util.Iterator;

public class AbilityUpdater {

    /**
     * The current instance of the running plugin.
     */
    private SorceryCraft sc = SorceryCraft.getPlugin(SorceryCraft.class);

    /**
     *
     */
    public static ArrayList<Ability> abilities = new ArrayList<>();

    /**
     *
     */
    public void startUpdater(){
        sc.getServer().getScheduler().runTaskLater(sc, () -> {

            //Update all ongoing abilities
            updateAll();

            //Loop method
            startUpdater();
        }, 1l);
    }

    /**
     *
     */
    public void updateAll(){

        Iterator<Ability> iterator = abilities.iterator();

        while(iterator.hasNext()){
            Ability ability = iterator.next();
            if(!ability.isScheduledForRemoved){
                ability.update();
            }
            else{
                iterator.remove();
            }
        }
    }
}
