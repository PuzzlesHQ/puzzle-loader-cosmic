package dev.puzzleshq.puzzleloader.cosmic.game.network.packet.cts;

import com.github.zafarkhaja.semver.Version;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import dev.puzzleshq.mod.api.IModContainer;
import dev.puzzleshq.puzzleloader.cosmic.game.network.api.IServerIdentity;
import dev.puzzleshq.puzzleloader.loader.util.ModFinder;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.networking.NetworkSide;
import finalforeach.cosmicreach.networking.server.ServerSingletons;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CTSModlistPacket extends GamePacket {

    Pair<String, String>[] modList;
    Map<String, Version> listOfMods;

    public CTSModlistPacket() {}

    public CTSModlistPacket(Pair<String, String>[] modList) {
        this.modList = modList;
    }

    @Override
    public void receive(ByteBuf in) {
        int listLength = readInt(in);

        listOfMods = new HashMap();

        for (int i = 0; i < listLength; i++) {
            String modId = readString(in);
            String modVersion = readString(in);

            listOfMods.put(modId, Version.parse(modVersion));
        }

    }

    @Override
    public void write() {
        writeInt(modList.length);
        for (Pair<String, String> modPair : modList) {
            writeString(modPair.getLeft());
            writeString(modPair.getRight());
        }
    }

    @Override
    public void handle(NetworkIdentity identity, ChannelHandlerContext ctx) {
        if (identity.getSide() == NetworkSide.SERVER) {

            List<IModContainer> missingMods = new ArrayList<>();
            Set<String> clientsMods = listOfMods.keySet();
            for (IModContainer mod : ModFinder.getModsArray()) {
                String modId = mod.getID();
                Version modVersion = mod.getVersion();
                Map<String, Boolean> allowedSides = mod.getInfo().getLoadableSides();

                if (allowedSides.get("CLIENT") && allowedSides.get("SERVER")) {
                    if (!clientsMods.contains(modId)) {
                        missingMods.add(mod);
                    } else {
                        if (!modVersion.equals(modVersion)) {
                            missingMods.add(mod);
                        }
                    }
                }
            }

            if (!missingMods.isEmpty()) {
                Account account = ServerSingletons.getAccount(identity);

                StringBuilder missingModsTxt = new StringBuilder();
                String errTxt = "These mods either don't exist or are the wrong version:";
                missingModsTxt.append(errTxt).append("\n");
                for (IModContainer mod : missingMods) {
                    StringBuilder modErrString = new StringBuilder();
                    modErrString.append(mod.getDisplayName()).append(": ").append(mod.getVersion());

                    modErrString.insert(0, "> ");
                    for (int i = 0; i < (errTxt.length() - 2) - modErrString.length(); i++) {
                        modErrString.insert(0, "-");
                    }


                    missingModsTxt.append(modErrString).append("\n");
                }

                LoggerFactory.getLogger("Server").info("Disconnecting player ID: \"{}\", Name: \"{}\" due to modlist not being the same.", account.getUniqueId(), account.getDisplayName());
                ServerSingletons.SERVER.kick(missingModsTxt.toString(), identity);
                ctx.close();
            }
            ((IServerIdentity) identity).setModList(listOfMods);
        }
    }
}