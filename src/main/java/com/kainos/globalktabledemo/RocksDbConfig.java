package com.kainos.globalktabledemo;

import org.apache.kafka.streams.state.RocksDBConfigSetter;
import org.rocksdb.*;

import java.util.Map;

public class RocksDbConfig implements RocksDBConfigSetter {

    private final static long BYTE_FACTOR = 1;
    private final static long KB_FACTOR = 1024 * BYTE_FACTOR;
    private final static long MB_FACTOR = 1024 * KB_FACTOR;

    private final static long TOTAL_OFF_HEAP_MEMORY = 25 * MB_FACTOR;
    private final static double INDEX_FILTER_BLOCK_RATIO = 0.25;

    static {
        RocksDB.loadLibrary();
    }

    private static final org.rocksdb.Cache CACHE = new org.rocksdb.LRUCache(TOTAL_OFF_HEAP_MEMORY, -1, false, INDEX_FILTER_BLOCK_RATIO);


    @Override
    public void setConfig(final String storeName,
                          final Options options,
                          final Map<String, Object> configs) {
        options.setCompactionStyle(CompactionStyle.LEVEL);
        BlockBasedTableConfig tableConfig = (BlockBasedTableConfig) options.tableFormatConfig();
        tableConfig.setBlockCache(CACHE);

        tableConfig.setCacheIndexAndFilterBlocks(true);
        tableConfig.setCacheIndexAndFilterBlocksWithHighPriority(true);
        tableConfig.setPinTopLevelIndexAndFilter(true);
        options.setMaxWriteBufferNumber(4);
        //options.setWriteBufferSize(MEMTABLE_SIZE);
        // Enable compression (optional). Compression can decrease the required storage
        // and increase the CPU usage of the machine. For CompressionType values, see
        // https://javadoc.io/static/org.rocksdb/rocksdbjni/6.4.6/org/rocksdb/CompressionType.html.
        options.setCompressionType(CompressionType.LZ4_COMPRESSION);

        options.setTableFormatConfig(tableConfig);
    }

    @Override
    public void close(String storeName, Options options) {

    }
}