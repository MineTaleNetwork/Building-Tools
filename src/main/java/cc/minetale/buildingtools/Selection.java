package cc.minetale.buildingtools;

import cc.minetale.magma.MagmaUtils;
import cc.minetale.magma.MagmaWriter;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;
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

public class Selection {

    @Getter @Setter private Vec pos1;
    @Getter @Setter private Vec pos2;

    @Getter @Setter @Nullable private Instance instance;

    @Getter @Setter @Nullable private Player owner;

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

        MagmaUtils.load(this.instance, getMinChunkPos(), getMaxChunkPos())
                .thenAccept(region -> future.complete(MagmaWriter.write(region, path)));

        return future;
    }

    public int getSize() {
        return getLengthX() * getLengthY() * getLengthZ();
    }

    public Stream<Vec> getAllBlocks() {
        return Stream.iterate(
                new Vec(getMinBlockX(), getMinBlockY(), getMinBlockZ()),
                vec -> vec.blockZ() < getMaxBlockZ(),
                prevVec -> {
                    int nextX = prevVec.blockX() + 1;

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
        return Math.min(this.pos1.blockX(), this.pos2.blockX());
    }

    public int getMaxBlockX() {
        return Math.max(this.pos1.blockX(), this.pos2.blockX());
    }

    public int getMinBlockY() {
        return Math.min(this.pos1.blockY(), this.pos2.blockY());
    }

    public int getMaxBlockY() {
        return Math.max(this.pos1.blockY(), this.pos2.blockY());
    }

    public int getMinBlockZ() {
        return Math.min(this.pos1.blockZ(), this.pos2.blockZ());
    }

    public int getMaxBlockZ() {
        return Math.max(this.pos1.blockZ(), this.pos2.blockZ());
    }

    public int getLengthX() {
        return getMaxBlockX() - getMinBlockX();
    }

    public int getLengthY() {
        return getMaxBlockY() - getMinBlockY();
    }

    public int getLengthZ() {
        return getMaxBlockZ() - getMinBlockZ();
    }

    public Pos getMinBlockPos() {
        return new Pos(getMinBlockX(), getMinBlockY(), getMinBlockZ());
    }

    public Pos getMaxBlockPos() {
        return new Pos(getMaxBlockX(), getMaxBlockY(), getMaxBlockZ());
    }

    public int getMinChunkX() {
        return Math.min(this.pos1.chunkX(), this.pos2.chunkX());
    }

    public int getMaxChunkX() {
        return Math.max(this.pos1.chunkX(), this.pos2.chunkX());
    }

    public int getMinChunkZ() {
        return Math.min(this.pos1.chunkZ(), this.pos2.chunkZ());
    }

    public int getMaxChunkZ() {
        return Math.max(this.pos1.chunkZ(), this.pos2.chunkZ());
    }

    public Pos getMinChunkPos() {
        return new Pos(getMinChunkX(), 0, getMinChunkZ());
    }

    public Pos getMaxChunkPos() {
        return new Pos(getMaxChunkX(), 0, getMaxChunkZ());
    }

    public Chunk getMinChunk() {
        if(this.instance == null) { return null; }
        return this.instance.getChunkAt(getMinChunkPos());
    }

    public Chunk getMaxChunk() {
        if(this.instance == null) { return null; }
        return this.instance.getChunkAt(getMaxChunkPos());
    }

    private void load(Document document) {
        this.pos1 = Utils.documentToVector(document.get("pos1", Document.class));
        this.pos2 = Utils.documentToVector(document.get("pos2", Document.class));
    }

    public Document toDocument() {
        return new Document()
                .append("pos1", Utils.vectorToDocument(this.pos1))
                .append("pos2", Utils.vectorToDocument(this.pos2));
    }

}
