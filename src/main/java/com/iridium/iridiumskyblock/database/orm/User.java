package com.iridium.iridiumskyblock.database.orm;

import com.iridium.iridiumskyblock.Role;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * ORM class representing a user in the database
 *
 * @author BomBardyGamer
 * @since 3.0
 */
@Getter
@Setter
@NoArgsConstructor
@DatabaseTable(tableName = "users")
public final class User {

    @DatabaseField(columnName = "uuid", canBeNull = false, id = true)
    @NotNull
    private UUID uuid;

    @DatabaseField(columnName = "name", canBeNull = false)
    @NotNull
    private String name;

    @DatabaseField(columnName = "island_id", foreign = true)
    @Nullable
    private Island island;

    @DatabaseField(columnName = "role", canBeNull = false)
    @Nullable
    private Role role;

    @DatabaseField(columnName = "last_creation_time", canBeNull = false)
    @NotNull
    private Long lastCreationTime;

    private boolean bypassing = false;

    private boolean islandChat = false;

    private boolean spying = false;

    private boolean tookInterestMessage = false;

    private IslandWarp islandWarp;

    @Setter(AccessLevel.PRIVATE)
    private List<Integer> islandInvites = new ArrayList<>();

    @Setter(AccessLevel.PRIVATE)
    private List<Object> holograms = new ArrayList<>();

    public void setLastCreationTime(LocalDateTime localDateTime) {
        this.lastCreationTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public LocalDateTime getLastCreationTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(lastCreationTime), ZoneId.systemDefault());
    }

    public User(final @NotNull UUID uuid, final @NotNull String name) {
        this.uuid = uuid;
        this.name = name;
        this.role = Role.Visitor;
        this.lastCreationTime = 0L;
    }
}
