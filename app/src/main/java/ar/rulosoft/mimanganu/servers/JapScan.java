package ar.rulosoft.mimanganu.servers;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ar.rulosoft.mimanganu.R;
import ar.rulosoft.mimanganu.componentes.Chapter;
import ar.rulosoft.mimanganu.componentes.Manga;
import ar.rulosoft.navegadores.Navigator;

/**
 * Created by xtj-9182 on 21.02.2017.
 */
class JapScan extends ServerBase {

    private static final String HOST = "https://www.japscan.to";

    JapScan(Context context) {
        super(context);
        setFlag(R.drawable.flag_fr);
        setIcon(R.drawable.japscan);
        setServerName("JapScan");
        setServerID(JAPSCAN);
    }

    @Override
    public boolean hasList() {
        return false;
    }

    @Override
    public ArrayList<Manga> getMangas() throws Exception {
        return null;
    }

    @Override
    public ArrayList<Manga> search(String search) throws Exception {
        ArrayList<Manga> mangas = new ArrayList<>();
        Navigator nav = getNavigatorAndFlushParameters();
        nav.addPost("search", URLEncoder.encode(search, "UTF-8"));
        String source = nav.post(HOST + "/search/");
        if (source.length() > 2) {
            JSONArray jsonArray = new JSONArray(source);
            JSONObject item;
            for (int i = 0; i < jsonArray.length(); i++) {
                item = (JSONObject) jsonArray.get(i);
                mangas.add(new Manga(getServerID(), item.getString("name"), item.getString("url"), false));
            }
        }
        return mangas;

    }

    @Override
    public void loadChapters(Manga manga, boolean forceReload) throws Exception {
        loadMangaInformation(manga, forceReload);
    }

    @Override
    public void loadMangaInformation(Manga manga, boolean forceReload) throws Exception {
        if (manga.getChapters().isEmpty() || forceReload) {
            String source = getNavigatorAndFlushParameters().get(HOST + manga.getPath());

            // Cover Image
            // JapScan has no cover images ...
            manga.setImages(HOST + getFirstMatchDefault("<div class=\"m-2\">[\\s\\S]+?src=\"([^\"]+)", source, ""));

            // Summary
            manga.setSynopsis(getFirstMatchDefault("Synopsis:</div>[\\s\\S]+?<p[^>]+>([^<]+)", source, context.getString(R.string.nodisponible)));

            // Status
            manga.setFinished(getFirstMatchDefault("Statut:</span>([^<]+)", source, "").contains("Terminé"));

            // Author
            manga.setAuthor(getFirstMatchDefault("Auteur\\(s\\):</span>([^<]+)", source, context.getString(R.string.nodisponible)).trim());

            // Genres
            manga.setGenre(getFirstMatchDefault("Type\\(s\\):</span>([^<]+)", source, context.getString(R.string.nodisponible)));

            // Chapters
            Pattern pattern = Pattern.compile("<div class=\"chapters_list text-truncate\">[\\s\\S]+?href=\"([^\"]+)\">([^<]+)", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(source);
            while (matcher.find()) {
                manga.addChapterFirst(new Chapter(matcher.group(2), matcher.group(1)));
            }
        }
    }

    @Override
    public String getImageFrom(Chapter chapter, int page) throws Exception {
        String[] parts = chapter.getExtra().split("\\|");
        return parts[0] + parts[page];
    }

    @Override
    public void chapterInit(Chapter chapter) throws Exception {
        if (chapter.getPages() == 0) {
            String source = getNavigatorAndFlushParameters().get(HOST + chapter.getPath());
            String pages = getFirstMatch("Page (\\d+)</option>\\s*</select>", source,
                    context.getString(R.string.server_failed_loading_image));
            String path = getFirstMatch("data-src=\"([^\"]+)", source, context.getString(R.string.error_downloading_image));
            path = path.substring(0, path.lastIndexOf("/") + 1);
            ArrayList<String> imgs = getAllMatch("<option[^<]+?data-img=\"([^\"]+)\"", source);
            chapter.setExtra(path + "|" + TextUtils.join("|", imgs));
            chapter.setPages(Integer.parseInt(pages));
        }
    }

    private ArrayList<Manga> getMangasFromSource(String source) {
        Pattern pattern = Pattern.compile("\"img-fluid\" src=\"([^\"]+)[\\s\\S]+?<a[^>]+?href=\"([^\"]+)\">([^<]+)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(source);
        ArrayList<Manga> mangas = new ArrayList<>();
        while (matcher.find()) {
            Manga manga = new Manga(getServerID(), matcher.group(3), matcher.group(2), HOST + matcher.group(1));
            mangas.add(manga);
        }
        return mangas;
    }

    @Override
    public ArrayList<Manga> getMangasFiltered(int[][] filters, int pageNumber) throws Exception {
        String source = getNavigatorAndFlushParameters().get(HOST + "/mangas/" + pageNumber);
        return getMangasFromSource(source);
    }

    @Override
    public boolean needRefererForImages() {
        return false;
    }
}