package cc.minetale.buildingtools;

import cc.minetale.magma.MagmaUtils;
import cc.minetale.magma.MagmaWriter;
import cc.minetale.mlib.util.DocumentUtil;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class Selection implements Cloneable {

    @Getter @Setter private Vec pos1;
    @Getter @Setter private Vec pos2;

    @Getter @Setter private @Nullable Instance instance;

    @Getter @Setter private @Nullable Player owner;

    public Selection(Vec pos1, Vec pos2, Instance instance, Player owner) {
        this.pos1 = pos1;
        this.pos2 = pos2;

        this.instance = instance;
        this.owner = owner;
    }

    public Selection(Vec pos1, Vec pos2, Instance instance) {
        this(pos1, pos2, instance, null);
    }

    public Selection(Vec pos1, Vec pos2) {
        this(pos1, pos2, null, null);
    }

    public Selection(Selection other) {
        this(other.pos1, other.pos2, other.instance, other.owner);
    }

    private Selection() {}

    public static Selection fromDocument(Document document) {
        var selection = new Selection();
        selection.load(document);
        return selection;
    }

    public void setPositions(Vec pos1, Vec pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public boolean isIncomplete() {
        return this.pos1 == null || this.pos2 == null || this.instance == null;
    }

    public CompletableFuture<Boolean> save(Path path) {
        if(this.instance == null) { return CompletableFuture.completedFuture(false); }

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        MagmaUtils.load(this.instance, getMinBlockPos(), getMaxBlockPos())
                .thenAccept(region -> future.complete(MagmaWriter.write(region, path)));

        return future;
    }

    public double getSize() {
        return getLengthX() * getLengthY() * getLengthZ();
    }

    public int getBlockSize() {
        return getBlockLengthX() * getBlockLengthY() * getBlockLengthZ();
    }

    public Stream<Vec> getAllBlocks() {
        return Stream.iterate(
                getMinBlockPos(),
                vec -> vec.blockZ() <= getMaxBlockZ(),
                prevVec -> {
                    var nextX = prevVec.blockX() + 1;

                    var nextY = prevVec.blockY();
                    if(nextX > getMaxBlockX()) {
                        nextY++;
                        nextX = getMinBlockX();
                    }

                    var nextZ = prevVec.blockZ();
                    if(nextY > getMaxBlockY()) {
                        nextZ++;
                        nextY = getMinBlockY();
                    }

                    return new Vec(nextX, nextY, nextZ);
                });
    }

    public List<Chunk> getAllChunks() {
        List<Chunk> chunks = new ArrayList<>();

        if(this.instance == null) { return chunks; }

        for(int x = getMinChunkX(); x <= getMaxChunkX(); x++) {
            for(int z = getMinChunkZ(); z <= getMaxChunkZ(); z++) {
                chunks.add(this.instance.getChunk(x, z));
            }
        }

        return chunks;
    }

    public double getMinX() {
        return Math.min(this.pos1.x(), this.pos2.x());
    }

    public double getMaxX() {
        return Math.max(this.pos1.x(), this.pos2.x());
    }

    public double getMinY() {
        return Math.min(this.pos1.y(), this.pos2.y());
    }

    public double getMaxY() {
        return Math.max(this.pos1.y(), this.pos2.y());
    }

    public double getMinZ() {
        return Math.min(this.pos1.z(), this.pos2.z());
    }

    public double getMaxZ() {
        return Math.max(this.pos1.z(), this.pos2.z());
    }

    public int getMinBlockX() {
        return (int) Math.floor(getMinX());
    }

    public int getMaxBlockX() {
        return (int) Math.floor(getMaxX());
    }

    public int getMinBlockY() {
        return (int) Math.floor(getMinY());
    }

    public int getMaxBlockY() {
        return (int) Math.floor(getMaxY());
    }

    public int getMinBlockZ() {
        return (int) Math.floor(getMinZ());
    }

    public int getMaxBlockZ() {
        return (int) Math.floor(getMaxZ());
    }

    public double getLengthX() {
        return getMaxX() - getMinX();
    }

    public double getLengthY() {
        return getMaxY() - getMinY();
    }

    public double getLengthZ() {
        return getMaxZ() - getMinZ();
    }

    public int getBlockLengthX() {
        return getMaxBlockX() - getMinBlockX();
    }

    public int getBlockLengthY() {
        return getMaxBlockY() - getMinBlockY();
    }

    public int getBlockLengthZ() {
        return getMaxBlockZ() - getMinBlockZ();
    }

    public Vec getMinPos() {
        return new Vec(getMinX(), getMinY(), getMinZ());
    }

    public Vec getMaxPos() {
        return new Vec(getMaxX(), getMaxY(), getMaxZ());
    }

    public Vec getMinBlockPos() {
        return new Vec(getMinBlockX(), getMinBlockY(), getMinBlockZ());
    }

    public Vec getMaxBlockPos() {
        return new Vec(getMaxBlockX(), getMaxBlockY(), getMaxBlockZ());
    }

    public int getMinChunkX() {
        return (int) Math.floor(getMinX() / 16.0);
    }

    public int getMaxChunkX() {
        return (int) Math.floor(getMaxX() / 16.0);
    }

    public int getMinChunkZ() {
        return (int) Math.floor(getMinZ() / 16.0);
    }

    public int getMaxChunkZ() {
        return (int) Math.floor(getMaxZ() / 16.0);
    }

    public Vec getMinChunkPos() {
        return new Vec(getMinChunkX(), 0.0, getMinChunkZ());
    }

    public Vec getMaxChunkPos() {
        return new Vec(getMaxChunkX(), 0.0, getMaxChunkZ());
    }

    public Chunk getMinChunk() {
        return this.instance == null ? null : this.instance.getChunkAt(getMinChunkPos());
    }

    public Chunk getMaxChunk() {
        return this.instance == null ? null : this.instance.getChunkAt(getMaxChunkPos());
    }

    private void load(Document document) {
        this.pos1 = DocumentUtil.documentToVector(document.get("pos1", Document.class));
        this.pos2 = DocumentUtil.documentToVector(document.get("pos2", Document.class));
    }

    public Document toDocument() {
        return new Document()
                .append("pos1", DocumentUtil.vectorToDocument(this.pos1))
                .append("pos2", DocumentUtil.vectorToDocument(this.pos2));
    }

}
