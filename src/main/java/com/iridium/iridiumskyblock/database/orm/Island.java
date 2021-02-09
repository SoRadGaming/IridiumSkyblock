package com.iridium.iridiumskyblock.database.orm;

import com.cryptomorin.xseries.XBiome;
import com.iridium.iridiumskyblock.Color;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Role;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

// TODO: Finish this table, persist permissions properly
// also make sure this actually works as intended

/**
 * ORM class representing an island in the database
 *
 * @author BomBardyGamer
 * @since 3.0
 */
@Getter
@Setter
@NoArgsConstructor
@DatabaseTable(tableName = "islands")
public final class Island {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    @NotNull
    private Integer id;

    @DatabaseField(columnName = "name", canBeNull = false)
    @NotNull
    private String name;

    @ForeignCollectionField(columnName = "id", foreignFieldName = "island")
    @Setter(AccessLevel.PRIVATE)
    private ForeignCollection<User> members;

    @ForeignCollectionField(columnName = "id", foreignFieldName = "island")
    @Setter(AccessLevel.PRIVATE)
    private ForeignCollection<IslandWarp> islandWarps;

    @DatabaseField(columnName = "biome", canBeNull = false)
    @NotNull
    private XBiome biome;

    @DatabaseField(columnName = "nether_biome", canBeNull = false)
    @NotNull
    private XBiome netherBiome;

    @DatabaseField(columnName = "schematic", canBeNull = false)
    @NotNull
    private String schematic;

    @DatabaseField(columnName = "nether_schematic", canBeNull = false)
    @NotNull
    private String netherSchematic;

    @DatabaseField(columnName = "last_regen")
    @NotNull
    private Long lastRegenTime;

    @DatabaseField(columnName = "visit")
    private boolean visit;

    @DatabaseField(columnName = "borderColor")
    @NotNull
    private Color borderColor;

    public void setLastRegenTime(LocalDateTime localDateTime) {
        this.lastRegenTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public LocalDateTime getLastRegenTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(lastRegenTime), ZoneId.systemDefault());
    }

    public User getOwner() {
        return members.stream().filter(user -> user.getRole() == Role.Owner).findFirst().orElse(null);
    }

    //Function based off: https://stackoverflow.com/a/19287714
    public Location getCenter(World world) {
        if (id == 1) return new Location(world, 0, 0, 0);
        // In this algorithm id  0 will be where we want id 2 to be and 1 will be where 3 is ect
        int n = id - 2;

        int r = (int) (Math.floor((Math.sqrt(n + 1) - 1) / 2) + 1);
        // compute radius : inverse arithmetic sum of 8+16+24+...=

        int p = (8 * r * (r - 1)) / 2;
        // compute total point on radius -1 : arithmetic sum of 8+16+24+...

        int en = r * 2;
        // points by face

        int a = (1 + n - p) % (r * 8);
        // compute de position and shift it so the first is (-r,-r) but (-r+1,-r)
        // so square can connect

        Location location;

        switch (a / (r * 2)) {
            case 0:
                location = new Location(world, (a - r), 0, -r);
                break;
            case 1:
                location = new Location(world, r, 0, (a % en) - r);
                break;
            case 2:
                location = new Location(world, r - (a % en), 0, r);
                break;
            case 3:
                location = new Location(world, -r, 0, r - (a % en));
                break;
            default:
                throw new IllegalStateException("Could not find island location with ID: " + id);
        }

        return location.multiply(IridiumSkyblock.getInstance().getConfiguration().distance);
    }

    public Island(@NotNull String name, Schematics.FakeSchematic fakeSchematic) {
        this.name = name;
        this.biome = fakeSchematic.overworldData.biome;
        this.netherBiome = fakeSchematic.netherData.biome;
        this.schematic = fakeSchematic.overworldData.schematic;
        this.netherSchematic = fakeSchematic.netherData.schematic;
        this.lastRegenTime = 0L;
        this.visit = true;
        this.borderColor = Color.BLUE;
    }
}
