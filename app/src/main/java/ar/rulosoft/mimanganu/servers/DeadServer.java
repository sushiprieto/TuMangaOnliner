package ar.rulosoft.mimanganu.servers;

import android.content.Context;
import android.util.SparseArray;

import java.util.ArrayList;

import ar.rulosoft.mimanganu.R;
import ar.rulosoft.mimanganu.componentes.Chapter;
import ar.rulosoft.mimanganu.componentes.Manga;

/**
 * Created by Raul on 28/04/2017.
 */

public class DeadServer extends ServerBase {
    public DeadServer(Context context) {
        super(context);
        setFlag(R.drawable.rip);
        setIcon(R.drawable.rip);
        setServerName("DEAD SERVER");
    }

    public static String getServerName(Manga m) {
        // before removing deprecated Servers add the correct id/name here
        SparseArray<String> deadServers = new SparseArray<>();
        deadServers.put(TUSMANGAS, "TusMangasOnline");

        return deadServers.get(m.getServerId());
    }

    @Override
    public ArrayList<Manga> getMangas() throws Exception {
        throw new Exception(context.getString(R.string.server_dead_message));
    }

    @Override
    public ArrayList<Manga> search(String term) throws Exception {
        throw new Exception(context.getString(R.string.server_dead_message));
    }

    @Override
    public void loadChapters(Manga manga, boolean forceReload) throws Exception {
        throw new Exception(context.getString(R.string.server_dead_message));
    }

    @Override
    public void loadMangaInformation(Manga manga, boolean forceReload) throws Exception {
        throw new Exception(context.getString(R.string.server_dead_message));
    }

    @Override
    public String getImageFrom(Chapter chapter, int page) throws Exception {
        throw new Exception(context.getString(R.string.server_dead_message));
    }

    @Override
    public void chapterInit(Chapter chapter) throws Exception {
        throw new Exception(context.getString(R.string.server_dead_message));
    }

    @Override
    public int searchForNewChapters(int id, Context context, boolean fast) {
        return 0; //just return 0 found and don't throw exceptions
    }

    @Override
    public boolean hasList() {
        return false;
    }
}
