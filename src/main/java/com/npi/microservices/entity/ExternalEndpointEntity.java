package com.npi.microservices.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import static com.npi.microservices.constant.Constants.COLUMN_HTTP_METHOD;
import static com.npi.microservices.constant.Constants.TABLE_NAME;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TABLE_NAME)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExternalEndpointEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @NotNull
    @PrimaryKeyJoinColumn
    String country;

    @NotNull
    @Column(name = COLUMN_HTTP_METHOD)
    String httpMethod;

    @NotNull
    String url;

}
