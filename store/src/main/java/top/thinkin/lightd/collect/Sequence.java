package top.thinkin.lightd.collect;

import org.rocksdb.RocksDBException;

public class Sequence {
    public final static String HEAD = "U";
    private final static byte[] HEAD_B = HEAD.getBytes();
    private final byte[] key_b;
    private DB db;
    private Long version;

    public synchronized long incr(Long increment) throws RocksDBException {
        if (version == null) {
            byte[] value = db.rocksDB().get(key_b);
            if (value == null) {
                version = 0L;
            } else {
                version = ArrayKits.bytesToLong(value);
            }
        }
        version = version + increment;
        db.rocksDB().put(key_b, ArrayKits.longToBytes(version));
        return version;
    }

    public Long get() {
        return version;
    }

    public Sequence(DB db, byte[] key) {
        this.db = db;
        this.key_b = ArrayKits.addAll(HEAD_B, key);
    }
}