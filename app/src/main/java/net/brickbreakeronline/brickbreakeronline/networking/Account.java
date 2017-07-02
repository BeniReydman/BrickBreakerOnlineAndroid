package net.brickbreakeronline.brickbreakeronline.networking;

/**
 * Created by Kozak on 2017-07-01.
 */

public class Account {

    public long id = 0;
    public String key = null;
    public String name = null;

    public Account(long id, String key, String name) {
        this.id = id;
        this.key = key;
        this.name = name;
    }

    public Account() {}
}
