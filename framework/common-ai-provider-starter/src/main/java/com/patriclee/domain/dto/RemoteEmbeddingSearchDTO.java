package com.patriclee.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Remote embedding search request payload.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemoteEmbeddingSearchDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * User query text.
     */
    private String query;

    /**
     * Tenant or user identifier.
     */
    private String userId;

    /**
     * Target document index identifiers.
     */
    private List<String> documentIndexIds;

    /**
     * Optional file names for filtering.
     */
    private List<String> fileNames;
}
