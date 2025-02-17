package com.wlminus.ufp.config.cassandra;

import com.codahale.metrics.MetricRegistry;
import com.datastax.driver.core.*;
import com.datastax.driver.extras.codecs.jdk8.InstantCodec;
import com.datastax.driver.extras.codecs.jdk8.LocalDateCodec;
import com.datastax.driver.extras.codecs.jdk8.ZonedDateTimeCodec;
import io.github.jhipster.config.JHipsterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.autoconfigure.cassandra.ClusterBuilderCustomizer;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.cassandra.core.convert.CassandraCustomConversions;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
@EnableCassandraRepositories("com.wlminus.ufp.repository")
@EntityScan("com.wlminus.ufp.domain.*")
@Profile({JHipsterConstants.SPRING_PROFILE_DEVELOPMENT, JHipsterConstants.SPRING_PROFILE_PRODUCTION})
public class CassandraConfiguration implements InitializingBean {

    @Value("${spring.data.cassandra.protocolVersion:V4}")
    private ProtocolVersion protocolVersion;

    @Autowired(required = false)
    MetricRegistry metricRegistry;

    private final Logger log = LoggerFactory.getLogger(CassandraConfiguration.class);

    @Autowired
    private Cluster cluster;

    @Override
    public void afterPropertiesSet() {
        TupleType tupleType = cluster.getMetadata()
            .newTupleType(DataType.timestamp(), DataType.varchar());

        cluster.getConfiguration().getCodecRegistry()
            .register(LocalDateCodec.instance)
            .register(InstantCodec.instance)
            .register(new ZonedDateTimeCodec(tupleType));

        cluster.init();
    }

    @Bean
    public CassandraCustomConversions cassandraCustomConversions(Cluster cluster) {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(TupleToZonedDateTimeConverter.INSTANCE);
        converters.add(new ZonedDateTimeToTupleConverter(protocolVersion, cluster.getConfiguration().getCodecRegistry()));
        return new CassandraCustomConversions(converters);
    }

    @ReadingConverter
    enum TupleToZonedDateTimeConverter implements Converter<TupleValue, ZonedDateTime> {
        INSTANCE;

        @Override
        public ZonedDateTime convert(TupleValue source) {
            java.util.Date timestamp = source.getTimestamp(0);
            ZoneId zoneId = ZoneId.of(source.getString(1));
            return timestamp.toInstant().atZone(zoneId);
        }
    }

    @WritingConverter
    class ZonedDateTimeToTupleConverter implements Converter<ZonedDateTime, TupleValue> {

        private TupleType type;

        public ZonedDateTimeToTupleConverter(ProtocolVersion version, CodecRegistry codecRegistry) {
            type = TupleType.of(version, codecRegistry, DataType.timestamp(), DataType.text());
        }

        @Override
        public TupleValue convert(@Nonnull ZonedDateTime source) {
            TupleValue tupleValue = type.newValue();
            tupleValue.setTimestamp(0, Date.from(source.toLocalDateTime().toInstant(ZoneOffset.UTC)));
            tupleValue.setString(1, source.getZone().toString());
            return tupleValue;
        }
    }

    @Bean
    ClusterBuilderCustomizer clusterBuilderCustomizer(CassandraProperties properties) {
        return builder -> builder
            .withProtocolVersion(protocolVersion)
            .withPort(getPort(properties))
            .withoutJMXReporting()
            .withoutMetrics();
    }

    protected int getPort(CassandraProperties properties) {
        return properties.getPort();
    }

    @Bean(destroyMethod = "close")
    public Session session(CassandraProperties properties, Cluster cluster) {
        log.debug("Configuring Cassandra session");
        return StringUtils.hasText(properties.getKeyspaceName()) ? cluster.connect(properties.getKeyspaceName()) : cluster.connect();
    }
}
