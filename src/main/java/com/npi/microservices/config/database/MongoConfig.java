package com.npi.microservices.config.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.npi.microservices.entity.ExchangeRatesEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexInfo;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.concurrent.TimeUnit;

import static com.npi.microservices.constant.Constants.FIELD_COUNTRY;
import static com.npi.microservices.constant.Constants.FIELD_DATE;

@Configuration
@RequiredArgsConstructor
@EnableMongoRepositories(basePackages = "com.npi.microservices.repository", mongoTemplateRef = "exchangeMongoTemplate")
public class MongoConfig {

    @Value("${mongodb.exchange.max.connection.count}")
    private int maxConnection;

    @Value("${mongodb.exchange.min.connection.count}")
    private int minConnection;

    @Value("${mongodb.exchange.timeout.connection}")
    private int timeoutConnection;

    @Value("${mongodb.exchange.uri}")
    private String uri;

    @Value("${mongodb.exchange.database}")
    private String databaseName;

    @Primary
    @Bean(name = "exchangeMongoClient")
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(uri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToConnectionPoolSettings(builder -> builder
                        .maxSize(maxConnection)
                        .minSize(minConnection))
                .applyToSocketSettings(builder -> builder
                        .connectTimeout(timeoutConnection, TimeUnit.MILLISECONDS))
                .build();
        return MongoClients.create(settings);
    }

    @Primary
    @Bean(name = "exchangeMongoDatabaseFactory")
    public MongoDatabaseFactory mongoDatabaseFactory() {
        return new SimpleMongoClientDatabaseFactory(mongoClient(), databaseName);
    }

    @Primary
    @Bean(name = "exchangeMongoTemplate")
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDatabaseFactory(), mappingMongoConverter());
    }

    // initialization index for field date with country  as date_country_idx
    @Bean
    public IndexOperations initIndex() {
        IndexOperations indexOps = mongoTemplate().indexOps(ExchangeRatesEntity.class);
        indexOps.getIndexInfo().stream()
                        .map(IndexInfo::getName)
                        .forEach(name -> {
                            String index = FIELD_DATE.concat("_").concat(FIELD_COUNTRY).concat("_idx");
                            if (name.equals((index))) {
                                indexOps.dropIndex(index);
                                indexOps.ensureIndex(new Index().on(FIELD_DATE, Sort.Direction.ASC).on(FIELD_COUNTRY, Sort.Direction.ASC).unique());
                            } else {
                                indexOps.ensureIndex(new Index().on(FIELD_DATE, Sort.Direction.ASC).on(FIELD_COUNTRY, Sort.Direction.ASC).unique());

                            }
                        });

        return indexOps;
    }

    // remove default field _class
    @Bean
    public MappingMongoConverter mappingMongoConverter() {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory());
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return converter;
    }

}
