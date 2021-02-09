package com.iridium.iridiumskyblock.database.orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@NoArgsConstructor
@DatabaseTable(tableName = "islandwarps")
public class IslandWarp {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    @NotNull
    private Integer id;

    @DatabaseField(columnName = "island_id", foreign = true, foreignAutoRefresh = true)
    @NotNull
    private Island island;

    @DatabaseField(columnName = "location")
    @NotNull
    private String location;

    @DatabaseField(columnName = "warp_name")
    @NotNull
    private String name;

    @DatabaseField(columnName = "password")
    @NotNull
    private String password;

    public Location getLocation() {
        String[] params = location.split(",");
        return new Location(Bukkit.getWorld(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]), Double.parseDouble(params[3]), Float.parseFloat(params[4]), Float.parseFloat(params[5]));
    }

    public void setLocation(@NotNull Location location) {
        this.location = location.getWorld() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getPitch() + "," + location.getYaw();
    }

    public IslandWarp(@NotNull Island island, @NotNull Location location, @NotNull String name, @NotNull String password) {
        this.island = island;
        setLocation(location);
        this.name = name;
        this.password = password;
    }

}
