CREATE
KEYSPACE IF NOT EXISTS myretail WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 3};

CREATE TABLE myretail.PRODUCT_DETAILS
(
    PRODUCT_ID       BIGINT,
    PRICE            DECIMAL,
    CREATE_TIMESTAMP timestamp,
    PRIMARY KEY (PRODUCT_ID)
)
    WITH bloom_filter_fp_chance = 0.01
        AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
        AND comment = ''
        AND compaction = {'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'}
        AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
        AND crc_check_chance = 1.0
        AND default_time_to_live = 0
        AND gc_grace_seconds = 60
        AND max_index_interval = 2048
        AND memtable_flush_period_in_ms = 0
        AND min_index_interval = 128
        AND read_repair_chance = 0.0
        AND speculative_retry = '99PERCENTILE';

