package org.jhipster.blog.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of PreferencesSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PreferencesSearchRepositoryMockConfiguration {

    @MockBean
    private PreferencesSearchRepository mockPreferencesSearchRepository;

}
