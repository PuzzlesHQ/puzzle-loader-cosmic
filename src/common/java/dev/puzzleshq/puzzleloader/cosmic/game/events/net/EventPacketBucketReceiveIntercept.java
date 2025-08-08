package dev.puzzleshq.puzzleloader.cosmic.game.events.net;

import com.badlogic.gdx.utils.Array;
import finalforeach.cosmicreach.networking.GamePacket;
import net.neoforged.bus.api.Event;

public class EventPacketBucketReceiveIntercept extends Event {

    private Array<GamePacket> bucket;

    public void setPacketBucket(Array<GamePacket> bucket) {
        this.bucket = bucket;
    }

    public Array<GamePacket> getPacketBucket() {
        return bucket;
    }

}