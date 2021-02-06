package com.iridium.iridiumskyblock.database.orm;

import com.cryptomorin.xseries.XBiome;
import com.iridium.iridiumskyblock.Direction;
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

    @DatabaseField(columnName = "id", generatedId = true)
    @NotNull
    private Integer id;

    @DatabaseField(columnName = "name", canBeNull = false)
    @NotNull
    private String name;

    @ForeignCollectionField(columnName = "id", foreignFieldName = "island")
    @Setter(AccessLevel.PRIVATE)
    private ForeignCollection<User> members;

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

    public void setLastRegenTime(LocalDateTime localDateTime) {
        this.lastRegenTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public LocalDateTime getLastRegenTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(lastRegenTime), ZoneId.systemDefault());
    }

    public User getOwner() {
        return members.stream().filter(user -> user.getRole() == Role.Owner).findFirst().orElse(null);
    }

    public Location getCenter(World world) {
        //TODO improve and optimise this
        int x = 0;
        int z = 0;

        int length = 1;
        int current = 0;
        Direction direction = Direction.NORTH;

        for (int i = 1; i < id; i++) {

            switch (direction) {
                case NORTH:
                    z++;
                    break;
                case EAST:
                    x++;
                    break;
                case SOUTH:
                    z--;
                    break;
                case WEST:
                    x--;
                    break;
            }

            current++;

            if (current == length) {
                current = 0;
                direction = direction.next();
                if (direction == Direction.SOUTH || direction == Direction.NORTH) {
                    length++;
                }
            }
        }
        return new Location(world, x * IridiumSkyblock.getInstance().getConfiguration().distance, 0, z * IridiumSkyblock.getInstance().getConfiguration().distance);
    }

    public Island(@NotNull String name, Schematics.FakeSchematic fakeSchematic) {
        this.name = name;
        this.biome = fakeSchematic.overworldData.biome;
        this.netherBiome = fakeSchematic.netherData.biome;
        this.schematic = fakeSchematic.overworldData.schematic;
        this.netherSchematic = fakeSchematic.netherData.schematic;
        this.lastRegenTime = 0L;
    }
}
