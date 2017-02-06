package me.Austin.MT.Managers.Objects;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.UUID;

/**
 * Created by mcaus_000 on 2/5/2017.
 */
public class User implements Serializable {

    public int userID;
    public UUID uuid;
    public String name;
    public InetAddress ip;


}
